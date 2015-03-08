package document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import json.JsonUtil;

import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import feed.FeedInfo;

public class ProcessFile {

	public static void processArticle(FeedInfo feedInfo) {
		// Retrieve Page
		String page = feedInfo.getLink();
		System.out.println("Starting Page: " + page);
		System.out.println("----------------------");
		try {
			Document doc = Jsoup.connect(page).timeout(20 * 1000).get();
			// Create Unique Files Name
			long fileName = System.currentTimeMillis();

			// Save Original HTML File
			saveOriginalHTML(doc, fileName);
			System.out.println("Saved File: " + page);

			// Create TextFile
			feedInfo = createTextFile(doc, feedInfo, fileName);

			// Create Json
			createJsonFile(doc, feedInfo, fileName);
			System.out.println("JSON Created: " + page);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void createJsonFile(Document doc, FeedInfo feedInfo,
			long fileName) {
		final String path = DocumentPath.JSONPATH;
		Integer numOfFiles = new File(path).list().length;

		String item1 = path + numOfFiles + ".txt";
		try {
			final File file = new File(item1);
			if (file.exists()) {
				// Check Size
				// Get length of file in bytes
				long fileSizeInBytes = file.length();
				// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
				long fileSizeInKB = fileSizeInBytes / 1024;
				// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
				long fileSizeInMB = fileSizeInKB / 1024;
				if (fileSizeInMB > 15) {
					// Create New File
					String fileNameWithOutExt = FilenameUtils
							.removeExtension(file.getName());
					Integer addOne = Integer.parseInt(fileNameWithOutExt) + 1;
					String newItem = path + addOne + ".txt";
					final File newFile = new File(newItem);
					newFile.createNewFile();

					// Add to file
					addToJsonFile(newFile, feedInfo, fileName);

				} else {
					// Add to File
					addToJsonFile(file, feedInfo, fileName);
				}
			} else {
				// Create File
				file.createNewFile();
				addToJsonFile(file, feedInfo, fileName);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void addToJsonFile(File file, FeedInfo feedInfo,
			long fileName) {
		// Append to file
		FileWriter fileWritter;
		try {
			fileWritter = new FileWriter(file, true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(JsonUtil.printCreateDocIdJson("bluf_index"));
			bufferWritter.newLine();
			bufferWritter.write(JsonUtil.printTextFileJson(fileName, feedInfo));
			bufferWritter.newLine();
			bufferWritter.flush();
			bufferWritter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Parse the webpage and save required text to text file
	// Format:
	// URL
	// Title
	// Publish Date
	// URL Feed Category
	// Description
	// Author
	// Keywords
	// Image Path
	// Location
	// Article Text
	private static FeedInfo createTextFile(Document doc, FeedInfo feedInfo,
			long fileName) throws IOException {
		String textFile = DocumentPath.TEXTPATH + fileName + ".txt";
		BufferedWriter textWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(textFile), "UTF-8"));

		textWriter.write(feedInfo.getLink());
		textWriter.newLine();
		textWriter.write(feedInfo.getTitle());
		textWriter.newLine();
		textWriter.write(feedInfo.getPubDate().toString());
		textWriter.newLine();
		textWriter.write(feedInfo.getCategory());
		textWriter.newLine();

		// <META name="description" content="HAVANA (Reuters)
		Elements descriptionElement = doc.select("meta[name=description]");
		Element descriptionEl = descriptionElement.first();
		String description = descriptionEl != null ? descriptionEl
				.attr("content") : "";
		feedInfo.setDescription(description);
		textWriter.write(description);
		textWriter.newLine();

		// <META name="Author" content="By Nelson Acosta">
		Elements authorElement = doc.select("meta[name=Author]");
		Element authorEl = authorElement.first();
		String author = authorEl != null ? authorEl.attr("content") : "";
		feedInfo.setAuthor(author);
		textWriter.write(author);
		textWriter.newLine();

		// <META name="keywords" content="Colombia, Alvaro Pico Malaver">
		Elements keywordsElement = doc.select("meta[name=keywords]");
		Element keywordsEl = keywordsElement.first();
		String keywords = keywordsEl != null ? keywordsEl.attr("content") : "";
		feedInfo.setKeywords(keywords);
		textWriter.write(keywords);
		textWriter.newLine();

		// Image Path
		Element imageTag = doc.select("div#articleImage img").first();
		if (imageTag != null) {
			feedInfo.setImagePath(imageTag.attr("src"));
			textWriter.write(imageTag.attr("src"));
		} else {
			feedInfo.setImagePath("");
			textWriter.write("");
		}
		textWriter.newLine();

		// Location
		Element location = doc.select("span.articleLocation").first();
		if (location != null) {
			feedInfo.setLocation(location.text());
			textWriter.write(location.text());
		} else {
			feedInfo.setLocation("");
			textWriter.write("");
		}
		textWriter.newLine();

		// Article Text
		Element articleText = doc.select("span#articleText").first();
		feedInfo.setArticleText(articleText.text());
		textWriter.write(articleText.text());
		textWriter.newLine();

		textWriter.flush();
		textWriter.close();

		System.out.println("Successfully wrote: " + textFile);

		return feedInfo;
	}

	private static void saveOriginalHTML(Document doc, long title) {
		String orgFile = DocumentPath.HTMLPATH + title + ".html";
		BufferedWriter orgWriter;
		try {
			orgWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(orgFile), "UTF-8"));
			orgWriter.write(doc.toString());
			orgWriter.flush();
			orgWriter.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
