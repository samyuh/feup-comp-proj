package jasmin.methods;

import jasmin.*;
import jasmin.translation.*;
import org.specs.comp.ollir.*;

import java.util.HashMap;

public class BuildOperand extends JasminMethod {

    Element lhs;
    HashMap<String, Descriptor> table;

    public BuildOperand(ClassUnit ollir, HashMap<String, Descriptor> table, Element lhs) {
        super(ollir);
        this.table = table;
        this.lhs = lhs;

    }

    public String getOperand(Instruction inst) {

        switch (inst.getInstType()) {
            case BINARYOPER -> addBinaryOper((BinaryOpInstruction) inst);
            case NOPER -> addNoOper((SingleOpInstruction) inst);
            case GETFIELD -> addGetField((GetFieldInstruction) inst);
            case CALL -> addCall((CallInstruction) inst);
        }

        return super.toString();
    }

    public void addBinaryOper(BinaryOpInstruction inst) {
        Element leftElem = inst.getLeftOperand();
        Element rightElem = inst.getRightOperand();
        // a = b[i];

        String leftInstruction = TranslateLoadStore.getLoadInst(leftElem, table);
        String rightInstruction = TranslateLoadStore.getLoadInst(rightElem, table);
        OperationType opType = inst.getUnaryOperation().getOpType();

        if (!UtilsJasmin.isBooleanOp(opType)) {
            methodString.append(leftInstruction);
            methodString.append(rightInstruction);

            methodString.append(InstSingleton.getOp(opType));
        }else
            methodString.append(TranslateBooleanOp.getJasminInst(leftInstruction, rightInstruction, opType));
    }

    public void addNoOper(SingleOpInstruction inst) {
        Element element = inst.getSingleOperand();
        methodString.append(TranslateLoadStore.getLoadInst(element, table));
    }

    public void addCall(CallInstruction callInstruction){
        methodString.append(TranslateCall.getJasminInst(callInstruction, table));
    }

    public void addGetField(GetFieldInstruction inst){
        methodString.append(TranslateGetField.getJasminInst(inst, table));
    }

}
