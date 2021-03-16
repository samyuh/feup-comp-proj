
import pt.up.fe.comp.jmm.JmmParser;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.report.Report;

import java.io.FileInputStream;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.StringReader;

public class Main implements JmmParser {

    public JmmParserResult parse(String jmmCode) {

        try {
            Grammar grammar = new Grammar(new StringReader(jmmCode));
            SimpleNode root = grammar.Program(); // returns reference to root node

            root.dump("");

            // TODO: PARSE JSON ON SIMPLENODE
            //System.out.println(root.toJson());

            return new JmmParserResult(root, new ArrayList<Report>());
        } catch (ParseException e) {
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