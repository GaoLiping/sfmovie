package dk.shx.model.search;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Query implements Serializable {

	private int start = 0; // start row
	private int rows = 5; // nr. of rows to return
	private List<NameValuePair<String>> fq = null; // filter query
	private List<String> fl = null; // return fields
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public List<NameValuePair<String>> getFq() {
		return fq;
	}
	public void setFq(List<NameValuePair<String>> fq) {
		this.fq = fq;
	}
	public List<String> getFl() {
		return fl;
	}
	public void setFl(List<String> fl) {
		this.fl = fl;
	}
}