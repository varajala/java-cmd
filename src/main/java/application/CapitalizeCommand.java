package application;

public class CapitalizeCommand implements Command {

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
        out.call(args[0].toUpperCase());
        exit.exitWithStatus(0);
    }

    
    @Override
    public String getHelp(String alias) {
        String[] lines = {
                "Return all uppercase version of the given input.",
                "Only one statement can be processed.\n",
                "EXAMPLE:",
                String.format("$ %s test [RET]", alias),
                "-> TEST"
        };
        return String.join("\n", lines);
    }
}
