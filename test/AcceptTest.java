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

    @Test
    public void Life(){
        String jmmCode = SpecsIo.getResource("fixtures/public/Life.jmm");
        assertEquals("Program", TestUtils.parse(jmmCode).getRootNode().getKind());
    }

    @Test
    public void MonteCarloPi(){
        String jmmCode = SpecsIo.getResource("fixtures/public/MonteCarloPi.jmm");
        assertEquals("Program", TestUtils.parse(jmmCode).getRootNode().getKind());
    }

    @Test
    public void QuickSort(){
        String jmmCode = SpecsIo.getResource("fixtures/public/QuickSort.jmm");
        assertEquals("Program", TestUtils.parse(jmmCode).getRootNode().getKind());
    }

    @Test
    public void TicTacToe(){
        String jmmCode = SpecsIo.getResource("fixtures/public/TicTacToe.jmm");
        assertEquals("Program", TestUtils.parse(jmmCode).getRootNode().getKind());
    }

    @Test
    public void WhileAndIF(){
        String jmmCode = SpecsIo.getResource("fixtures/public/WhileAndIF.jmm");
        assertEquals("Program", TestUtils.parse(jmmCode).getRootNode().getKind());
    }
}
