package org.noname.wrapper;

/**
 * @author Jaewan Yun
 * @author Zachary A. Hankinson
 */
public class TickerData {

	public String currencyPair;
	public int id;
	public double last;
	public double lowestAsk;
	public double highestBid;
	public double percentChange;
	public double baseVolume;
	public double quoteVolume;
	public boolean isFrozen;
	public double high24hr;
	public double low24hr;

	@SuppressWarnings("unused")
	private TickerData() {throw new UnsupportedOperationException();}

	public TickerData(String currencyPair, int id, double last, double lowestAsk,
			double highestBid, double percentChange, double baseVolume, double quoteVolume,
			boolean isFrozen, double high24hr, double low24hr) {

		this.currencyPair = currencyPair;
		this.id = id;
		this.last = last;
		this.lowestAsk = lowestAsk;
		this.highestBid = highestBid;
		this.percentChange = percentChange;
		this.baseVolume = baseVolume;
		this.quoteVolume = quoteVolume;
		this.isFrozen = isFrozen;
		this.high24hr = high24hr;
		this.low24hr = low24hr;

	}

	@Override
	public String toString() {

		return "{\"" + currencyPair + "\":{" +
				"\"id\":" + id + "," +
				"\"last\":" + last + "," +
				"\"lowestAsk\":" + lowestAsk + "," +
				"\"highestBid\":" + highestBid + "," +
				"\"percentChange\":" + percentChange + "," +
				"\"baseVolume\":" + baseVolume + "," +
				"\"quoteVolume\":" + quoteVolume + "," +
				"\"isFrozen\":" + isFrozen + "," +
				"\"high24hr\":" + high24hr + "," +
				"\"low24hr\":" + low24hr + "}";

	}

}
