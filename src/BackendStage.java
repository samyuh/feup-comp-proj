import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jasmin.BuildJasmin;
import org.specs.comp.ollir.ClassUnit;
import org.specs.comp.ollir.OllirErrorException;

import pt.up.fe.comp.jmm.jasmin.JasminBackend;
import pt.up.fe.comp.jmm.jasmin.JasminResult;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;
import pt.up.fe.specs.util.SpecsIo;
import registerAllocation.AllocateRegister;
import registerAllocation.OptimizeException;

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

public class BackendStage implements JasminBackend {
    private boolean optimizeR = false;
    public int maxRegisters = -1;
    public void setOptimizeR(boolean optimizeR) {
        this.optimizeR = optimizeR;
    }

    public void setMaxRegisters(int maxRegisters){
        this.maxRegisters = maxRegisters;
    }

    @Override
    public JasminResult toJasmin(OllirResult ollirResult) {
        ClassUnit ollirClass = ollirResult.getOllirClass();

        try {

            // Example of what you can do with the OLLIR class
            ollirClass.checkMethodLabels(); // check the use of labels in the OLLIR loaded
            ollirClass.buildCFGs(); // build the CFG of each method
            ollirClass.outputCFGs(); // output to .dot files the CFGs, one per method
            ollirClass.buildVarTables(); // build the table of variables for each method

            // More reports from this stage
            List<Report> reports = new ArrayList<>();
            if (optimizeR){
                new AllocateRegister(ollirResult, maxRegisters).allocateRegistersClass();
            }

            // Convert the OLLIR to a String containing the equivalent Jasmin code
            String jasminCode = new BuildJasmin(ollirClass).build(); // Convert node ...

            return new JasminResult(ollirResult, jasminCode, reports);

        } catch (OllirErrorException e) {
            return new JasminResult(ollirClass.getClassName(), null,
                    Arrays.asList(Report.newError(Stage.GENERATION, -1, -1, "Exception during Jasmin generation", e)));
        } catch(OptimizeException e){
            return new JasminResult(ollirClass.getClassName(), null,
                    Arrays.asList(new Report(ReportType.ERROR, Stage.OPTIMIZATION, -1, "Not possible to allocate registers.")));
        }

    }

}
