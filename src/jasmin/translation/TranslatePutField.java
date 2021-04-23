package jasmin.translation;

import org.specs.comp.ollir.Descriptor;
import org.specs.comp.ollir.PutFieldInstruction;

import java.util.HashMap;

public class TranslatePutField {

    public static String getJasminInst(PutFieldInstruction putFieldInstruction, HashMap<String, Descriptor> table){
        System.out.println("First operand " + putFieldInstruction.getFirstOperand());
        System.out.println("Second operand " + putFieldInstruction.getSecondOperand());
        System.out.println("Third operand " + putFieldInstruction.getThirdOperand());
        return "bla";
    }
}
