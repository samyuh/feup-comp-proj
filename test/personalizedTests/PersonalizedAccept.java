package personalizedTests;

import org.junit.Test;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.specs.util.SpecsIo;

public class PersonalizedAccept {

    @Test
    public void NotPersonalized(){
        String jmmCode = SpecsIo.getResource("fixtures/personalized/syntactic/NotTest.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.noErrors(jmmParser.getReports());
        System.out.println(jmmParser.getRootNode().toJson());
    }

    @Test
    public void SemanticTest(){
        String jmmCode = SpecsIo.getResource("fixtures/personalized/failSemantic/SemanticCorrections.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        var analysisResult = TestUtils.analyse(jmmParser);
        //System.out.println(jmmParser.getRootNode().toJson());
        TestUtils.noErrors(analysisResult.getReports());
    }
}
