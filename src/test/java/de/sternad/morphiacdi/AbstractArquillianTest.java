package de.sternad.morphiacdi;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Rule;
import org.junit.runner.RunWith;

import com.lordofthejars.nosqlunit.mongodb.MongoDbConfigurationBuilder;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;

@RunWith(Arquillian.class)
public abstract class AbstractArquillianTest {

	@Rule
	public MongoDbRule remoteMongoDbRule = new MongoDbRule(
			MongoDbConfigurationBuilder.mongoDb().databaseName("morphiatest")
					.port(27017).host("localhost").build());

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(JavaArchive.class)
				.addPackages(true, "de/sternad/morphiacdi")
				.addAsResource("configuration.properties")
                .addAsResources(de.sternad.morphiacdi.PersonServiceIT.class.getPackage(),
                        "PersonServiceIT#create.json",
                        "PersonServiceIT#create-expected.json",
                        "PersonServiceIT#findByLastName.json",
                        "PersonServiceIT#delete.json",
                        "PersonServiceIT#delete-expected.json",
                        "PersonServiceIT#update.json",
                        "PersonServiceIT#update-expected.json"
                        )
                .addAsResource("logback.xml")
                .addAsManifestResource("META-INF/services/javax.enterprise.inject.spi.Extension")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}
}
