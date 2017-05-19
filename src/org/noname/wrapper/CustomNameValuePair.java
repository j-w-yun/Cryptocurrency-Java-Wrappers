package org.noname.wrapper;

/**
 * @author Jaewan Yun
 * @author Zachary A. Hankinson
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
			return name + ":" + value;
		else
			return name.toString();
	}

}
