import org.junit.Test;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.specs.util.SpecsIo;

import static org.junit.Assert.assertEquals;

public class PersonalizedAccept {

    @Test
    public void TestConflict1(){

        String jmmCode = SpecsIo.getResource("fixtures/personalized/test1");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.noErrors(jmmParser.getReports());
        System.out.println(jmmParser.getRootNode().toJson());
    }

    @Test
    public void DotPersonalized(){
        String jmmCode = SpecsIo.getResource("fixtures/personalized/DotPrecendence.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.noErrors(jmmParser.getReports());
        System.out.println(jmmParser.getRootNode().toJson());
    }

    @Test
    public void NotPersonalized(){
        String jmmCode = SpecsIo.getResource("fixtures/personalized/NotTest.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.noErrors(jmmParser.getReports());
        System.out.println(jmmParser.getRootNode().toJson());
    }
}
