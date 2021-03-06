package colesico.framework.weblet.teleapi;

import java.lang.annotation.*;


/**
 * Specifies custom reader for tele-param
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Inherited
@Documented
public @interface WebletParamReader {
    Class<? extends WebletTeleReader> value();
}
