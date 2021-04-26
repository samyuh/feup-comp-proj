
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

import org.junit.Test;

import pt.up.fe.comp.TestUtils;
import pt.up.fe.specs.util.SpecsIo;

public class OptimizeTest {

    @Test
    public void testHelloWorld() {
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/public/HelloWorld.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void testSimple(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/public/Simple.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void testFindMaximum(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/public/FindMaximum.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void InvokeVirtualTest(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/personalized/ollir/OllirInvokeVirtual.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void FacTest(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/personalized/ollir/OllirFac.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void MyClass1(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/personalized/ollir/OllirClass1.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void MyClass2(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/personalized/ollir/OllirClass2.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void MyClass3(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/personalized/ollir/OllirClass3.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void MyClass4(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/personalized/ollir/OllirClass4.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void testMonteCarloPi(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/public/MonteCarloPi.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void testWhileAndIF(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/public/WhileAndIF.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void testArraysAndSimpleVars() {
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/personalized/ollir/OllirArraysAndSimpleVars.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void testRecursionInAssignments(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/personalized/ollir/OllirRecursiveAssignment.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void testDotMethods(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/personalized/ollir/OllirDotMethod.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void testDotMethods2(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/personalized/ollir/OllirDotMethod2.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void testIfElse(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/personalized/ollir/OllirIfElse.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void notTest(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/personalized/ollir/OllirNotTest.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void whileTest(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/personalized/ollir/OllirWhile.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void testLazysort(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/public/Lazysort.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void testLife(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/public/Life.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void testQuickSort(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/public/QuickSort.jmm"));
        TestUtils.noErrors(result.getReports());
    }

    @Test
    public void testTicTacToe(){
        var result = TestUtils.optimize(SpecsIo.getResource("fixtures/public/TicTacToe.jmm"));
        TestUtils.noErrors(result.getReports());
    }

}
