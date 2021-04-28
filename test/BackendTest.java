
/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.comp.jmm.ollir.OllirUtils;
import pt.up.fe.specs.util.SpecsIo;

import java.util.ArrayList;

public class BackendTest {

    @Test
    public void testHelloWorld() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/public/ollir/myclass4.ollir"))
                , null, new ArrayList<>()));
        /*
        var result = TestUtils.backend(SpecsIo.getResource("fixtures/public/HelloWorld.jmm"));
        TestUtils.noErrors(result.getReports());

        var output = result.run();
        assertEquals("Hello, World!", output.trim());

         */
    }
    // Freaking error
    @Test
    public void ArraysAndSimpleVar() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir/ArraysAndSimpleVars.ollir"))
                , null, new ArrayList<>()));

    }
    // Freaking error
    @Test
    public void DotMethod() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir/DotMethod.ollir"))
                , null, new ArrayList<>()));

    }


    @Test
    public void DotMethod2() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir/DotMethod2.ollir"))
                , null, new ArrayList<>()));

    }


    // Freaking error
    @Test
    public void FindMaximum() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir/FindMaximum.ollir"))
                , null, new ArrayList<>()));

    }

    // OK.
    @Test
    public void HelloWorld() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir/HelloWorld.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void IfElse() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir/IfElse.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void InvokeVirtualTest() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir/InvokeVirtualTest.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void MonteCarloPi() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir/MonteCarloPi.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void MyClass() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir/MyClass.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void MyClass2() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir/MyClass2.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void MyClass3() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir/MyClass3.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void MyClass4() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir/MyClass4.ollir"))
                , null, new ArrayList<>()));

    }

    // Seems ok... Needs to check the registers.
    @Test
    public void NotTest() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir/NotTest.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void RecursionInAssigments() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir/RecursionInAssigments.ollir"))
                , null, new ArrayList<>()));

    }
    // Ok
    @Test
    public void Simple() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir/Simple.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void While() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir/While.ollir"))
                , null, new ArrayList<>()));

    }

    @Test
    public void WhileAndIf() {

        TestUtils.backend(new OllirResult(OllirUtils.parse(SpecsIo.getResource("fixtures/personalized/ollir/WhileAndIf.ollir"))
                , null, new ArrayList<>()));

    }
}
