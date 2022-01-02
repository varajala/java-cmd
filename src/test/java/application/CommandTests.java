package application;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class CommandTests {
    
    private static final TestingEnv TEST_ENV = new TestingEnv();
    
    
    @Test
    public void testSum() {
        TEST_ENV.executeCommand(new SumCommand(), new String[] {"1", "1", "1"});
        assertEquals(0, TEST_ENV.getExitCode());
        assertEquals("3.0\n", TEST_ENV.getOutput());
        TEST_ENV.reset();
        
        TEST_ENV.executeCommand(new SumCommand(), new String[] {"1", "10", "100"});
        assertEquals(0, TEST_ENV.getExitCode());
        assertEquals("111.0\n", TEST_ENV.getOutput());
        TEST_ENV.reset();
        
        TEST_ENV.executeCommand(new SumCommand(), new String[] {"1.25", "10.25", "100.25"});
        assertEquals(0, TEST_ENV.getExitCode());
        assertEquals("111.75\n", TEST_ENV.getOutput());
        TEST_ENV.reset();
        
        TEST_ENV.executeCommand(new SumCommand(), new String[] {"-1.125", "-10.5", "100"});
        assertEquals(0, TEST_ENV.getExitCode());
        assertEquals("88.375\n", TEST_ENV.getOutput());
        TEST_ENV.reset();
        
        TEST_ENV.executeCommand(new SumCommand(), new String[] {"-1.125", "-10.5", "-100"});
        assertEquals(0, TEST_ENV.getExitCode());
        assertEquals("-111.625\n", TEST_ENV.getOutput());
        TEST_ENV.reset();
    }
    
    
    @Test
    public void testCapitalize() {
        TEST_ENV.executeCommand(new CapitalizeCommand(), new String[] {"asd"});
        assertEquals(0, TEST_ENV.getExitCode());
        assertEquals("ASD\n", TEST_ENV.getOutput());
        TEST_ENV.reset();
        
        TEST_ENV.executeCommand(new CapitalizeCommand(), new String[] {"1.0"});
        assertEquals(0, TEST_ENV.getExitCode());
        assertEquals("1.0\n", TEST_ENV.getOutput());
        TEST_ENV.reset();
        
        TEST_ENV.executeCommand(new CapitalizeCommand(), new String[] {});
        assertEquals(1, TEST_ENV.getExitCode());
        String[] errLines = TEST_ENV.getErrorLines();
        assertEquals(errLines.length, 1);
        assertEquals("Expected a single argument, got: 0", errLines[0]);
        TEST_ENV.reset();
        
        TEST_ENV.executeCommand(new CapitalizeCommand(), new String[] {"asd", "asd"});
        assertEquals(1, TEST_ENV.getExitCode());
        errLines = TEST_ENV.getErrorLines();
        assertEquals(errLines.length, 1);
        assertEquals("Expected a single argument, got: 2", errLines[0]);
        TEST_ENV.reset();
    }
    
    
    @Test
    public void testPalindrome() {
        TEST_ENV.executeCommand(new IsPalindromeCommand(), new String[] {"asd"});
        assertEquals(0, TEST_ENV.getExitCode());
        assertEquals("FALSE, asd is not a palindrome\n", TEST_ENV.getOutput());
        TEST_ENV.reset();
        
        TEST_ENV.executeCommand(new IsPalindromeCommand(), new String[] {"asddsa"});
        assertEquals(0, TEST_ENV.getExitCode());
        assertEquals("TRUE, asddsa is a palindrome\n", TEST_ENV.getOutput());
        TEST_ENV.reset();
        
        TEST_ENV.executeCommand(new IsPalindromeCommand(), new String[] {"1.0"});
        assertEquals(0, TEST_ENV.getExitCode());
        assertEquals("FALSE, 1.0 is not a palindrome\n", TEST_ENV.getOutput());
        TEST_ENV.reset();
        
        TEST_ENV.executeCommand(new IsPalindromeCommand(), new String[] {"Asddsa"});
        assertEquals(0, TEST_ENV.getExitCode());
        assertEquals("FALSE, Asddsa is not a palindrome\n", TEST_ENV.getOutput());
        TEST_ENV.reset();
        
        TEST_ENV.executeCommand(new IsPalindromeCommand(), new String[] {"asd", "asd"});
        assertEquals(1, TEST_ENV.getExitCode());
        String[] errLines = TEST_ENV.getErrorLines();
        assertEquals(errLines.length, 1);
        assertEquals("Expected a single argument, got: 2", errLines[0]);
        TEST_ENV.reset();
    }
}
