package dk.shx.model.search;

import java.io.Serializable;
import java.util.List;

public class Hit implements Serializable {
	
	private List<NameValuePair<?>> values;
	
	public Hit() {
	}
	
	public Hit(List<NameValuePair<?>> values) {
		this.values = values;
	}

	public List<NameValuePair<?>> getValues() {
		return values;
	}

	public void setValues(List<NameValuePair<?>> values) {
		this.values = values;
	}
}
