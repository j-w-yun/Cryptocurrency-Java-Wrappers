package org.jaewanyun.wrapper;

/**
 * Name-value data
 */
public class CustomNameValuePair<S,T> {

	private S name;
	private T value;

	public CustomNameValuePair() {}

	public CustomNameValuePair(S name, T value) {
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

	@Override
	public String toString() {
		if(value != null)
			return Utility.padRight(name.toString(), 25, '.') + ":" + value;
		else
			return name.toString();
	}

	@Override
	public boolean equals(Object obj) {throw new UnsupportedOperationException();}

	@Override
	public int hashCode() {throw new UnsupportedOperationException();}

}
