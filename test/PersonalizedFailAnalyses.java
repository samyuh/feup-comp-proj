import org.junit.Test;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.analysis.table.SymbolTable;
import pt.up.fe.specs.util.SpecsIo;

public class PersonalizedFailAnalyses {

    @Test
    public void symbolTableTest() {
        String jmmCode = SpecsIo.getResource("fixtures/personalized/SymbolTable.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        var analysisResult = TestUtils.analyse(jmmParser);
        TestUtils.noErrors(analysisResult.getReports());
        //System.out.println(jmmParser.getRootNode().toJson());

        SymbolTable symbolTable = analysisResult.getSymbolTable();

        System.out.println("Class name: " + symbolTable.getClassName());
        System.out.println("Extends name: " + symbolTable.getSuper());
        System.out.println("Imports: " + symbolTable.getImports());
        System.out.println("Methods: " + symbolTable.getMethods());

        System.out.println();

        System.out.println("RETURN TYPE [main]: " + symbolTable.getReturnType("main").getName());
        System.out.println("RETURN IS ARRAY [main]:: " + symbolTable.getReturnType("main").isArray());

        System.out.println();

        System.out.println("PARAMETER 1 TYPE [main]:: " + symbolTable.getParameters("main").get(0).getType().getName());
        System.out.println("PARAMETER 1 IS ARRAY [main]:: " + symbolTable.getParameters("main").get(0).getType().isArray());

        System.out.println();

        System.out.println("RETURN TYPE [methodFoo] :: " + symbolTable.getReturnType("methodFoo").getName());
        System.out.println("RETURN IS ARRAY [methodFoo]:: " + symbolTable.getReturnType("methodFoo").isArray());

        System.out.println("\n >>>> Parameters");
        for (int i = 0; i < symbolTable.getParameters("methodFoo").size(); i++) {
            System.out.println("VAR " + (i + 1) + " NAME [methodFoo]:: " + symbolTable.getParameters("methodFoo").get(i).getName());
            System.out.println("PARAMETER " + (i + 1) + " TYPE [methodFoo]:: " + symbolTable.getParameters("methodFoo").get(i).getType().getName());
            System.out.println("PARAMETER " + (i + 1) + " IS ARRAY [methodFoo]:: " + symbolTable.getParameters("methodFoo").get(i).getType().isArray());
        }

        System.out.println("\n >>>> Local Variables");
        for (int i = 0; i < symbolTable.getLocalVariables("methodFoo").size(); i++) {
            System.out.println("VAR " + (i + 1) + " NAME [methodFoo]:: " + symbolTable.getLocalVariables("methodFoo").get(i).getName());
            System.out.println("VAR " + (i + 1) + " TYPE [methodFoo]:: " + symbolTable.getLocalVariables("methodFoo").get(i).getType().getName());
            System.out.println("VAR " + (i + 1) + " IS ARRAY [methodFoo]:: " + symbolTable.getLocalVariables("methodFoo").get(i).getType().isArray());
        }

        System.out.println("\n >>>> Fields");
        for (int i = 0; i < symbolTable.getFields().size(); i++) {
            System.out.println("VAR " + (i + 1) + " NAME:: " + symbolTable.getFields().get(i).getName());
            System.out.println("VAR " + (i + 1) + " TYPE:: " + symbolTable.getFields().get(i).getType().getName());
            System.out.println("VAR " + (i + 1) + " IS ARRAY:: " + symbolTable.getFields().get(i).getType().isArray());
        }
    }
}
