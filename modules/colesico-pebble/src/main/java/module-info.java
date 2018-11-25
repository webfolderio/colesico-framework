module colesico.framework.pebble {

    //requires org.slf4j;
    requires slf4j.api;
    requires org.apache.commons.lang3;

    requires transitive colesico.framework.htmlrenderer;

    requires transitive pebble;

    // classes
    exports colesico.framework.pebble;
    exports colesico.framework.pebble.internal to colesico.framework.ioc;
}