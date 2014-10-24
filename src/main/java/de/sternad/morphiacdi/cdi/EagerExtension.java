package de.sternad.morphiacdi.cdi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.util.AnnotationLiteral;
import java.util.Set;

public class EagerExtension implements Extension {

	private static Logger LOG = LoggerFactory.getLogger(EagerExtension.class);

	public void afterDeploymentValidation(
			@Observes AfterDeploymentValidation event, BeanManager beanManager) {

		@SuppressWarnings("serial")
		Set<Bean<?>> beans = beanManager.getBeans(Object.class,
				new AnnotationLiteral<Eager>() {
				});

		for (Bean<?> bean : beans) {
			beanManager.getReference(bean, bean.getBeanClass(),
					beanManager.createCreationalContext(bean)).toString();
			LOG.debug("eagerly initialized bean: " + bean.toString());
		}

	}
}