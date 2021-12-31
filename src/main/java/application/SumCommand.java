package application;

public class SumCommand implements Command {
    
    private double sum = 0.0;

    @Override
    public void execute(
            String[] args,
            DisplayOutputCall out,
            DisplayErrorCall error,
            ExitCallback exit) {
        for (String str : args) {
            double number;
            try {
                number = Double.parseDouble(str);
            } catch (NumberFormatException e) {
                error.call("Invalid argument " + str);
                exit.exitWithStatus(1);
                return;
            }
            sum += number;
            
        }
        out.call(Double.toString(sum));
        exit.exitWithStatus(0);
    }
    
    
    @Override
    public String getHelp(String alias) {
        String[] lines = {
                "Calculate the sum of all given numbers.",
                "Integers or decimal numbers are valid.\n",
                "EXAMPLE:",
                String.format("$ %s 2.5 3.5 [RET]", alias),
                "-> 6.0"
        };
        return String.join("\n", lines);
    }

}
