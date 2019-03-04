package colesico.framework.assist.codegen.model;

import colesico.framework.assist.codegen.CodegenException;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public class ClassType extends ParserType {

    protected final DeclaredType originDeclaredType;

    public ClassType(ProcessingEnvironment processingEnv, DeclaredType classType) {
        super(processingEnv);
        if (classType == null){
            throw CodegenException.of().message("classType is null").build();
        }
        this.originDeclaredType = classType;
    }

    public ClassType(ProcessingEnvironment processingEnv, TypeElement typeElement) {
        super(processingEnv);
        if (typeElement == null){
            throw CodegenException.of().message("typeElement is null").build();
        }
        this.originDeclaredType = (DeclaredType) typeElement.asType();
    }

    @Override
    public DeclaredType unwrap() {
        return originDeclaredType;
    }

    public TypeElement asElement() {
        return (TypeElement) originDeclaredType.asElement();
    }

    public ClassElement asClassElement() {
        return new ClassElement(getProcessingEnv(), asElement());
    }

    public DeclaredType getErasure() {
        return (DeclaredType) getTypeUtils().erasure(originDeclaredType);
    }

    public TypeMirror getMemberType(Element element) {
        return getTypeUtils().asMemberOf(originDeclaredType, element);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassType classType = (ClassType) o;

        return originDeclaredType != null ? originDeclaredType.equals(classType.originDeclaredType) : classType.originDeclaredType == null;
    }

    @Override
    public int hashCode() {
        return originDeclaredType != null ? originDeclaredType.hashCode() : 0;
    }
}