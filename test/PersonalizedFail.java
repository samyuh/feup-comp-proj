import org.junit.Test;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.specs.util.SpecsIo;

import static org.junit.Assert.assertEquals;

public class PersonalizedFail {
    @Test
    public void WhileRecovery(){
        String jmmCode = SpecsIo.getResource("fixtures/personalized/syntactic/WhileRecovery");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        TestUtils.mustFail(jmmParser.getReports());
        System.out.println(jmmParser.toJson());
    }

}


