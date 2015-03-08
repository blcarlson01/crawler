package feed;

public class Node {
	private String url;
	private NodeName node;
	private String category;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Node(String url, NodeName node, String category) {
		super();
		this.url = url;
		this.node = node;
		this.category = category;
	}

	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		url = this.url;
	}

	public Integer getNode() {
		return node.getNodeName();
	}

	public void setNode(NodeName node) {
		this.node = node;
	}
}
