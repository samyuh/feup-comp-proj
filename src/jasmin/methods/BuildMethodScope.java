package jasmin.methods;

import jasmin.UtilsJasmin;
import jasmin.translation.TranslateType;
import org.specs.comp.ollir.ClassUnit;
import org.specs.comp.ollir.Element;
import org.specs.comp.ollir.Method;
import org.specs.comp.ollir.Type;

import java.util.ArrayList;

public class BuildMethodScope extends JasminMethod{

    Method method;
    public BuildMethodScope(ClassUnit ollir, Method method) {
        super(ollir);
        this.method = method;
    }

    public String getScope() {
        methodString.append(".method ");

        //<access-modifiers>
        String accessModifiers = this.getAccessModifiers(method.getMethodAccessModifier(), method);
        methodString.append(accessModifiers);

        if (method.isStaticMethod())
            methodString.append(" static");
        if (method.isFinalMethod())
            methodString.append(" final");
        methodString.append(" ");


        if (method.isConstructMethod())
            addScopeConstructor();
        else
            addScopeMethod();

        addEndLine();

        return methodString.toString();

    }

    public void addScopeMethod() {
        methodString.append(method.getMethodName());
        methodString.append(UtilsJasmin.getArguments(method.getParams()));
        methodString.append(TranslateType.getJasminType(method.getReturnType()));
        addEndLine();
        methodString.append(".limit stack 99");
        addEndLine();
        methodString.append(".limit locals ").append(getLocalLimit());
        addEndLine();
    }

    /**
     * The local limit is this + arguments + local variables.
     */
    public int getLocalLimit(){
        // Elements in the varTable + this.
        var varTable = method.getVarTable();
        return varTable.size() + 1;
    }

    public void addScopeConstructor() {
        methodString.append("<init>()V");
    }

}
