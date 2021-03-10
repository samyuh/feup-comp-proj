import static org.junit.Assert.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Properties;
import java.io.StringReader;

import pt.up.fe.comp.TestUtils;
import pt.up.fe.specs.util.SpecsIo;

public class ExampleTest {
    @Test
    public void testExpression() {
        String jmmCode = SpecsIo.getResource("fixtures/public/test.jmm");
        assertEquals("Goal", TestUtils.parse(jmmCode).getRootNode().getKind());
    }

}
