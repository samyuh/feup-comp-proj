package personalizedTests;


import org.junit.Test;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.jasmin.JasminResult;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.specs.util.SpecsIo;

import java.util.ArrayList;
import java.util.List;


/**
 * These are tests to check if the Jasmin some parts of code correctly.
 */
public class PersonalizedJasmin {
    /**
     * This function gets the path to the jasmin code and run it.
     * Used for debugging reasons.
     * @param path Path to the jasmin code.
     */
    public void runJasmin(String path){
        List<Report> reportList = new ArrayList<>();
        var jasminCode = SpecsIo.getResource(path);
        JasminResult jasminResult = new JasminResult("ArrayTests", jasminCode, reportList);
        jasminResult.run();
    }

    public void runOllirJasmin(String path){
        var result = TestUtils.backend(SpecsIo.getResource(path));
        TestUtils.noErrors(result.getReports());
        result.run();
    }

    @Test
    public void ArrayTests() {
        var result = TestUtils.backend(SpecsIo.getResource("fixtures/personalized/running_accept/array_tests/ArrayTests.jmm"));
        TestUtils.noErrors(result.getReports());
        result.run();
    }

    @Test
    public void IfCondition(){
        var result = TestUtils.backend(SpecsIo.getResource("fixtures/personalized/running_accept/if_tests/ifTests.jmm"));
        TestUtils.noErrors(result.getReports());
        result.run();
    }

    @Test
    public void WhileTests(){
        var result = TestUtils.backend(SpecsIo.getResource("fixtures/personalized/running_accept/while_tests/WhileTests.jmm"));
        TestUtils.noErrors(result.getReports());
        result.run();
    }

    @Test
    public void FunctionsTests(){
        var result = TestUtils.backend(SpecsIo.getResource("fixtures/personalized/running_accept/functions_tests/FunctionsTests.jmm"));
        TestUtils.noErrors(result.getReports());
        result.run();
    }

    @Test
    public void FunctionTestsJasmin(){
        runJasmin("fixtures/personalized/running_accept/functions_tests/FunctionTests.j");
    }

    @Test
    public void FindMaximum(){
        runOllirJasmin("fixtures/personalized/running_accept/debugFindMaximum/FindMaximum.jmm");
    }

    @Test
    public void FindMaximumJasmin(){
        runJasmin("fixtures/personalized/running_accept/debugFindMaximum/FindMaximum.j");
    }



}
