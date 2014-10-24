package de.sternad.morphiacdi.config;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({ TYPE, METHOD, FIELD, PARAMETER })
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigValue {
	@Nonbinding
	String key() default "";

	@Nonbinding
	boolean mandatory() default false;

	@Nonbinding
	String defaultValue() default "";
}