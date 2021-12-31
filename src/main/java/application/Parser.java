package application;

import java.util.regex.*;
import java.util.List;
import java.util.ArrayList;

/**
 * @author varajala
 * @version Mar 16, 2021
 * A simple command line parser.
 */
public class Parser {
    
    private final String CMD_RE = "^[\\w\\./\\\\:-]+";
    private final String ARG_RE = "(?<=(\\s|\\A))\"[^\"]*\"|(?<=(\\s|\\A))[^\\s]+";
    
    
    /**
     * @param line Line of text.
     * @return A list where first item is a command and
     *         all other items arguments passed.
     */
    public List<String> parse(String line) {
        ArrayList<String> results = new ArrayList<String>();
        Pattern pattern = Pattern.compile(CMD_RE);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            String command = matcher.group();
            results.add(command);
            String args = line.substring(command.length());
            findArgs(args, results);
            
        }
        return results;
    }

    private void findArgs(String args, ArrayList<String> results) {
        Pattern pattern = Pattern.compile(ARG_RE);
        Matcher matcher = pattern.matcher(args);
        while (matcher.find()) {
            String arg = matcher.group();
            results.add(arg.replaceAll("\"", ""));
        }
    }
}
