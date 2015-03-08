import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import document.ProcessFile;
import feed.FeedInfo;
import feed.FeedUtils;
import feed.Node;
import feed.NodeName;

public class RetrieveHtml {

	// RSS Feed List to retrieve news articles
	public static List<Node> allocateFeedsToNodes(NodeName node) {
		List<Node> feeds = new ArrayList<Node>();
		feeds.add(new Node(
				"http://feeds.reuters.com/Reuters/worldNews?format=xml",
				NodeName.BRANDON, "WorldNews"));
		feeds.add(new Node(
				"http://feeds.reuters.com/Reuters/domesticNews?format=xml",
				NodeName.BRANDON, "DomesticNews"));
		feeds.add(new Node(
				"http://feeds.reuters.com/Reuters/PoliticsNews?format=xml",
				NodeName.BRANDON, "PoliticsNews"));
		feeds.add(new Node(
				"http://feeds.reuters.com/reuters/healthNews?format=xml",
				NodeName.BRANDON, "HealthNews"));
		feeds.add(new Node(
				"http://feeds.reuters.com/reuters/environment?format=xml",
				NodeName.BRANDON, "Environment"));
		feeds.add(new Node("http://feeds.reuters.com/news/wealth?format=xml",
				NodeName.TOM, "Wealth"));
		feeds.add(new Node(
				"http://feeds.reuters.com/reuters/businessNews?format=xml",
				NodeName.TOM, "BusinessNews"));
		feeds.add(new Node(
				"http://feeds.reuters.com/reuters/scienceNews?format=xml",
				NodeName.TOM, "ScienceNews"));
		feeds.add(new Node(
				"http://feeds.reuters.com/reuters/globalmarketsNews?format=xml",
				NodeName.TOM, "GlobalMarketsNews"));
		feeds.add(new Node(
				"http://feeds.reuters.com/news/usmarkets?format=xml",
				NodeName.TOM, "USMarkets"));

		return feeds.stream().filter(p -> p.getNode() == node.getNodeName())
				.collect(Collectors.toList());
	}

	public static void main(String[] args) throws InterruptedException {

		while (true) {
			System.out.println("------ STARTING A CHECK OF FEEDS ------");
			List<Node> feeds = allocateFeedsToNodes(NodeName.BRANDON);

			// Iterate List and Get Feed
			for (int x = 0; x < feeds.size(); x++) {
				String feed = feeds.get(x).getURL();
				String category = feeds.get(x).getCategory();
				List<FeedInfo> articles = FeedUtils.getFeedReturnNewArticles(
						feed, category);
				// Buffer time between rss feed request and page request
				TimeUnit.SECONDS.sleep(10);
				for (int y = 0; y < articles.size(); y++) {
					ProcessFile.processArticle(articles.get(y));
					// Wait to not anger the Internet gods
					TimeUnit.SECONDS.sleep(10);
				}

				System.out.println("Completed Check on Feed:" + feed);
				// Wait to not anger the Internet gods
				TimeUnit.SECONDS.sleep(10);
			}

			// Now Sleep for 30min and restart
			System.out.println("------ GOING TO SLEEP FOR 30MIN ------");
			TimeUnit.MINUTES.sleep(30);
		}
	}
}
