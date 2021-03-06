/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
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
 */

package colesico.framework.service.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractProcessor;
import colesico.framework.service.codegen.generator.IocGenerator;
import colesico.framework.service.codegen.generator.ServiceProxyGenerator;
import colesico.framework.service.codegen.model.ServiceElement;
import colesico.framework.service.codegen.modulator.ModulatorKit;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static colesico.framework.service.ServiceProxy.PROXY_CLASS_SUFFIX;
import static colesico.framework.service.ServiceProxy.SERVICE_CLASS_SUFFIX;

/**
 * Процессор аннотации осуществляющий обработку классов помеченных аннотацией @Service
 *
 * @author Vladlen Larionov
 */
public class ServiceProcessor extends FrameworkAbstractProcessor {

    protected final Logger logger;
    protected final ModulatorKit modulatorKit;
    protected ProcessorContext context;

    public ServiceProcessor() {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
        System.setProperty("org.slf4j.simpleLogger.log.colesico.framework", "debug");
        logger = LoggerFactory.getLogger(ProcessorContext.class);
        logger.debug("Creating modulator kit...");

        modulatorKit = new ModulatorKit();
        modulatorKit.lookup();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    protected Class<? extends Annotation>[] getSupportedAnnotations() {
        Set<Class<? extends Annotation>> result = new HashSet<>();
        for (Class<? extends Annotation> ac : modulatorKit.getServiceAnnotations()) {
            logger.debug("Use service annotation: " + ac.getName());
            result.add(ac);
        }
        return result.toArray(new Class[result.size()]);
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        try {
            super.init(processingEnv);
            context = new ProcessorContext(modulatorKit, processingEnv);
            modulatorKit.notifyInit(context);
        } catch (Exception e) {
            String msg = ExceptionUtils.getRootCauseMessage(e);
            processingEnv.getMessager().printMessage(Kind.ERROR, msg);
            if (logger.isDebugEnabled()) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            RoundContext roundContext = new RoundContext(annotations, roundEnv);
            modulatorKit.notifyRoundStart(roundContext);
            processClasses(annotations, roundEnv);
            modulatorKit.notifyRoundStop();
            return true;
        } catch (CodegenException ce) {
            logger.error("Error processing service: " + ExceptionUtils.getRootCauseMessage(ce));
            throw ce;
        } catch (Exception e) {
            String msg = ExceptionUtils.getRootCauseMessage(e);
            context.getMessager().printMessage(Kind.ERROR, msg);
            if (logger.isDebugEnabled()) {
                e.printStackTrace();
            }
            return true;
        }
    }

    protected void processClasses(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        logger.debug("Start service processing...");
        context.getProcessedServices().clear();

        Set<String> serviceTypsNames = new HashSet<>();

        for (Class<? extends Annotation> serviceAnnotation : modulatorKit.getServiceAnnotations()) {
            for (Element elm : roundEnv.getElementsAnnotatedWith(serviceAnnotation)) {
                if (elm.getKind() != ElementKind.CLASS) {
                    continue;
                }
                TypeElement serviceType = null;
                try {
                    serviceType = (TypeElement) elm;
                    // @Service annotation are inherited from superclass so need to filter generated proxies by name.
                    // But this is not correct in general, so  need to change in perspective.
                    if (StringUtils.endsWith(serviceType.getSimpleName(), SERVICE_CLASS_SUFFIX + PROXY_CLASS_SUFFIX)) {
                        continue;
                    }
                    if (serviceTypsNames.contains(serviceType.toString())) {
                        // There is no need to throw an error because the models can be annotated
                        // with many different types of annotation (@Service annotation aliases)
                        logger.debug("Service has already been processed: " + serviceType.toString());
                        continue;
                    }
                    logger.debug("Parsing service: " + serviceType.toString());
                    ServiceParser parser = new ServiceParser(context);
                    ServiceElement serviceElement = parser.parse(serviceType);
                    context.getProcessedServices().add(serviceElement);
                    serviceTypsNames.add(serviceType.asType().toString());
                    ServiceProxyGenerator serviceGenerator = new ServiceProxyGenerator(context);
                    serviceGenerator.generateService(serviceElement);
                } catch (CodegenException ce) {
                    String message = "Error processing class '" + serviceType.toString() + "': " + ce.getMessage();
                    logger.error(message);
                    ce.print(processingEnv, elm);
                    throw ce;
                } catch (Exception e) {
                    processingEnv.getMessager().printMessage(Kind.ERROR, "Processing class general error: '" + e.getMessage(), serviceType);
                    if (logger.isDebugEnabled()) {
                        e.printStackTrace();
                    }
                    throw e;
                }
            }
        }

        if (!context.getProcessedServices().isEmpty()) {
            new IocGenerator(context).generate();
        }
    }

}
