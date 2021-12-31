package application;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class Interpreter {
    
    private static boolean RUNNING = true;
    private static String PROMPT = ">>> ";
    private static String OUTPUT_PREFIX = "-> ";
    private static String ERROR_PREFIX = "ERROR: ";
    private static final String EXIT = "exit";
    private static final String HELP = "help";
    
    private static final Parser PARSER = new Parser();
    private static final Hashtable<String, Command> COMMANDS = new Hashtable<String, Command>();
    private static final Scanner SCANNER = new Scanner(System.in);
    
    private static class EXIT implements Command {
        @Override
        public void execute(
                String[] args,
                DisplayOutputCall out,
                DisplayErrorCall err,
                ExitCallback exit) {
            Interpreter.RUNNING = false;
            out.call("Exiting...");
            exit.exitWithStatus(0);
        }
        
        @Override
        public String getHelp(String alias) {
            return "Exit the program...";
        }
    }
    
    private static class HELP implements Command {
        @Override
        public void execute(
                String[] args,
                DisplayOutputCall out,
                DisplayErrorCall err,
                ExitCallback exit) {
            showHelp();
            exit.exitWithStatus(0);
        }
        
        @Override
        public String getHelp(String alias) {
            return "Displays this view...";
        }
    }
    
    private static class DoneCallback implements ExitCallback {
        @Override
        public void exitWithStatus(int exitCode) {
            Interpreter.handleCommandDone(exitCode);
        }
    }
    
    private static class DisplayOutput implements DisplayOutputCall {
        @Override
        public void call(String output) {
            Interpreter.displayOutput(output);
        }
    }
    
    private static class DisplayError implements DisplayErrorCall {
        @Override
        public void call(String output) {
            Interpreter.displayError(output);
        }
    }
    
    
    public static void run() {
        registerCommands();
        mainloop();
    }
    
    private static void registerCommands() {
        COMMANDS.put(EXIT, new EXIT());
        COMMANDS.put(HELP, new HELP());
        COMMANDS.put("sum", new SumCommand());
        COMMANDS.put("upper", new CapitalizeCommand());
        COMMANDS.put("palindrome", new IsPalindromeCommand());
    }
    
    
    private static void mainloop() {
        while (RUNNING) {
            String input = readInput();
            if (input.equals("")) continue;
            List<String> parsedCommand = parseInput(input);
            if (parsedCommand.isEmpty()) {
                String info = "ERROR: invalid command";
                displayOutput(info);
                continue;
            }
            String command = parsedCommand.remove(0);
            executeCommand(command, parsedCommand);
        }
    }
    
    
    private static String readInput() {
        System.out.print(PROMPT);
        return SCANNER.nextLine().trim();
    }
    
    
    private static List<String> parseInput(String line) {
        return PARSER.parse(line);
    }
    
    
    private static void executeCommand(String command, List<String> args) {
        if (COMMANDS.containsKey(command)) {
            int numberOfArgs = args.size();
            String[] arguments = new String[numberOfArgs];
            arguments = args.toArray(arguments);
            Command cmd = COMMANDS.get(command);
            cmd.execute(arguments, new DisplayOutput(), new DisplayError(), new DoneCallback());
            return;
        }
        String info = "Unknown command: " + command;
        displayError(info);
    }
    
    
    private static void handleCommandDone(int exitCode) {
        //TODO
    }
    
    
    private static void displayOutput(String output) {
        System.out.println(OUTPUT_PREFIX + output);
    }
    
    
    private static void displayError(String info) {
        System.out.println(ERROR_PREFIX + info);
    }
    
    
    private static void showHelp() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n== HELP ==\n");
        Enumeration<String> keysEnum = COMMANDS.keys();
        while (keysEnum.hasMoreElements()) {
            String commandAlias = keysEnum.nextElement();
            Command cmd = COMMANDS.get(commandAlias);
            String helpMessage = cmd.getHelp(commandAlias);
            sb.append(commandAlias + " -> " + helpMessage + "\n\n\n");
        }
        sb.append("\n- - - - - - - - - - - - - - - - - - - - - - - - -\n");
        displayOutput(sb.toString());
    }
    
}
