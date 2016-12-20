package dk.shx.model.search;

import java.io.Serializable;

public class NameValuePair<T> implements Serializable {
	private String name;
	private T value;
	
	public NameValuePair() {
	}
	
	public NameValuePair(String name, T value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
}