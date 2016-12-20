package dk.shx.model.search;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AutocompleteQuery implements Serializable {
	private int rows = 5; // nr. of rows to return
	private String q = null; 
	private List<String> fls = null; // fields
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public List<String> getFls() {
		return fls;
	}
	public void setFls(List<String> fls) {
		this.fls = fls;
	}
}