
import org.junit.Test;

import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.JmmParserResult;
import pt.up.fe.specs.util.SpecsIo;

public class FailSemanticTests {

    @Test
    public void arr_index_not_int(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/semantic/arr_index_not_int.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        var analysisResult = TestUtils.analyse(jmmParser);
        TestUtils.noErrors(analysisResult.getReports());
        //System.out.println(jmmParser.getRootNode().toJson());
    }

    @Test
    public void arr_size_not_int(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/semantic/arr_size_not_int.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        var analysisResult = TestUtils.analyse(jmmParser);
        TestUtils.noErrors(analysisResult.getReports());
        System.out.println(jmmParser.getRootNode().toJson());
    }
    @Test
    public void badArguments(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/semantic/badArguments.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        var analysisResult = TestUtils.analyse(jmmParser);
        TestUtils.noErrors(analysisResult.getReports());
        System.out.println(jmmParser.getRootNode().toJson());
    }

    @Test
    public void binop_incomp(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/semantic/binop_incomp.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        var analysisResult = TestUtils.analyse(jmmParser);
        TestUtils.noErrors(analysisResult.getReports());
    }

    @Test
    public void funcNotFound(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/semantic/funcNotFound.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        var analysisResult = TestUtils.analyse(jmmParser);
        TestUtils.noErrors(analysisResult.getReports());
    }

    @Test
    public void simple_length(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/semantic/simple_length.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        var analysisResult = TestUtils.analyse(jmmParser);

        TestUtils.noErrors(analysisResult.getReports());
        System.out.println(jmmParser.getRootNode().toJson());
    }

    @Test
    public void var_exp_incomp(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/semantic/var_exp_incomp.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        var analysisResult = TestUtils.analyse(jmmParser);
        TestUtils.noErrors(analysisResult.getReports());
        System.out.println(jmmParser.getRootNode().toJson());
    }

    @Test
    public void var_lit_incomp(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/semantic/var_lit_incomp.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        var analysisResult = TestUtils.analyse(jmmParser);
        TestUtils.noErrors(analysisResult.getReports());
        System.out.println(jmmParser.getRootNode().toJson());
    }

    @Test
    public void var_undef(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/semantic/var_undef.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        var analysisResult = TestUtils.analyse(jmmParser);
        TestUtils.noErrors(analysisResult.getReports());
        System.out.println(jmmParser.getRootNode().toJson());
    }

    @Test
    public void varNotInit(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/semantic/varNotInit.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        var analysisResult = TestUtils.analyse(jmmParser);
        TestUtils.noErrors(analysisResult.getReports());
        System.out.println(jmmParser.getRootNode().toJson());
    }

    // Extra
    @Test
    public void missType(){
        String jmmCode = SpecsIo.getResource("fixtures/public/fail/semantic/extra/miss_type.jmm");
        JmmParserResult jmmParser = TestUtils.parse(jmmCode);
        var analysisResult = TestUtils.analyse(jmmParser);
        TestUtils.noErrors(analysisResult.getReports());
        System.out.println(jmmParser.getRootNode().toJson());
    }

}
