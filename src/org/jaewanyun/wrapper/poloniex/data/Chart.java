package org.jaewanyun.wrapper.poloniex.data;

/**
 * Chart data
 */
public class Chart {

	public long date;
	public double high;
	public double low;
	public double open;
	public double close;
	public double volume;
	public double quoteVolume;
	public double weightedAverage;

	public Chart() {}

	public Chart(long date, double high, double low, double open, double close, double volume, double quoteVolume, double weightedAverage) {
		this.date = date;
		this.high = high;
		this.low = low;
		this.open = open;
		this.close = close;
		this.volume = volume;
		this.quoteVolume = quoteVolume;
		this.weightedAverage = weightedAverage;
	}

	@Override
	public String toString() {
		return "{\"date\":" + date + "," +
				"\"high\":" + high + "," +
				"\"low\":" + low + "," +
				"\"open\":" + open + "," +
				"\"close\":" + close + "," +
				"\"volume\":" + volume + "," +
				"\"quoteVolume\":" + quoteVolume + "," +
				"\"weightedAverage\":" + weightedAverage + "}";
	}

}
