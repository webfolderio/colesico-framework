package colesico.framework.assist.codegen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

abstract public class FrameworkAbstractProcessor extends AbstractProcessor {

    protected Logger logger;

    public FrameworkAbstractProcessor() {
        try {
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
            System.setProperty("org.slf4j.simpleLogger.log.colesico.framework", "debug");
            logger = LoggerFactory.getLogger(this.getClass());
        } catch (Throwable e) {
            System.out.print("Logger creation error: ");
            System.out.println(e);
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    abstract protected Class<? extends Annotation>[] getSupportedAnnotations();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> result = new HashSet<>();
        Class<? extends Annotation>[] annClasses = getSupportedAnnotations();
        for (Class<? extends Annotation> c : annClasses) {
            result.add(c.getName());
        }
        return result;
    }

    protected void onInit() {}

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        try {
            logger.debug("Initialize " + this.getClass().getName() + " annotation processor...");
            onInit();
        } catch (Throwable e) {
            System.out.print("Error initializing " + this.getClass().getName() + " annotation processor ");
            System.out.println(e);
        }
    }

    protected Elements getElementUtils(){
        return processingEnv.getElementUtils();
    }

    protected Messager getMessager(){
        return processingEnv.getMessager();
    }

    protected Filer getFiler(){
        return processingEnv.getFiler();
    }

    protected Types getTypeUtils(){
        return processingEnv.getTypeUtils();
    }

    protected Map<String,String> getOptions(){
        return processingEnv.getOptions();
    }
}
