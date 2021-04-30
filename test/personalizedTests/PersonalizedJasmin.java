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

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/jasmin/ArraysAndSimpleVars.ollir"))
                , null, new ArrayList<>()));

    }

    // OK
    @Test
    public void DotMethod() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/jasmin/DotMethod.ollir"))
                , null, new ArrayList<>()));

    }


    @Test
    public void DotMethod2() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/jasmin/DotMethod2.ollir"))
                , null, new ArrayList<>()));

    }


    @Test
    public void FindMaximum() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/jasmin/FindMaximum.ollir"))
                , null, new ArrayList<>()));

    }


    @Test
    public void IfElse() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/jasmin/IfElse.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void InvokeVirtualTest() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/jasmin/InvokeVirtualTest.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void MonteCarloPi() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/jasmin/MonteCarloPi.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void MyClass() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/jasmin/MyClass.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void MyClass2() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/jasmin/MyClass2.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void MyClass3() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/jasmin/MyClass3.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void MyClass4() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/jasmin/MyClass4.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void NotTest() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/jasmin/NotTest.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void RecursionInAssigments() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/jasmin/RecursionInAssigments.ollir"))
                , null, new ArrayList<>()));

    }


    @Test
    public void While() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/jasmin/While.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void WhileAndIf() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/jasmin/WhileAndIf.ollir"))
                , null, new ArrayList<>()));

    }
}
