package analysis;

import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.SymbolTable;
import pt.up.fe.comp.jmm.analysis.table.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MySymbolTable implements SymbolTable {

    String className;
    String extendSuper;
    List<String> imports;
    List<Symbol> fields;
    HashMap<String, List<Symbol>> methodParameters; // contains parameters
    HashMap<String, List<Symbol>> methodLocalVariables;
    HashMap<String, Type> methodTypes;

    public MySymbolTable() {
        imports =  new ArrayList<>();
        fields = new ArrayList<>();
        methodParameters = new HashMap<>();
        methodLocalVariables = new HashMap<>();
        methodTypes = new HashMap<>();
    }

    /**
     * @return a list of fully qualified names of imports
     */
    public List<String> getImports(){
        return imports;
    }

    /**
     * @return the name of the main class
     */
    public String getClassName(){
        return className;
    }

    /**
     *
     * @return the name that the class extends, or null if the class does not extend another class
     */
    public String getSuper(){
        return extendSuper;
    }

    /**
     *
     * @return a list of Symbols that represent the fields of the class
     */
    public List<Symbol> getFields(){
        return fields;
    }

    /**
     *
     * @return a list with the names of the methods of the class
     */
    public List<String> getMethods(){
        return new ArrayList<>(methodParameters.keySet());
    }

    /**
     *
     * @return the return type of the given method
     */
    public Type getReturnType(String methodName){
        return methodTypes.get(methodName);
    }

    /**
     *
     * @param methodName - name of the method that contains these parameters
     * @return a list of parameters of the given method
     */
    public List<Symbol> getParameters(String methodName){
        return methodParameters.get(methodName);
    }

    /**
     *
     * @param methodName - name of the method of the scope of the local variable
     * @return a list of local variables declared in the given method
     */
    public List<Symbol> getLocalVariables(String methodName){
        return  methodLocalVariables.get(methodName);
    }


    public void setClassName(String className) {
        this.className = className;
    }

    public void setExtendSuper(String extendSuper) {
        this.extendSuper = extendSuper;
    }

    public void addImport(String importName) {
        imports.add(importName);
    }

    public void addField(Symbol field) {
        fields.add(field);
    }

    public void addMethod(String methodName, List<Symbol> params) {
        methodParameters.put(methodName, params);
    }

    public void addLocalVariables(String methodName, List<Symbol> variables) {
        methodLocalVariables.put(methodName, variables);
    }

    public void addMethodType(String methodName, Type returnType) {
        methodTypes.put(methodName, returnType);
    }


}
