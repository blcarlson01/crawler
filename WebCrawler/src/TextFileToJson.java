import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import json.JsonUtil;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TextFileToJson {

	/*
	 * Create the Json Object for each Text File
	 */
	private static String printTextFileJson(String textFileName, String url,
			String title, String body) {
		Gson gson = new Gson();
		JsonObject textDocument = new JsonObject();
		textDocument.addProperty("doc_id", textFileName);
		textDocument.addProperty("url", url);
		textDocument.addProperty("title", title);
		textDocument.addProperty("body", body);
		return gson.toJson(textDocument);
	}

	public static void main(String[] args) throws IOException {
		final File folder = new File(
				"/Users/blcarlson/Documents/Grad Program/CS410/hw2/ask.slashdot.org/blcrlsn2_txt");
		String orgFile = "/Users/blcarlson/Documents/Grad Program/CS410/hw2/ask.slashdot.org/DataAsJson0.txt";
		BufferedWriter orgWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(orgFile), "UTF-8"));
		int fileCount = 0;
		int newFileNumber = 0;
		for (final File fileEntry : folder.listFiles()) {
			FileReader fr = new FileReader(fileEntry);
			BufferedReader br = new BufferedReader(fr);
			String line;
			int lineNumber = 0;
			String url = "";
			String title = "";
			String body = "";

			while ((line = br.readLine()) != null) {
				if (lineNumber == 0) {
					// Grab URL
					url = line;
					// System.out.println(line);
				}

				if (lineNumber == 1) {
					title = line;
					// System.out.println(line);
				}

				if (lineNumber > 1) {
					body += line;
					// System.out.println(line);
				}

				lineNumber++;
			}
			br.close();
			fr.close();
			// System.out.println(printCreateDocIdJson());
			// System.out.println(printTextFileJson(fileEntry.getName(), url,
			// title, body));

			if (fileCount == 300) {
				fileCount = 0;
				orgWriter.flush();
				orgWriter.close();
				orgFile = "/Users/blcarlson/Documents/Grad Program/CS410/hw2/ask.slashdot.org/DataAsJson"
						+ ++newFileNumber + ".txt";
				orgWriter = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(orgFile), "UTF-8"));
			}

			orgWriter.write(JsonUtil.printCreateDocIdJson("blcrlsn2_index"));
			orgWriter.newLine();
			orgWriter.write(printTextFileJson(fileEntry.getName(), url, title,
					body));
			orgWriter.newLine();
			System.out.println("Completed File: " + fileEntry.getName()
					+ " Count: " + fileCount);
			fileCount++;
		}
		orgWriter.flush();
		orgWriter.close();
		System.out.println("File Count: " + folder.listFiles().length);
	}

}
