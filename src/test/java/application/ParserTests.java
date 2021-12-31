package application;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import application.Parser;


public class ParserTests {

    private final Parser PARSER = new Parser();
    
    
    @Test
    public void testValidParsing() {
        String line = "sum 1 2 3 33 22";
        List<String> result = PARSER.parse(line);
        
        String cmd = result.remove(0); 
        assertEquals("sum", cmd);
        String args = "[1, 2, 3, 33, 22]";
        assertEquals(args, Arrays.toString(result.toArray()));
        
        line = "help";
        result = PARSER.parse(line);
        cmd = result.remove(0); 
        assertEquals("help", cmd);
        assertTrue(result.size() == 0);
        
        line = "some-command 1.123 \"some arg with spaces\" arg";
        result = PARSER.parse(line);
        cmd = result.remove(0);
        assertEquals("some-command", cmd);
        args = "[1.123, some arg with spaces, arg]";
        assertEquals(args, Arrays.toString(result.toArray()));
        
        line = "./a.out --file some.txt";
        result = PARSER.parse(line);
        cmd = result.remove(0);
        assertEquals("./a.out", cmd);
        args = "[--file, some.txt]";
        assertEquals(args, Arrays.toString(result.toArray()));
        
        line = "mkdir C:Users\\varajala\\Documents";
        result = PARSER.parse(line);
        cmd = result.remove(0);
        assertEquals("mkdir", cmd);
        args = "[C:Users\\varajala\\Documents]";
        assertEquals(args, Arrays.toString(result.toArray()));
        
        line = "mkdir /home/varajala/Documents/folder";
        result = PARSER.parse(line);
        cmd = result.remove(0);
        assertEquals("mkdir", cmd);
        args = "[/home/varajala/Documents/folder]";
        assertEquals(args, Arrays.toString(result.toArray()));
    }
    
}
