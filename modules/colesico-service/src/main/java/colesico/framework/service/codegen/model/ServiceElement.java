/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package colesico.framework.service.codegen.model;

import colesico.framework.assist.Elements;
import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.service.ServicePrototype;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represnts a Framework Automated Bean
 *
 * @author Vladlen Larionov
 */
public final class ServiceElement {

    /**
     * Service origin class element
     */
    private final TypeElement originClass;

    /**
     * Service proxy interfaces
     */
    private final Elements<TypeName> interfaces;

    /**
     * Service proxy extra fields
     */
    private final Elements<FieldElement> fields;

    private final Elements<ProxyMethodElement> proxyMethods;

    private final Elements<CustomMethodElement> customMethods;

    private final Elements<CodeBlock> constructorExtraCode;

    private final Elements<TeleFacadeElement> teleFacades;

    /**
     * Common purpose properties
     */
    private final Map<Class, Object> properties;

    private final DeclaredType scopeAnnotation;

    public ServiceElement(TypeElement originClass, DeclaredType scopeAnnotation) {
        this.originClass = originClass;
        this.scopeAnnotation = scopeAnnotation;
        this.interfaces = new Elements<>();
        this.proxyMethods = new Elements<>();
        this.fields = new Elements<>();
        this.customMethods = new Elements<>();
        this.constructorExtraCode = new Elements<>();
        this.teleFacades = new Elements<>();
        this.properties = new HashMap();
    }

    public void addField(final FieldElement field) {
        FieldElement mfe = fields.find(f -> f.getName().equals(field.getName()));
        if (mfe != null) {
            if (mfe.getTypeName().equals(field.getTypeName())) {
                return;
            }
            throw CodegenException.of().message("Duplicate field name:" + field.getName()).element(getOriginClass()).create();
        }
        fields.add(field);
        field.parentService = this;
    }

    public void addInterface(TypeName interfaceTypeName) {
        TypeName intf = interfaces.find(i -> i.toString().equals(interfaceTypeName.toString()));
        if (intf != null) {
            return;
        }
        interfaces.add(interfaceTypeName);
    }

    public void addProxyMethod(final ProxyMethodElement proxyMethod) {
        proxyMethods.add(proxyMethod);
        proxyMethod.parentService = this;
    }

    public void addCustomMethod(final CustomMethodElement customMethod) {
        CustomMethodElement cme = customMethods.find(m -> m.getName().equals(customMethod.getName()));
        if (cme != null) {
            throw CodegenException.of().message("Duplicate method name:" + customMethod.getName()).element(getOriginClass()).create();
        }
        customMethods.add(customMethod);
        customMethod.parentService = this;
    }

    public void addTeleFacade(TeleFacadeElement teleFacade) {
        TeleFacadeElement ta = teleFacades.find(t -> t.getTeleType().equals(teleFacade.getTeleType()));
        if (ta != null) {
            throw CodegenException.of().message("Duplicate teleDriver-facade: " + teleFacade.getTeleType()).element(getOriginClass()).create();
        }
        teleFacade.parentService = this;
        teleFacades.add(teleFacade);
    }

    public void addConstuctorExtraCode(CodeBlock cb) {
        constructorExtraCode.add(cb);
    }

    public Elements<TypeName> getInterfaces() {
        return interfaces;
    }

    public Elements<FieldElement> getFields() {
        return fields;
    }

    public Elements<CustomMethodElement> getCustomMethods() {
        return customMethods;
    }

    public Elements<ProxyMethodElement> getProxyMethods() {
        return proxyMethods;
    }

    public Object getProperty(Class propertyClass) {
        return properties.get(propertyClass);
    }

    public void setProperty(Object property) {
        properties.put(property.getClass(), property);
    }

    public Elements<CodeBlock> getConstructorExtraCode() {
        return constructorExtraCode;
    }

    public Elements<TeleFacadeElement> getTeleFacades() {
        return teleFacades;
    }


    public DeclaredType getScopeAnnotation() {
        return scopeAnnotation;
    }

    public TypeElement getOriginClass() {
        return originClass;
    }

    public String getProxyClassSimpleName() {
        return originClass.getSimpleName().toString() + ServicePrototype.PROXY_CLASS_SUFFIX;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceElement that = (ServiceElement) o;
        return Objects.equals(originClass, that.originClass);
    }

    @Override
    public int hashCode() {

        return Objects.hash(originClass);
    }
}
