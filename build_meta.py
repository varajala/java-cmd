import os
import glob
import sys
import re
import shutil
import typing
import subprocess as subp
from contextlib import suppress
from dataclasses import dataclass


JAVAC_CMD = 'javac'
JAVAC_CLASSPATH_FLAG = '-classpath'
JAVAC_MODULE_PATH_FLAG = '--module-path'
JAVAC_ADD_MODULES_FLAG = '--add-modules'
JAVAC_OUTPUT_DIR_FLAG = '-d'

JAVA_CMD = 'java'
JAVA_JAR_FLAG = '-jar'
JAVA_CLASSPATH_FLAG = '-classpath'
JAVA_MODULE_PATH_FLAG = '--module-path'
JAVA_ADD_MODULES_FLAG = '--add-modules'

JAR_CMD = 'jar'
JAR_CREATE_FLAG = '--create'
JAR_OUTPUT_FILE_FLAG = '--file'
JAR_ENTRYPOINT_FLAG = '--main-class'


class ChangeWorkingDirectory:
    def __init__(self, path: str):
        self.cwd = os.getcwd()
        self.path = path

    def __enter__(self):
        os.chdir(self.path)
        return self.path

    def __exit__(self, exc_type, exc, tb):
        os.chdir(self.cwd)


@dataclass
class JavaBuildOptions:
    copy_files:         typing.Optional[list] = None
    source_files:       typing.Optional[list] = None
    output_directory:   typing.Optional[str] = None
    include_modules:    typing.Optional[typing.Tuple[list, list]] = None
    classpath:          typing.Optional[list] = None
    main_class:         typing.Optional[str] = None


def format_classpath(options: JavaBuildOptions) -> str:
    return '' if not options.classpath else os.pathsep.join(options.classpath)


def create_build_command(options: JavaBuildOptions) -> typing.List[str]:
    command = [JAVAC_CMD]
    if not options.source_files:
        raise RuntimeError('No source files provided')
    
    command.extend(options.source_files)
    
    if options.include_modules:
        module_paths, modules = options.include_modules
        
        command.append(JAVAC_MODULE_PATH_FLAG)
        path_listing = os.pathsep.join(module_paths)
        command.append(path_listing)

        command.append(JAVAC_ADD_MODULES_FLAG)
        module_listing = ','.join(modules)
        command.append(module_listing)

    if options.classpath:
        command.append(JAVAC_CLASSPATH_FLAG)
        command.append(format_classpath(options))
    
    if options.output_directory:
        command.append(JAVAC_OUTPUT_DIR_FLAG)
        command.append(options.output_directory)
    return command


def create_run_command(options: JavaBuildOptions) -> typing.List[str]:
    command = [JAVA_CMD]

    if options.classpath:
        command.append(JAVA_CLASSPATH_FLAG)
        command.append(format_classpath(options))
    
    if options.include_modules:
        module_paths, modules = options.include_modules
        
        command.append(JAVA_MODULE_PATH_FLAG)
        path_listing = os.pathsep.join(module_paths)
        command.append(path_listing)

        command.append(JAVA_ADD_MODULES_FLAG)
        module_listing = ','.join(modules)
        command.append(module_listing)

    if options.main_class is None:
        raise RuntimeError('Missing the main class option required to run a java project')
    
    command.append(options.main_class)
    return command


def get_build_options() -> JavaBuildOptions:
    return JavaBuildOptions()


def change_working_dir(path):
    return ChangeWorkingDirectory(path)


def find_files_with_pattern(pattern: str, path: str, *, only_names = False, exclude_files = None) -> list:
    with change_working_dir(path):
        if only_names:
            output = glob.glob(pattern)        
        else:
            output = [os.path.join(path, filename) for filename in glob.glob(pattern)]
    
    for entry in os.scandir(path):
        if entry.is_dir():
            output.extend(
                find_files_with_pattern(
                    pattern,
                    entry.path,
                    only_names = only_names,
                    exclude_files=exclude_files
                    )
                )

    def filter_results(files: list) -> list:
        filtered_files = files.copy()
        
        if exclude_files is None:
            return filtered_files

        with suppress(Exception):
            regex = re.compile(exclude_files)
            filtered_files = filter(lambda path: not re.match(regex, path), files)
        return filtered_files

    return filter_results(output)


def copy_files(src, dst, patterns):
    def find_files_to_copy(path, patterns):
        output = list()
        for pattern in patterns:
            results = find_files_with_pattern(pattern, path)
            output.extend(results)
        return output
    
    files = find_files_to_copy(src, patterns)
    for file in files:
        rel_path = file.replace(src, '')
        if rel_path.startswith(os.sep):
            rel_path = rel_path.replace(os.sep, '', 1)
        
        target = os.path.join(dst, rel_path)
        shutil.copy(file, target)


def run_command(command: typing.List[str], *, path: str = os.getcwd(), output_stream: typing.TextIO = sys.stdout) -> bool:
    proc = subp.run(command, capture_output=True, text=True, cwd=path)
    if proc.stdout:
        output_stream.write(proc.stdout)
    if proc.stderr:
        output_stream.write(proc.stderr)
    return proc.returncode == 0


def start_process(command: typing.List[str], *, path: str = os.getcwd(), output_stream = sys.stdout, input_stream = sys.stdin):
    proc = subp.Popen(command, stdout=output_stream, stderr=output_stream, stdin=input_stream, cwd=path)
    try:
        proc.wait()
    except (KeyboardInterrupt, SystemExit):
        proc.kill()


def exec_jar(jar_path: str, run_path: str = os.getcwd(), *additional_commandline_args):
    command = [JAVA_CMD, JAVA_JAR_FLAG]
    command.append(jar_path)
    command.extend(additional_commandline_args)
    start_process(command, path = run_path)


def build_only(options: JavaBuildOptions, *, path: str, output_stream=sys.stdout) -> bool:
    build_cmd = create_build_command(options)
    success = run_command(build_cmd, path = path, output_stream = output_stream)
    if options.copy_files and success:
        src_directory, dst_directory, patterns = options.copy_files
        copy_files(src_directory, dst_directory, patterns)
    return success


def run_only(options: JavaBuildOptions, *, path: str, output_stream=sys.stdout):
    run_command = create_run_command(options)
    start_process(run_command, path = path, output_stream = output_stream)


def build_and_run(options: JavaBuildOptions, *, build_path: str, run_path: str, output_stream=sys.stdout):
    success = build_only(options, path=build_path, output_stream=output_stream)
    if not success:
        return
    
    run_only(options, path = run_path, output_stream = output_stream)


def create_jar(
    output_jar: str,
    class_files: typing.List[str],
    class_file_path: str,
    *, entrypoint: typing.Optional[str] = None,
    output_stream = sys.stdout
    ) -> bool:
    command = [JAR_CMD, JAR_CREATE_FLAG, JAR_OUTPUT_FILE_FLAG, os.path.abspath(output_jar)]
    if entrypoint is not None:
        command.append(JAR_ENTRYPOINT_FLAG)
        command.append(entrypoint)

    if not class_files:
        raise RuntimeError('No .class files given for packaging...')
    command.extend(class_files)

    return run_command(command, path=class_file_path, output_stream=output_stream)
