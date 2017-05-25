package org.jaewanyun.wrapper.etherscan.data;

import java.math.BigInteger;

import org.jaewanyun.wrapper.CustomNameValuePair;

/**
 * Ether price data
 */
public class EtherPrice implements AutoDataAdder {

	public double ethbtc;
	public BigInteger ethbtc_timestamp;
	public double ethusd;
	public BigInteger ethusd_timestamp;

	public EtherPrice() {}

	public EtherPrice(double ethbtc, BigInteger ethbtc_timestamp, double ethusd, BigInteger ethusd_timestamp) {
		super();
		this.ethbtc = ethbtc;
		this.ethbtc_timestamp = ethbtc_timestamp;
		this.ethusd = ethusd;
		this.ethusd_timestamp = ethusd_timestamp;
	}

	@Override
	public void addData(CustomNameValuePair<?, ?> data) {

		if(data.getName().toString().equals("ethbtc"))
			this.ethbtc = Double.parseDouble(data.getValue().toString());
		else if(data.getName().toString().equals("ethbtc_timestamp"))
			this.ethbtc_timestamp = new BigInteger(data.getValue().toString());
		else if(data.getName().toString().equals("ethusd"))
			this.ethusd = Double.parseDouble(data.getValue().toString());
		else if(data.getName().toString().equals("ethusd_timestamp"))
			this.ethusd_timestamp = new BigInteger(data.getValue().toString());

	}

	@Override
	public String toString() {
		return "{\"ethbtc\":" + ethbtc + "," +
				"\"ethbtc_timestamp\":" + ethbtc_timestamp + "," +
				"\"ethusd\":" + ethusd + "," +
				"\"ethusd_timestamp\":" + ethusd_timestamp + "}";
	}

}
