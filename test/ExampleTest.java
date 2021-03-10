import static org.junit.Assert.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Properties;
import java.io.StringReader;

import pt.up.fe.comp.TestUtils;
import pt.up.fe.specs.util.SpecsIo;

public class ExampleTest {
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

}
