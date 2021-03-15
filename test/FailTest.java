import org.junit.Test;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.specs.util.SpecsIo;

import static org.junit.Assert.assertEquals;

// TODO: for the while loop tests generated error recovery
public class FailTest {

    @Test
    public void BlowUp(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/syntactical/BlowUp.jmm");
        assertEquals("Program", TestUtils.parse(jmmCode).getRootNode().getKind());
    }

    // TODO: is it to fail in the part that it fails?
    @Test
    public void CompleteWhileTest(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/syntactical/CompleteWhileTest.jmm");
        assertEquals("Program", TestUtils.parse(jmmCode).getRootNode().getKind());
    }

    // TODO: is it to fail in the part that it fails?
    @Test
    public void LenghtError(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/syntactical/LengthError.jmm");
        assertEquals("Program", TestUtils.parse(jmmCode).getRootNode().getKind());
    }

    @Test
    public void MissingRightPar(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/syntactical/MissingRightPar.jmm");
        assertEquals("Program", TestUtils.parse(jmmCode).getRootNode().getKind());
    }

    @Test
    public void MultipleSequential(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/syntactical/MultipleSequential.jmm");
        assertEquals("Program", TestUtils.parse(jmmCode).getRootNode().getKind());
    }

    @Test
    public void NestedLoop(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/syntactical/NestedLoop.jmm");
        assertEquals("Program", TestUtils.parse(jmmCode).getRootNode().getKind());
    }


}
