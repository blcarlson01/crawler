package feed;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import document.DocumentPath;

public class FeedUtils {
	public static List<FeedInfo> getFeedReturnNewArticles(String feed,
			String category) {
		URL feedUrl;
		List<FeedInfo> articles = new ArrayList<FeedInfo>();
		try {
			feedUrl = new URL(feed);
			/*
			 * <title>Peru withdrawing ambassador from Chile over spying
			 * accusations</title>
			 * <link>http://feeds.reuters.com/~r/Reuters/worldNews
			 * /~3/2YbnT0Xutt4/story01.htm</link> <pubDate>Sat, 07 Mar 2015
			 * 21:06:35 GMT</pubDate> <category domain="">worldNews</category>
			 */
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feedxml = input.build(new XmlReader(feedUrl));

			for (int i = 0; i < feedxml.getEntries().size(); i++) {
				SyndEntry entry = feedxml.getEntries().get(i);
				// Get Link and check to see if its new, skip if exists
				if (isLinkNew(entry.getLink())) {
					// Get Create Object of Title, Link, Pubdate and return as
					// list of news items to crawl
					articles.add(new FeedInfo(entry.getLink(),
							entry.getTitle(), entry.getPublishedDate(),
							category));
				}
			}

		} catch (IllegalArgumentException | FeedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return articles;
	}

	@SuppressWarnings("unchecked")
	private static boolean isLinkNew(String link) {
		MessageDigest md;
		Boolean isNew = false;
		try {
			// Create Hash of URL
			md = MessageDigest.getInstance("SHA-256");
			md.update(link.getBytes());
			byte byteData[] = md.digest();

			// convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
						.substring(1));
			}

			// Read File and check list to see if hash is in the file
			final String path = DocumentPath.LINKSPATH + "hashedLinks.txt";
			final File file = new File(path);
			List<String> lines = new ArrayList<String>();
			if (file.exists()) {
				// Read File
				lines = FileUtils.readLines(file, "utf-8");
			} else {
				// Create File
				file.createNewFile();
			}

			isNew = !lines.contains(sb.toString());
			if (isNew) {
				// Append to File
				FileWriter fileWritter = new FileWriter(path, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(sb.toString());
				bufferWritter.newLine();
				bufferWritter.flush();
				bufferWritter.close();
			}

		} catch (NoSuchAlgorithmException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return isNew;
	}
}
