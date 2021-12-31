package application;

public interface Command {
    
    public String getHelp(String alias);
    
    public void execute(
            String[] args,
            DisplayOutputCall display,
            DisplayErrorCall error,
            ExitCallback done);
    
}
