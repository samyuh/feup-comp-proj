import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.analysis.table.SymbolTable;
import pt.up.fe.comp.jmm.ollir.JmmOptimization;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import visitor.ConstantPropagationVisitor;

import java.util.HashMap;
import java.util.List;

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

public class OptimizationStage implements JmmOptimization {
    /*
     * Without Optimization - Obsolete method
     */
    @Override
    public OllirResult toOllir(JmmSemanticsResult semanticsResult) {
        return toOllir(semanticsResult, false);
    }

    /*
     * With Optimization - Checkpoint 3
     */
    @Override
    public OllirResult toOllir(JmmSemanticsResult semanticsResult, boolean optimize) {
        JmmNode node = semanticsResult.getRootNode();

        // Convert the AST to a String containing the equivalent OLLIR code
        OllirEmitter ollirEmitter = new OllirEmitter(semanticsResult.getSymbolTable(), optimize);
        String ollirCode = ollirEmitter.visit(node); // Convert node ...

        return new OllirResult(semanticsResult, ollirCode, ollirEmitter.getReports());
    }

    // Before toOllir
    @Override
    public JmmSemanticsResult optimize(JmmSemanticsResult semanticsResult) {
        List<JmmNode> classNodeChildren = semanticsResult.getRootNode().getChildren().get(1).getChildren();
        List<JmmNode> methods = classNodeChildren.subList(2, classNodeChildren.size());
        ConstantPropagationVisitor constantVisitor = new ConstantPropagationVisitor();
        HashMap<String, String> constants = new HashMap<>();


        do {
            constantVisitor.resetCounter();

            for (JmmNode methodNode : methods) {
                JmmNode methodType = methodNode.getChildren().get(0);
                JmmNode methodBody;
                if (methodType.getKind().equals("MethodMain")) {
                    methodBody = methodType.getChildren().get(1);
                } else {
                    methodBody = methodType.getChildren().get(3);
                }

                // Loop through method body
                constantVisitor.visit(methodBody, constants);

            }
        } while (constantVisitor.getCounter() != 0);

        //System.out.println(semanticsResult.getRootNode().toJson());
        return semanticsResult;
    }

    // Coloração de grafos.
    // After toOllir - apply the algorithm to check the min number of register.
    // Which register each variable must have.
    @Override
    public OllirResult optimize(OllirResult ollirResult) {
        // THIS IS JUST FOR CHECKPOINT 3
        return ollirResult;
    }





}
