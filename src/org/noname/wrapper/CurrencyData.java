package org.noname.wrapper;

public class CurrencyData {

	public String currency;
	public int id;
	public String name;
	public double txFee;
	public int minConf;
	public String depositAddress;
	public boolean disabled;
	public boolean delisted;
	public boolean frozen;

	public CurrencyData() {}

	public CurrencyData(String currency, int id, String name, double txFee, int minConf, String depositAddress, boolean disabled, boolean delisted, boolean frozen) {
		this.currency = currency;
		this.id = id;
		this.name = name;
		this.txFee = txFee;
		this.minConf = minConf;
		this.depositAddress = depositAddress;
		this.disabled = disabled;
		this.delisted = delisted;
		this.frozen = frozen;
	}

	@Override
	public String toString() {
		return "{\"" + currency + "\":{" +
				"\"id\":" + id +
				",\"name\":" + name +
				",\"txFee\":" + txFee +
				",\"minConf\":" + minConf +
				",\"depositAddress\":" + depositAddress +
				",\"disabled\":" + disabled +
				",\"delisted\":" + delisted +
				",\"frozen\":" + frozen + "}}";
	}

}
