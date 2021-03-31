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
    List<String> methods;
    List<Symbol> fields;
    HashMap<String, List<Symbol>> parameters;
    HashMap<String, List<Symbol>> localVariables;
    HashMap<String, Type> methodTypes;

    public MySymbolTable() {
        imports =  new ArrayList<>();
        methods = new ArrayList<>();
        fields = new ArrayList<>();
        parameters = new HashMap<>();
        localVariables = new HashMap<>();
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
        return methods;
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
        return parameters.get(methodName);
    }

    /**
     *
     * @param methodName - name of the method of the scope of the local variable
     * @return a list of local variables declared in the given method
     */
    public List<Symbol> getLocalVariables(String methodName){
        return localVariables.get(methodName);
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

    public void addMethod(String methodName){
        methods.add(methodName);
    }

    public void addParameters(String methodName, Symbol param) {
        parameters.get(methodName).add(param);
    }

    public void addLocalVariables(String methodName, Symbol variable) {
        parameters.get(methodName).add(variable);
    }

    public void addMethodTypes(String methodName, Type type) {
        methodTypes.put(methodName,type);
    }
}
