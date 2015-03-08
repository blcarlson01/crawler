import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RetriveHtmlOld {

	private final static String FILEPATH = "/Users/blcarlson/Documents/Grad Program/CS410/hw2/ask.slashdot.org/blcrlsn2_";

	// Parse the webpage and save required text to text file
	// Format:
	// URL
	// Title
	// Body of the article
	public static void CreateTextFile(Document doc, int articleId,
			String articleLink, String linkText) throws IOException {
		String textFile = FILEPATH + articleId + ".txt";
		BufferedWriter textWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(textFile), "UTF-8"));
		File input = new File(
				"/Users/blcarlson/Documents/Grad Program/CS410/hw2/ask.slashdot.org/blcrlsn2_"
						+ articleId + ".html");
		doc = Jsoup.parse(input, "UTF-8", "http://ask.slashdot.org");
		Element details = doc.select("div.details").first();
		Element body = doc.select("div.body").first();

		// Actual Question
		// System.out.println(details.text());
		// System.out.println(body.text());
		textWriter.write(articleLink);
		textWriter.newLine();
		textWriter.write(linkText);
		textWriter.newLine();
		textWriter.write(details.text());
		textWriter.write(body.text());
		textWriter.newLine();

		// Parse out the Comments
		Elements commentDetails = doc.select("ul li div.details");
		Elements commentText = doc.select("ul li div.commentBody");
		for (int x = 0; x < commentDetails.size(); x++) {
			// System.out.println(commentDetails.get(x).text());
			// System.out.println(commentText.get(x).text());
			textWriter.write(commentDetails.get(x).text());
			textWriter.write(commentText.get(x).text());
			textWriter.newLine();
			// System.out.println("-----");
		}

		textWriter.flush();
		textWriter.close();

		System.out.println("Successfully wrote: " + textFile);
	}

	public static void main(String[] args) throws InterruptedException {

		System.out.println("-----------------------");
		Document doc;
		try {
			List<String> foundUrls = new ArrayList<String>();
			int uniqueId = 3213;
			for (int x = 1; x < 3; x++) {
				// Ask Slashdot
				// String page = "http://ask.slashdot.org/?page=" + x
				// + "&view=search&fhfilter=askslashdot";

				// Science
				String page = "http://slashdot.org/archive.pl?op=sections&keyword=developers&year=2010&page="
						+ x;
				doc = Jsoup.connect(page).timeout(20 * 1000).get();

				// Save Original File
				String orgFile = "/Users/blcarlson/Documents/Grad Program/CS410/hw2/ask.slashdot.org/org/"
						+ uniqueId + ".html";
				BufferedWriter orgWriter = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(orgFile),
								"UTF-8"));
				orgWriter.write(doc.toString());
				orgWriter.flush();
				orgWriter.close();

				System.out.println("Starting Page: " + x + " url: " + page);
				System.out.println("----------------------");
				// File input = new File(
				// "/Users/blcarlson/Documents/Grad Program/CS410/hw2/ask.slashdot.org/blcrlsn2.html");
				// doc = Jsoup.parse(input, "UTF-8", "http://ask.slashdot.org");

				// Elements articles = doc.select("article");
				// Elements links = articles.select("a[href]");
				Elements links = doc.select("a[href]");
				for (Element link : links) {
					String linkHref = link.attr("href"); // "http://example.com/"
					String linkText = link.text(); // "example""
					if (linkHref.contains("//developers.slashdot.org/story")
							&& !linkText.contains("Read the")
							&& !linkHref.startsWith("http:")
							&& !linkHref.startsWith("https:")
							&& !foundUrls.contains(linkHref.replace("//",
									"http://"))) {
						// Parse the parent document to get the link
						int articleId = uniqueId++;

						String htmlFile = "/Users/blcarlson/Documents/Grad Program/CS410/hw2/ask.slashdot.org/blcrlsn2_"
								+ articleId + ".html";
						File f = new File(htmlFile);
						if (!f.exists()) {
							String articleLink = linkHref.replace("//",
									"http://");
							System.out.println(articleLink);
							foundUrls.add(articleLink);
							System.out.println(linkText);

							// Wait to not anger the Internet gods
							TimeUnit.SECONDS.sleep(8);
							// Connect to the website and get the webpage
							doc = Jsoup.connect(articleLink).timeout(20 * 1000)
									.get();

							// Save the webpage in required format
							BufferedWriter htmlWriter = new BufferedWriter(
									new OutputStreamWriter(
											new FileOutputStream(htmlFile),
											"UTF-8"));
							htmlWriter.write(doc.toString());
							htmlWriter.flush();
							htmlWriter.close();

							// Parse the webpage and save required text to text
							// file
							CreateTextFile(doc, articleId, articleLink,
									linkText);

							System.out.println("Successfully wrote: "
									+ htmlFile);
							System.out.println("-----------------------");
							// Wait to not anger the Internet gods
							TimeUnit.SECONDS.sleep(8);
						}
					}
				}

				System.out.println("Completed Page: " + x);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
