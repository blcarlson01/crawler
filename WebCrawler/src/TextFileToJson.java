import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class TextFileToJson {
	
	public static String printCreateDocIdJson()
	{
		// Line required before each document
		Gson gson = new Gson();
		JsonObject createDocId = new JsonObject();
		JsonObject createProperties = new JsonObject();
		createProperties.addProperty("_index", "blcrlsn2_index");
		createProperties.addProperty("_type", "doc");	
		createDocId.add("create", createProperties);
		return gson.toJson(createDocId);
	}

	public static void main(String[] args) {
		Gson gson = new Gson();
		
		JsonObject textDocument = new JsonObject();
		textDocument.addProperty("doc_id", "NAME_OF_TXT_FILE");
		textDocument.addProperty("url", "YOUR_URL");
		textDocument.addProperty("title", "YOUR_TITLE");
		textDocument.addProperty("body", "YOUR BODY");
		
		
		
		
		
		System.out.println(printCreateDocIdJson());
		System.out.println(gson.toJson(textDocument));

	}

}
