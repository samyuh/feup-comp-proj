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
public class PersonalizedRunning {

    public void runJasmin(String path){
        List<Report> reportList = new ArrayList<>();
        var jasminCode = SpecsIo.getResource(path);
        JasminResult jasminResult = new JasminResult("ArrayTests", jasminCode, reportList);
        jasminResult.run();
    }

    @Test
    public void arrayTests() {
        runJasmin("fixtures/personalized/running_accept/array_tests/ArrayTests.j");
    }

    @Test
    public void ArrayTests() {
        var result = TestUtils.backend(SpecsIo.getResource("fixtures/personalized/running_accept/array_tests/ArrayTests.jmm"));
        TestUtils.noErrors(result.getReports());
        result.run();
    }
}
