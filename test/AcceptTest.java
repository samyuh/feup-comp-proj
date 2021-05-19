
import org.junit.Test;

import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.specs.util.SpecsIo;

public class AcceptTest {
    // Hello World, Simple
    @Test
    public void HelloWorld() {
        String jmmCode = SpecsIo.getResource("fixtures/public/HelloWorld.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.noErrors(jmmParser.getReports());
        System.out.println(jmmParser.getRootNode().toJson());
    }

    @Test
    public void Simple() {
        String jmmCode = SpecsIo.getResource("fixtures/public/Simple.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.noErrors(jmmParser.getReports());
        System.out.println(jmmParser.getRootNode().toJson());
    }

    @Test
    public void FindMaximum() {
        String jmmCode = SpecsIo.getResource("fixtures/public/FindMaximum.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.noErrors(jmmParser.getReports());
        System.out.println(jmmParser.getRootNode().toJson());


    }

    @Test
    public void Lazysort() {
        String jmmCode = SpecsIo.getResource("fixtures/public/Lazysort.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.noErrors(jmmParser.getReports());
        System.out.println(jmmParser.getRootNode().toJson());

    }

    @Test
    public void Life() {
        String jmmCode = SpecsIo.getResource("fixtures/public/Life.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.noErrors(jmmParser.getReports());
        System.out.println(jmmParser.getRootNode().toJson());


    }

    @Test
    public void MonteCarloPi() {
        String jmmCode = SpecsIo.getResource("fixtures/public/MonteCarloPi.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.noErrors(jmmParser.getReports());
        System.out.println(jmmParser.getRootNode().toJson());


    }

    @Test
    public void QuickSort() {
        String jmmCode = SpecsIo.getResource("fixtures/public/QuickSort.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.noErrors(jmmParser.getReports());
        System.out.println(jmmParser.getRootNode().toJson());;

    }

    @Test
    public void TicTacToe() {
        String jmmCode = SpecsIo.getResource("fixtures/public/TicTacToe.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.noErrors(jmmParser.getReports());
        System.out.println(jmmParser.getRootNode().toJson());

    }

    @Test
    public void WhileAndIF() {
        String jmmCode = SpecsIo.getResource("fixtures/public/WhileAndIF.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.noErrors(jmmParser.getReports());
        System.out.println(jmmParser.getRootNode().toJson());
    }



}
