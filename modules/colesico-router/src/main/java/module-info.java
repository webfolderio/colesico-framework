module colesico.framework.router {

    requires static java.compiler;
    requires static auto.service;
    requires static com.squareup.javapoet;

    requires transitive colesico.framework.service;
    requires transitive colesico.framework.http;

    //requires org.slf4j;
    requires slf4j.api;
    requires org.apache.commons.lang3;

    // classes

    exports colesico.framework.router;
    exports colesico.framework.router.assist;
    exports colesico.framework.router.internal to colesico.framework.ioc;
    exports colesico.framework.router.codegen;
}