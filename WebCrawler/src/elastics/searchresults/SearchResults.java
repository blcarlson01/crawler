package elastics.searchresults;
import java.util.List;

public class SearchResults {
	public HitInfo hits;

	public class HitInfo {
		public int total;
		public float max_score;
		public List<Hit> hits;
	}
}