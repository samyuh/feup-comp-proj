import static org.junit.Assert.*;

import org.junit.Test;

import pt.up.fe.comp.TestUtils;
import pt.up.fe.specs.util.SpecsIo;

public class AcceptTest {
    // Hello World, Simple
    @Test
    public void HelloWorld() {
        String jmmCode = SpecsIo.getResource("fixtures/public/HelloWorld.jmm");
        assertEquals("Program", TestUtils.parse(jmmCode).getRootNode().getKind());
    }

    @Test
    public void Simple(){
        String jmmCode = SpecsIo.getResource("fixtures/public/Simple.jmm");
        assertEquals("Program", TestUtils.parse(jmmCode).getRootNode().getKind());
    }

    @Test
    public void FindMaximum(){
        String jmmCode = SpecsIo.getResource("fixtures/public/FindMaximum.jmm");
        assertEquals("Program", TestUtils.parse(jmmCode).getRootNode().getKind());
    }

    @Test
    public void Lazysort(){
        String jmmCode = SpecsIo.getResource("fixtures/public/Lazysort.jmm");
        assertEquals("Program", TestUtils.parse(jmmCode).getRootNode().getKind());
    }



}
