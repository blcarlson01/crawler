package feed;

public enum NodeName {
	BRANDON(1), TOM(2);

	private Integer nodeName;

	private NodeName(Integer n) {
		nodeName = n;
	}

	public Integer getNodeName() {
		return nodeName;
	}
}
