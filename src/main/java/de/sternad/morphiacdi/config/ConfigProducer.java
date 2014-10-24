package de.sternad.morphiacdi.config;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.slf4j.Logger;

import de.sternad.morphiacdi.cdi.Eager;

@ApplicationScoped
@Eager
public class ConfigProducer {
	private static final String MANDATORY_PARAM_MISSING = "No definition for mandatory field found: '{0}'";
	private final String BUNDLE_FILE_NAME = "configuration";
	private final ResourceBundle bundle = ResourceBundle
			.getBundle(BUNDLE_FILE_NAME);

	@Inject
	private Logger log;

	@Produces
	@ConfigValue
	public String produceConfigString(InjectionPoint ip)
			throws IllegalStateException {
		return getValue(ip);
	}

	private String getValue(InjectionPoint ip) {
		ConfigValue param = ip.getAnnotated().getAnnotation(ConfigValue.class);
		String configValue = null;
		log.debug(
				"config value requested for key:{}, mandatory:{}, defaultValue:{}",
				param.key(), param.mandatory(), param.defaultValue());

		if (param.key() == null || param.key().length() == 0) {
			configValue = param.defaultValue();
		} else {
			try {
				configValue = bundle.getString(param.key());
				if (configValue == null || configValue.trim().length() == 0) {
					if (param.mandatory()) {
						throw new IllegalStateException(MessageFormat.format(
								MANDATORY_PARAM_MISSING,
								new Object[] { param.key() }));
					} else {
						configValue = param.defaultValue();
					}
				}
			} catch (MissingResourceException e) {
				if (param.mandatory()) {
					log.error("config value " + param.key()
							+ " could not be found, but is mandatory");
					throw new IllegalStateException(MessageFormat.format(
							MANDATORY_PARAM_MISSING,
							new Object[] { param.key() }));
				}
				configValue = param.defaultValue();
			}
		}
		log.debug("config value found:{}", configValue);
		return configValue;
	}
}
