module colesico.framework.htmlrenderer {

    requires transitive colesico.framework.weblet;
    requires transitive colesico.framework.validation;

    //requires org.slf4j;
    requires slf4j.api;
    requires org.apache.commons.lang3;

    // API
    exports colesico.framework.htmlrenderer;
}