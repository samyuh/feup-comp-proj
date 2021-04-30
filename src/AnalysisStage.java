
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
        new WhileIfVisitorCondition().visit(node, analysis);

        System.out.println("\nReports:");
        for(Report report: analysis.getReports()){
            System.out.println(report);
        }

        return new JmmSemanticsResult(parserResult, analysis.getSymbolTable(), analysis.getReports());

    }

}