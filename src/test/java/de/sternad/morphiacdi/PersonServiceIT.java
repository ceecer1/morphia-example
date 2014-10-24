package de.sternad.morphiacdi;

import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import de.sternad.morphiacdi.model.Person;
import de.sternad.morphiacdi.mongo.MyComparisonStrategy;
import de.sternad.morphiacdi.service.PersonService;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;

@CustomComparisonStrategy(comparisonStrategy = MyComparisonStrategy.class)
@UsingDataSet(loadStrategy=LoadStrategyEnum.CLEAN_INSERT)
public class PersonServiceIT extends AbstractArquillianTest {

	@Inject
	private PersonService service;
	
    @Test
    @UsingDataSet
    @ShouldMatchDataSet
    public void create() {
		service.create("Sternad", "Kai");
	}

    @Test
    @UsingDataSet
    public void findByLastName() {
        List<Person> persons = service.findByLastName("Smith");
        assertEquals(1, persons.size());
        Person actual = persons.get(0);
        Person expected = new Person("Smith", "John");
        assertEquals(expected, actual);
    }

    @Test
    @UsingDataSet
    public void update() {
        List<Person> persons = service.findByLastName("Smith");
        assertEquals(1, persons.size());
        Person original = persons.get(0);
        original.setLastName("Karlsson");
        service.update(original);
    }

    @Test
    @UsingDataSet
    @ShouldMatchDataSet
    public void delete() {
        int deleted = service.deleteByLastName("Winter");
        assertEquals(1, deleted);
    }

}
