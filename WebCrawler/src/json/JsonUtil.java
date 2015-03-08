package json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import feed.FeedInfo;

public class JsonUtil {
	public static String printCreateDocIdJson(String indexName) {
		// Line required before each document
		Gson gson = new Gson();
		JsonObject createDocId = new JsonObject();
		JsonObject createProperties = new JsonObject();
		createProperties.addProperty("_index", indexName);
		createProperties.addProperty("_type", "doc");
		createDocId.add("create", createProperties);
		return gson.toJson(createDocId);
	}

	public static String printTextFileJson(long textFileName, FeedInfo feedInfo) {
		Gson gson = new Gson();
		// build the json object
		JsonObject textDocument = new JsonObject();
		textDocument.addProperty("doc_id", textFileName);
		textDocument.addProperty("url", feedInfo.getLink());
		textDocument.addProperty("title", feedInfo.getTitle());
		textDocument.addProperty("pubDate", feedInfo.getPubDate().toString());
		textDocument.addProperty("category", feedInfo.getCategory());
		textDocument.addProperty("description", feedInfo.getDescription());
		textDocument.addProperty("author", feedInfo.getAuthor());
		textDocument.addProperty("keywords", feedInfo.getKeywords());
		textDocument.addProperty("imagePath", feedInfo.getImagePath());
		textDocument.addProperty("location", feedInfo.getLocation());
		textDocument.addProperty("body", feedInfo.getArticleText());
		return gson.toJson(textDocument);
	}
}
