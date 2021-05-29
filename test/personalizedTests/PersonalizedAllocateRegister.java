package personalizedTests;

import RegisterAllocation.AllocateRegister;
import org.junit.Test;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.specs.util.SpecsIo;

public class PersonalizedAllocateRegister {
    @Test
    public void Simple(){
        var jmmCode = SpecsIo.getResource("fixtures/public/Simple.jmm");
        var ollirCode = TestUtils.optimize(jmmCode);
        new AllocateRegister(ollirCode).allocateRegistersClass();
    }
}
