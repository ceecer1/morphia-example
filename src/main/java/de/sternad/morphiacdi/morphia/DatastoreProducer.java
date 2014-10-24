package de.sternad.morphiacdi.morphia;

import java.io.Serializable;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.ValidationExtension;
import org.mongodb.morphia.logging.MorphiaLoggerFactory;
import org.mongodb.morphia.logging.slf4j.SLF4JLoggerImplFactory;
import org.slf4j.Logger;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import de.sternad.morphiacdi.cdi.Eager;
import de.sternad.morphiacdi.config.ConfigValue;
import de.sternad.morphiacdi.model.BaseEntity;
import de.sternad.morphiacdi.model.Person;

@Eager
@ApplicationScoped
public class DatastoreProducer implements Serializable {

	private static final long serialVersionUID = -472588366780494355L;

	@Inject
	private Logger log;

	@Inject
	@ConfigValue(key = "mongo.URI")
	private String mongoURI;

	@Inject @Any
	private Instance<BaseEntity> entities;
	
	private Datastore datastore = null;

	@PostConstruct
	public void init() {
		log.debug("initializing {}", this.getClass().toString());

		MongoClientURI mongoClientURI = new MongoClientURI(mongoURI);
		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient(mongoClientURI);
		} catch (UnknownHostException e) {
			throw new IllegalStateException(e);
		}

		// http://stackoverflow.com/questions/6832517/how-to-check-from-a-driver-if-mongodb-server-is-running
		mongoClient.getDatabaseNames();

		Morphia morphia = new Morphia();

		// Morphia Logger Plugin
		MorphiaLoggerFactory.reset();
        MorphiaLoggerFactory.registerLogger(SLF4JLoggerImplFactory.class);

		// JSR 303 Interceptors
		new ValidationExtension(morphia);

		datastore = morphia.createDatastore(mongoClient,
				mongoClientURI.getDatabase());

		for (BaseEntity b : entities) {
			morphia.map(b.getClass());
		}
		
		datastore.ensureIndexes();

		morphia.getMapper().getConverters()
				.addConverter(BigDecimalConverter.class);
	}

	@Produces
	public Datastore getDatastore() {
		return datastore;
	}
}