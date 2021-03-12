import org.junit.Test;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.specs.util.SpecsIo;

import static org.junit.Assert.assertEquals;

public class PersonalizedAccept {
    @Test
    public void TestConflict1(){

        String jmmCode = SpecsIo.getResource("fixtures/personalized/test1");
        assertEquals("Program", TestUtils.parse(jmmCode).getRootNode().getKind());
    }


}
