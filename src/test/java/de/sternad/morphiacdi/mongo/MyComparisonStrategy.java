package de.sternad.morphiacdi.mongo;

import java.io.IOException;
import java.io.InputStream;

import com.lordofthejars.nosqlunit.core.IOUtils;
import com.lordofthejars.nosqlunit.mongodb.MongoComparisonStrategy;
import com.lordofthejars.nosqlunit.mongodb.MongoDbConnectionCallback;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class MyComparisonStrategy implements MongoComparisonStrategy {
		
	@Override
	public boolean compare(MongoDbConnectionCallback connection, InputStream dataset) throws IOException {
		String expectedJsonData = loadContentFromInputStream(dataset);
		DBObject parsedData = parseData(expectedJsonData);

		MyMongoDbAssertion.strictAssertEquals(parsedData, connection.db());
		
		return true;
	}

	private String loadContentFromInputStream(InputStream inputStreamContent) throws IOException {
		return IOUtils.readFullStream(inputStreamContent);
	}
	
	private DBObject parseData(String jsonData) throws IOException {
		DBObject parsedData = (DBObject) JSON.parse(jsonData);
		return parsedData;
	}

}
