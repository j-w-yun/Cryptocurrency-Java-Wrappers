package org.noname.wrapper;

public class VolumeData {

	public String currencyPair;
	public String currencyA;
	public double volumeA;
	public String currencyB;
	public double volumeB;

	public VolumeData(String currencyPair, String currencyA, double volumeA, String currencyB, double volumeB) {
		this.currencyPair = currencyPair;
		this.currencyA = currencyA;
		this.volumeA = volumeA;
		this.currencyB = currencyB;
		this.volumeB = volumeB;
	}

	@Override
	public String toString() {

		return "{\"" + currencyPair + "\":{" +
				"\"" + currencyA + "\":" + volumeA + "," +
				"\"" + currencyB + "\":" + volumeB + "}";

	}

}
