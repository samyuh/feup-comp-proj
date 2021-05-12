package personalizedTests;

import org.junit.Test;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.comp.jmm.ollir.OllirUtils;
import pt.up.fe.specs.util.SpecsIo;

import java.util.ArrayList;

public class PersonalizedJasmin {

    @Test
    public void ArraysAndSimpleVar() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir_to_convert/ArraysAndSimpleVars.ollir"))
                , null, new ArrayList<>()));
    }

    // OK
    @Test
    public void DotMethod() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir_to_convert/DotMethod.ollir"))
                , null, new ArrayList<>()));

    }


    @Test
    public void DotMethod2() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir_to_convert/DotMethod2.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void Fac() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir_to_convert/Fac.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void FindMaximum() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir_to_convert/FindMaximum.ollir"))
                , null, new ArrayList<>()));

    }


    @Test
    public void IfElse() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir_to_convert/IfElse.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void InvokeVirtualTest() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir_to_convert/InvokeVirtualTest.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void MonteCarloPi() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir_to_convert/MonteCarloPi.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void MyClass() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir_to_convert/MyClass.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void MyClass2() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir_to_convert/MyClass2.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void MyClass3() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir_to_convert/MyClass3.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void MyClass4() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir_to_convert/MyClass4.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void NotTest() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir_to_convert/NotTest.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void RecursionInAssigments() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir_to_convert/RecursionInAssigments.ollir"))
                , null, new ArrayList<>()));

    }


    @Test
    public void While() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir_to_convert/While.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void WhileAndIf() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir_to_convert/WhileAndIf.ollir"))
                , null, new ArrayList<>()));

    }
}
