
import pt.up.fe.comp.jmm.JmmParser;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.jasmin.JasminResult;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;
import pt.up.fe.specs.util.SpecsIo;

import java.util.Arrays;
import java.util.ArrayList;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Main implements JmmParser {
    public JmmParserResult parse(String jmmCode) {
        List<Report> reportList = new ArrayList<Report>();
        try {
            Grammar grammar = new Grammar(new StringReader(jmmCode));
            SimpleNode root = grammar.Program(); // returns reference to root node

            reportList  = grammar.getReportList();

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
        String fileName = args[0];

        // Parse filename and optimization options
        List<String> options = new ArrayList<>();
        Collections.addAll(options, args);

        boolean optimizeR = false;
        int maxRegisters = -1;
        boolean optimizeO = options.contains("-o");
        // Get optmization
        int index = IntStream.range(0, args.length).filter(i-> args[i].startsWith("-r=")).findFirst().orElse(-1);
        if (index != -1){
            optimizeR = true;
            maxRegisters = Integer.parseInt(args[index].split("=")[1]);
        }

        // Parse results
        JmmParser parser = new Main();
        JmmParserResult jmmParserResult = parser.parse(SpecsIo.read(fileName));

        // Semantic Analysis
        AnalysisStage analysis = new AnalysisStage();
        JmmSemanticsResult semanticsResult = analysis.semanticAnalysis(jmmParserResult);

        // Optimization
        OptimizationStage optimization = new OptimizationStage();
        if (optimizeO) {
            semanticsResult = optimization.optimize(semanticsResult);
        }

        OllirResult ollirResult = optimization.toOllir(semanticsResult, optimizeO);

        if (optimizeR) {
            ollirResult = optimization.optimize(ollirResult);
        }

        // Backend

        BackendStage backend = new BackendStage();
        backend.setOptimizeR(optimizeR);
        backend.setMaxRegisters(maxRegisters);

        JasminResult jasminResult = backend.toJasmin(ollirResult);
        if (jasminResult.getReports().size() != 0){
            for (var report: jasminResult.getReports()){
                System.out.println(report.toString());
            }
            return;
        }
        jasminResult.run();

    }
}