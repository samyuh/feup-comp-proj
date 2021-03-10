
import pt.up.fe.comp.jmm.JmmParser;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.report.Report;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.Arrays;
import java.util.ArrayList;

/**
 * This file is the main of the Grammar.jjt.
 */
public class Main implements JmmParser {
	public JmmParserResult parse(String filePath) {

		try {
		    Grammar grammar = new Grammar(new FileInputStream(new File(filePath)));
    		SimpleNode root = grammar.Goal();
            	
    		root.dump(""); // prints the tree on the screen
    	
    		return new JmmParserResult(root, new ArrayList<Report>());
		} catch(ParseException | FileNotFoundException e) {
			throw new RuntimeException("Error while parsing", e);
		}
	}

    public static void main(String[] args) {
        System.out.println("Executing with args: " + Arrays.toString(args));
        if (args[0].contains("fail")) {
            throw new RuntimeException("It's supposed to fail");
        }
    }


}