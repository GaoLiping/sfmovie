package dk.shx.model.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AutocompleteResult implements Serializable {

	private List<String> values = new ArrayList<String>();

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}
}