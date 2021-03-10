import static org.junit.Assert.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Properties;
import java.io.StringReader;

import pt.up.fe.comp.TestUtils;

public class ExampleTest {
    @Test
    public void testExpression() {
        assertEquals("Goal", TestUtils.parse("test/bla").getRootNode().getKind());
    }

}
