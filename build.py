import os
import build_meta as build


ROOT_DIRECTORY = os.path.abspath(os.path.dirname(__file__))
SRC_DIRECTORY = os.path.join(ROOT_DIRECTORY, 'src')
BIN_DIRECTORY = os.path.join(ROOT_DIRECTORY, 'bin')

# Insert path to JUnit dependencies here
JARS_LIB_PATH = '/home/varajala/dev/java/.lib/jars'
# Standalone console platform jar
JUNIT_JAR_PATH = os.path.join(JARS_LIB_PATH, 'junit-platform-console-standalone-1.8.0.jar')


def do_build():
    options = build.get_build_options()
    
    options.output_directory = BIN_DIRECTORY
    options.source_files = build.find_files_with_pattern('*.java', SRC_DIRECTORY)

    # Update versions here
    options.classpath = ('.',
        os.path.join(JARS_LIB_PATH, 'apiguardian-api-1.0.0.jar'),        
        os.path.join(JARS_LIB_PATH, 'jars/junit-jupiter-5.7.0.jar'),
        os.path.join(JARS_LIB_PATH, 'junit-jupiter-api-5.7.0.jar'),
        os.path.join(JARS_LIB_PATH, 'junit-jupiter-engine-5.7.0.jar'),
        os.path.join(JARS_LIB_PATH, 'junit-jupiter-params-5.7.0.jar'),
        )

    options.main_class = 'application.Main'

    # To build and run:
    # build.build_and_run(options = options, build_path = ROOT_DIR, run_path = BIN_DIRECTORY)
    
    # To build, run tests and run application:
    build.build_only(options = options, path = ROOT_DIRECTORY)
    build.exec_jar(
        JUNIT_JAR_PATH,
        BIN_DIRECTORY,
        build.JAVA_CLASSPATH_FLAG,
        build.format_classpath(options),
        '--scan-classpath'
        )
    build.run_only(options = options, path = BIN_DIRECTORY)


if __name__ == '__main__':
    do_build()
