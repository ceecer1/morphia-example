package de.sternad.morphiacdi.morphia;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

@ApplicationScoped
public class DaoProducer implements Serializable {

	private static final long serialVersionUID = -451493209484211677L;

	@Inject
	private Datastore datastore;

	@SuppressWarnings("unchecked")
	@Produces
	public synchronized <T, ID> BasicDAO<T, ID> produce(InjectionPoint ip,
			BeanManager bm) {
		ParameterizedType type = (ParameterizedType) ip.getType();
		Type[] typeArgs = type.getActualTypeArguments();
		Class<T> entityClass = (Class<T>) typeArgs[0];
		return new BasicDAO<T, ID>(entityClass, datastore);

	}
}