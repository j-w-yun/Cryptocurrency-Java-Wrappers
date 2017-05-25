package org.jaewanyun.wrapper.etherscan.data;

import java.math.BigInteger;

import org.jaewanyun.wrapper.CustomNameValuePair;

/**
 * Balance data
 */
public class Balance implements AutoDataAdder {

	public String address;
	public BigInteger balance;

	public Balance() {}

	public Balance(String address, BigInteger balance) {
		this.address = address;
		this.balance = balance;
	}

	@Override
	public void addData(CustomNameValuePair<?, ?> data) {

		if(data.getName().toString().equals("address"))
			this.address = data.getValue().toString();
		else if(data.getName().toString().equals("balance"))
			this.balance = new BigInteger(data.getValue().toString());

	}

	@Override
	public String toString() {
		return "{\"account\":" + address + ",\"balance\":" + balance.toString() + "}";
	}

}
