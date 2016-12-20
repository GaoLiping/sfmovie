package dk.shx.model.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CombinedAutocompleteResult implements Serializable {
	private AutocompleteQuery q = null;
	private List<NameValuePair<AutocompleteResult>> results = new ArrayList<NameValuePair<AutocompleteResult>>();
	private String error = null;

	public AutocompleteQuery getQ() {
		return q;
	}

	public void setQ(AutocompleteQuery q) {
		this.q = q;
	}

	public List<NameValuePair<AutocompleteResult>> getResults() {
		return results;
	}

	public void setResults(List<NameValuePair<AutocompleteResult>> results) {
		this.results = results;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}