package org.noname.wrapper;

public class TradeData {

	public long globalTradeID;
	public long tradeID;
	public String date;
	public String type;
	public double rate;
	public double amount;
	public double total;

	public TradeData() {}

	public TradeData(long globalTradeID, long tradeID, String date, String type, double rate, double amount, double total) {
		this.globalTradeID = globalTradeID;
		this.tradeID = tradeID;
		this.date = date;
		this.type = type;
		this.rate = rate;
		this.amount = amount;
		this.total = total;
	}

	@Override
	public String toString() {
		return "{\"globalTradeID\":" + globalTradeID +
				",\"tradeID\":" + tradeID +
				",\"date\":" + date +
				",\"type\":" + type +
				",\"rate\":" + rate +
				",\"amount\":" + amount +
				",\"total\":" + total + "}";
	}

}
