package application;


/**
 * @author varajala
 * @version Mar 17, 2021
 * A simple testing 'environment' to execute single commands and
 * examine the output without any user interface.
 */
public class TestingEnv { 
    
    private class DoneCallback implements ExitCallback {
        @Override
        public void exitWithStatus(int exitCode) {
            setExitCode(exitCode);
        }
    }
    
    private class DisplayOutput implements DisplayOutputCall {
        @Override
        public void call(String output) {
            logOutput(output);
        }
    }
    
    private class DisplayError implements DisplayErrorCall {
        @Override
        public void call(String info) {
            logError(info);
        }
    }
    
    private final StringBuffer OUTPUT_BUFFER = new StringBuffer();
    private final StringBuffer ERROR_BUFFER = new StringBuffer();
    
    private int exitCode = -1;
    
    
    public void executeCommand(Command cmd, String[] args) {
        cmd.execute(args, new DisplayOutput(), new DisplayError(), new DoneCallback());
    }
    
    
    public String[] getErrorLines() {
        return ERROR_BUFFER.toString().split("\\n");
    }
    
    
    public String[] getOutputLines() {
        return OUTPUT_BUFFER.toString().split("\\n");
    }
    
    
    public String getOutput() {
        return OUTPUT_BUFFER.toString();
    }
    
    
    public int getExitCode() {
        return this.exitCode;
    }
    
    
    public void reset() {
        int capacity = OUTPUT_BUFFER.capacity();
        OUTPUT_BUFFER.delete(0, capacity);
        
        capacity = ERROR_BUFFER.capacity();
        ERROR_BUFFER.delete(0, capacity);
        
        setExitCode(-1);
    }
    
    
    private void setExitCode(int code) {
        this.exitCode = code;
    }
    
    
    private void logOutput(String output) {
        OUTPUT_BUFFER.append(output + "\n");
    }
    
    
    private void logError(String info) {
        ERROR_BUFFER.append(info + "\n");
    }
    
}
