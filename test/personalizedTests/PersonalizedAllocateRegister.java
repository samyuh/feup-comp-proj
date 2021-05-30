package personalizedTests;

import registerAllocation.AllocateRegister;
import org.junit.Test;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.specs.util.SpecsIo;

public class PersonalizedAllocateRegister {
    @Test
    public void Simple(){
        var jmmCode = SpecsIo.getResource("fixtures/public/Simple.jmm");
        var ollirCode = TestUtils.optimize(jmmCode);
        new AllocateRegister(ollirCode, 4).allocateRegistersClass();
    }

    @Test
    public void dataflow(){
        var jmmCode = SpecsIo.getResource("fixtures/personalized/dataflow/test1.jmm");
        var ollirCode = TestUtils.optimize(jmmCode);
        new AllocateRegister(ollirCode, 4).allocateRegistersClass();
    }

    @Test
    public void putFieldUsed(){
        var jmmCode = SpecsIo.getResource("fixtures/personalized/dataflow/field.jmm");
        var ollirCode = TestUtils.optimize(jmmCode);
        new AllocateRegister(ollirCode, 4).allocateRegistersClass();
    }
}
