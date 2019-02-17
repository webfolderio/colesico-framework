package colesico.framework.assist.codegen.model;

import colesico.framework.assist.StrUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MethodElement extends ParserElement {

    protected final ClassElement parentClass;
    protected final ExecutableElement originExecutableElement;

    public MethodElement(ProcessingEnvironment processingEnv, ClassElement parentClass, ExecutableElement methodElement) {
        super(processingEnv);
        this.parentClass = parentClass;
        this.originExecutableElement = methodElement;
    }

    @Override
    public ExecutableElement unwrap() {
        return originExecutableElement;
    }

    public ClassElement getParentClass() {
        return parentClass;
    }

    public ExecutableType getExecutableType() {
        ClassElement parentClass = getParentClass();
        ExecutableType result = (ExecutableType) parentClass.asClassType().getMemberType(originExecutableElement);
        return result;
    }

    public TypeMirror getReturnType() {
        return getExecutableType().getReturnType();
    }

    public ClassType getReturnClassType() {
        if (getReturnType().getKind() == TypeKind.DECLARED) {
            return new ClassType(getProcessingEnv(), (DeclaredType) getReturnType());
        }
        return null;
    }

    public String getName() {
        return originExecutableElement.getSimpleName().toString();
    }

    public String getNameWithPrefix(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            return getName();
        }
        return StrUtils.addPrefix(prefix, getName());
    }

    public List<ParameterElement> getParameters() {
        List<? extends VariableElement> params = originExecutableElement.getParameters();
        List<? extends TypeMirror> paramTypes = getExecutableType().getParameterTypes();

        List<ParameterElement> result = new ArrayList<>();
        int i = 0;
        for (VariableElement paramElement : params) {
            TypeMirror paramType = paramTypes.get(i++);
            result.add(new ParameterElement(processingEnv, this, paramElement, paramType));
        }
        return result;
    }

    public List<ParameterElement> getParametersFiltered(Predicate<ParserElement> filter) {
        throw new NotImplementedException("getParametersFiltered not implemented yet");
    }

    public boolean isConstractor() {
        return originExecutableElement.getKind() == ElementKind.CONSTRUCTOR;
    }

    public boolean isGetter() {
        return StringUtils.startsWith(getName(), "get") && getParameters().isEmpty()
                && !(getReturnType() instanceof NoType);
    }

    public boolean isSetter() {
        return StringUtils.startsWith(getName(), "set") && (getParameters().size() == 1)
                && (getReturnType() instanceof NoType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodElement that = (MethodElement) o;

        return originExecutableElement != null ? originExecutableElement.equals(that.originExecutableElement) : that.originExecutableElement == null;
    }

    @Override
    public int hashCode() {
        return originExecutableElement != null ? originExecutableElement.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MethodElement{" +
                "originElement=" + originExecutableElement +
                '}';
    }
}
