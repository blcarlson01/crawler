import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class BulkImportToSearch {

	public static void main(String[] args) throws IOException,
			InterruptedException {
		// Bulk Import to ElasticSearch
		String path = "/Users/blcarlson/Documents/Grad Program/CS410/hw2/ask.slashdot.org/json";
		final File folder = new File(path);
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.getName().startsWith("DataAs")) {
				File jsonFile = new File(path + '/' + fileEntry.getName());
				HttpEntity entity = new FileEntity(jsonFile);
				HttpPost post = new HttpPost("http://localhost:9200/_bulk");
				post.setEntity(entity);
				HttpClientBuilder clientBuilder = HttpClientBuilder.create();
				HttpClient client = clientBuilder.build();
				post.addHeader("content-type", "text/plain");
				post.addHeader("Accept", "text/plain");
				HttpResponse response = client.execute(post);
				System.out.println("Response: " + response);
				TimeUnit.SECONDS.sleep(10);
			}
		}

	}

}
