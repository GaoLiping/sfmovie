package dk.shx.model.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QueryResult implements Serializable {

	private Query q;
	private int totalHits;
	private List<Hit> hits = new ArrayList<Hit>();
	private String error = "";
	public Query getQ() {
		return q;
	}
	public void setQ(Query q) {
		this.q = q;
	}
	public int getTotalHits() {
		return totalHits;
	}
	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}
	public List<Hit> getHits() {
		return hits;
	}
	public void setHits(List<Hit> hits) {
		this.hits = hits;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
}