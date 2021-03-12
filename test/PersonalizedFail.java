import org.junit.Test;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.specs.util.SpecsIo;

import static org.junit.Assert.assertEquals;

public class PersonalizedFail {
    @Test
    public void WhileRecovery(){
        String jmmCode = SpecsIo.getResource("fixtures/personalized/WhileRecovery");
        assertEquals("Program", TestUtils.parse(jmmCode).getRootNode().getKind());
    }

}


