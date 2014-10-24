package de.sternad.morphiacdi.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.mongodb.WriteResult;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.utils.FieldName;
import org.slf4j.Logger;

import de.sternad.morphiacdi.model.Person;

import java.util.List;

@ApplicationScoped
public class PersonService {

    // Type safe field name for query, evaluates very early
    public static final String _LASTNAME = FieldName.of(Person.class,"lastName");

    @Inject
	private Datastore ds;

	@Inject
	private Logger log;
	
	public String create(String lastName, String firstName) {
		Key<Person> key = ds.save(new Person(lastName, firstName));
		log.debug(" person created: " + key.toString());
	    return key.toString();
    }

    public List<Person> find() {
        return ds.find(Person.class).asList();
    }

    public List<Person> findByLastName(String lastName) {
      return  ds.createQuery(Person.class).field("lastName").equal(lastName).asList();
    }

    public Key<Person> update(Person p) {
        Key<Person> key = ds.merge(p);
        return key;
    }

    public int deleteByLastName(String lastName){
        int deletedRecords = ds.delete(ds.createQuery(Person.class).field(_LASTNAME).equal(lastName)).getN();
        return deletedRecords;
    }
}
