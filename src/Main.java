
import pt.up.fe.comp.jmm.JmmParser;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;

import java.util.Arrays;
import java.util.ArrayList;
import java.io.StringReader;
import java.util.List;

public class Main implements JmmParser {


    public JmmParserResult parse(String jmmCode) {
        List<Report> reportList = new ArrayList<Report>();
        try {
            Grammar grammar = new Grammar(new StringReader(jmmCode));
            SimpleNode root = grammar.Program(); // returns reference to root node

            reportList  = grammar.getReportList();

            for (int i = 0 ; i < reportList.size(); i++){
                //System.out.println(reportList.get(i));
            }
           if (!grammar.getHasError()){
                root.dump("");
                //System.out.println(root.toJson());
           }

            return new JmmParserResult(root, reportList);
        } catch (ParseException e) {
            int lineError = e.getStackTrace()[0].getLineNumber();
            String message = e.getMessage();
            reportList.add(new Report(ReportType.ERROR, Stage.SYNTATIC, lineError, message));
            return new JmmParserResult(null, reportList);
        }
    }

    public static void main(String[] args) {
        System.out.println("Executing with args: " + Arrays.toString(args));
        if (args[0].contains("fail")) {
            throw new RuntimeException("It's supposed to fail");
        }

    }


}