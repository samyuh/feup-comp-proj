package personalizedTests;

import org.junit.Test;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.specs.util.SpecsIo;

public class PersonalizedAcceptAnalysis {


    @Test
    public void booleanReturn(){
        String jmmCode = SpecsIo.getResource("fixtures/personalized/acceptSemantic/VerifyMathOperator.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        var analysisResult = TestUtils.analyse(jmmParser);
        TestUtils.noErrors(analysisResult.getReports());
    }

    @Test
    public void whileIf(){
        String jmmCode = SpecsIo.getResource("fixtures/personalized/acceptSemantic/WhileIfCondition.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        var analysisResult = TestUtils.analyse(jmmParser);
        TestUtils.noErrors(analysisResult.getReports());
    }
}
