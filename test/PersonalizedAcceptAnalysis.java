import org.junit.Test;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.specs.util.SpecsIo;

public class PersonalizedAcceptAnalysis {


    @Test
    public void booleanReturn(){
        String jmmCode = SpecsIo.getResource("fixtures/personalized/acceptSemantic/VerifyMathOperatorVisitor.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        var analysisResult = TestUtils.analyse(jmmParser);
        //System.out.println(jmmParser.toJson());
        TestUtils.noErrors(analysisResult.getReports());
        System.out.println(jmmParser.getRootNode().toJson());
    }
}
