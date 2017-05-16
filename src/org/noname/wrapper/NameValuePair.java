package org.noname.wrapper;

public class NameValuePair<S,T> {

	private S name;
	private T value;

	@SuppressWarnings("unused")
	private NameValuePair() {throw new UnsupportedOperationException();}

	public NameValuePair(S name, T value) {
		this.name = name;
		this.value = value;
	}

	public S getName() {
		return name;
	}

	public void setName(S name) {
		this.name = name;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

}
