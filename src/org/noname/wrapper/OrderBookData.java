package org.noname.wrapper;

import java.util.ArrayList;

public class OrderBookData {

	public String currencyPair;
	public ArrayList<Order> asks;
	public ArrayList<Order> bids;
	public boolean isFrozen;
	public long seq;

	public OrderBookData(String currencyPair, ArrayList<Order> asks, ArrayList<Order> bids, boolean isFrozen, long seq) {
		this.currencyPair = currencyPair;
		this.asks = asks;
		this.bids = bids;
		this.isFrozen = isFrozen;
		this.seq = seq;
	}

	public static class Order {

		public double price;
		public double amount;

		public Order(double price, double amount) {
			this.price = price;
			this.amount = amount;
		}

		@Override
		public String toString() {

			return "[\"" + price + "\"," + amount + "]";

		}

	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		if(currencyPair != null)
			sb.append("{\"" + currencyPair + "\":");

		sb.append("{\"asks\":[");
		for(Order askOrder : asks) {
			sb.append(askOrder.toString());
			sb.append(",");
		}
		sb.append("\"bids\":[");
		for(Order bidOrder : bids) {
			sb.append(bidOrder.toString());
			sb.append(",");
		}

		sb.append("\"isFrozen\":" + isFrozen + ",");
		sb.append("\"seq\":" + seq + "}");

		if(currencyPair != null)
			sb.append("}");

		return sb.toString();

	}

}
