import org.junit.Test;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.comp.jmm.ollir.OllirUtils;
import pt.up.fe.specs.util.SpecsIo;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FailTest {

    @Test
    public void BlowUp(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/syntactical/BlowUp.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.mustFail(jmmParser.getReports());
        System.out.println(jmmParser.toJson());
    }


    @Test
    public void CompleteWhileTest(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/syntactical/CompleteWhileTest.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.mustFail(jmmParser.getReports());
        System.out.println(jmmParser.toJson());
    }


    @Test
    public void LenghtError(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/syntactical/LengthError.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.mustFail(jmmParser.getReports());
        System.out.println(jmmParser.toJson());
    }

    @Test
    public void MissingRightPar(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/syntactical/MissingRightPar.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.mustFail(jmmParser.getReports());
        System.out.println(jmmParser.toJson());
    }

    @Test
    public void MultipleSequential(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/syntactical/MultipleSequential.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.mustFail(jmmParser.getReports());
        System.out.println(jmmParser.toJson());
    }

    @Test
    public void NestedLoop(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/syntactical/NestedLoop.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.mustFail(jmmParser.getReports());
        System.out.println(jmmParser.toJson());
    }


}
