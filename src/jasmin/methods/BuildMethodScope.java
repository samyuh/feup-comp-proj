package jasmin.methods;

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
            methodString.append("static");
        if (method.isFinalMethod())
            methodString.append("final");
        methodString.append(" ");


        if (method.isConstructMethod())
            addScopeConstructor();
        else
            addScopeMethod();

        return methodString.toString();

    }

    public void addScopeMethod() {
        methodString.append(method.getMethodName());
        addParameters();
        methodString.append(TranslateType.getJasminType(method.getReturnType()));
        addEndLine();
        methodString.append(".limit stack 99");
        addEndLine();
        methodString.append(".limit locals 99");
        addEndLine();
    }

    // TODO: only the default constructor?
    public void addScopeConstructor() {
        methodString.append("<init>()V");
        addEndLine();
        methodString.append("aload_0");
        addEndLine();
        methodString.append("return");
        addEndLine();
    }

    public void addParameters() {
        ArrayList<Element> params = method.getParams();
        methodString.append("(");

        for (int i = 0; i < params.size(); i++) {
            Type type = params.get(i).getType();
            methodString.append(TranslateType.getJasminType(type));
            methodString.append(",");
        }

        methodString.deleteCharAt(methodString.length() - 1);
        methodString.append(")");

    }
}
