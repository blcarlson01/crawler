import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import elastics.searchresults.Hit;
import elastics.searchresults.SearchResults;

public class ParseElasticSearchJson {

	public static void main(String[] args) throws JsonSyntaxException,
			JsonIOException, IOException {
		String orgFile = "/Users/blcarlson/git/CS410/WebCrawler/src/results.json";
		Gson gson = new GsonBuilder().create();
		SearchResults p = gson.fromJson(new FileReader(orgFile),
				SearchResults.class);
		// System.out.println(p.hits.total);

		// Write value to file
		String textFile = "/Users/blcarlson/git/CS410/WebCrawler/src/blcrlsn2_query_1.txt";
		BufferedWriter textWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(textFile), "UTF-8"));

		for (Hit hit : p.hits.hits) {
			System.out.println(hit._source.doc_id + " : " + hit._source.title);

			textWriter.write(hit._source.doc_id + ",");
			textWriter.newLine();
		}
		textWriter.flush();
		textWriter.close();

		System.out.println("Size: " + p.hits.hits.size());
	}
}
