package registerAllocation.dataflow;

import org.specs.comp.ollir.Instruction;

/**
 * Given an instruction, this class is responsible for
 * returning the used variables for this instruction.
 */
public class UsedVariables {

    Instruction instruction;

    public UsedVariables(Instruction instruction){
        this.instruction = instruction;
    }

    public String[] getUsed(){
        switch(instruction.getInstType()){
            case CALL:
                return getUsedCall();
            case PUTFIELD:
                return getUsedPutField();
            case UNARYOPER:
                return getUsedUnaryOperator();
            case BINARYOPER:
                return getUsedBinaryOperator();
            case NOPER:
                return getUsedNoper();
        }
        return new String[]{};
    }

    public String[] getUsedCall(){

    }

    public String[] getUsedPutField(){

    }

    public String[] getUsedUnaryOperator(){

    }

    public String[] getUsedBinaryOperator(){

    }

    public String[] getUsedNoper(){

    }
}
