package application;

public class IsPalindromeCommand implements Command {

    @Override
    public void execute(
            String[] args,
            DisplayOutputCall out,
            DisplayErrorCall error,
            ExitCallback exit) {
        if (args.length != 1) {
            String lengthStr = Integer.toString(args.length);
            error.call("Expected a single argument, got: " + lengthStr);
            exit.exitWithStatus(1);
            return;
        }
        String input = args[0];
        String output = String.format("FALSE, %s is not a palindrome", input);
        if(input.equals(reverseString(input))) {
            output = String.format("TRUE, %s is a palindrome", input);
        }
        out.call(output);
        exit.exitWithStatus(0);
    }
    
    @Override
    public String getHelp(String alias) {
        String[] lines = {
                "Check if the input given is a palindrome.",
                "This is case sensitive and can process only one statement at a time.\n",
                "EXAMPLE:",
                String.format("$ %s test [RET]", alias),
                "-> FALSE, test is not a palindrome"
        };
        return String.join("\n", lines);
    }

    
    private String reverseString(String input) {
        StringBuffer sb = new StringBuffer();
        for (int i = input.length() - 1; i >= 0; i--) {
            sb.append(input.charAt(i));
        }
        return sb.toString();
    }
}
