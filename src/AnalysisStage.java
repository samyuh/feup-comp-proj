
import java.util.Arrays;
import analysis.Analysis;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.analysis.JmmAnalysis;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;
import visitor.*;

// TODO: example of the new step.
// TODO: declare and build symbol table here.
public class AnalysisStage implements JmmAnalysis {

    @Override
    public JmmSemanticsResult semanticAnalysis(JmmParserResult parserResult) {
        if (TestUtils.getNumReports(parserResult.getReports(), ReportType.ERROR) > 0) {
            var errorReport = new Report(ReportType.ERROR, Stage.SEMANTIC, -1,
                    "Started semantic analysis but there are errors from previous stage");
            return new JmmSemanticsResult(parserResult, null, Arrays.asList(errorReport));
        }

        if (parserResult.getRootNode() == null) {
            var errorReport = new Report(ReportType.ERROR, Stage.SEMANTIC, -1,
                    "Started semantic analysis but AST root node is null");
            return new JmmSemanticsResult(parserResult, null, Arrays.asList(errorReport));
        }

        //DONE: criar estrutura com symbolTable e report que passamos ao visitor .
        Analysis analysis = new Analysis();

        // Visitor to fill the symbol table
        JmmNode node = parserResult.getRootNode();
        new SymbolTableVisitor().visit(node, analysis);
        new UndefinedVarVisitor().visit(node, analysis);
        new FuncNotFoundVisitor().visit(node, analysis);
        new BadArgumentsVisitor().visit(node, analysis);
        new ArrayVisitor().visit(node,analysis);
        new AssignmentVisitor().visit(node, analysis);

        new VerifyMathOperatorVisitor().visit(node, analysis);
        new VerifyBoolOperatorVisitor().visit(node, analysis);

        System.out.println("\nReports:");
        for(Report report: analysis.getReports()){
            System.out.println(report);
        }
/*
        System.out.println("Dump tree with Visitor where you control tree traversal");
        ExampleVisitor visitor = new ExampleVisitor("Identifier", "id");
        System.out.println(visitor.visit(node, ""));

        System.out.println("Dump tree with Visitor that automatically performs preorder tree traversal");
        var preOrderVisitor = new ExamplePreorderVisitor("Identifier", "id");
        System.out.println(preOrderVisitor.visit(node, ""));
        System.out.println(
                "Create histogram of node kinds with Visitor that automatically performs postorder tree traversal");
        var postOrderVisitor = new ExamplePostorderVisitor();
        var kindCount = new HashMap<String, Integer>();
        postOrderVisitor.visit(node, kindCount);
        System.out.println("Kinds count: " + kindCount + "\n");

        System.out.println(
                "Print variables name and line, and their corresponding parent with Visitor that automatically performs preorder tree traversal");
        var varPrinter = new ExamplePrintVariables("Variable", "name", "line");
        varPrinter.visit(node, null);*/

        // No Symbol Table being calculated yet
        return new JmmSemanticsResult(parserResult, analysis.getSymbolTable(), analysis.getReports());

    }

}