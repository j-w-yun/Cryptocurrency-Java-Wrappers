package org.jaewanyun.wrapper.poloniex.data;

import java.util.ArrayList;

/**
 * Order book data
 */
public class OrderBook {

	public String currencyPair;
	public ArrayList<Order> asks;
	public ArrayList<Order> bids;
	public boolean isFrozen;
	public long seq;

	public OrderBook(String currencyPair) {
		this(currencyPair, new ArrayList<>(), new ArrayList<>(), false, 0);
	}

	public OrderBook(String currencyPair, boolean isFrozen, long seq) {
		this(currencyPair, new ArrayList<>(), new ArrayList<>(), isFrozen, seq);
	}

	public OrderBook(String currencyPair, ArrayList<Order> asks, ArrayList<Order> bids, boolean isFrozen, long seq) {
		this.currencyPair = currencyPair;
		this.asks = asks;
		this.bids = bids;
		this.isFrozen = isFrozen;
		this.seq = seq;
	}

	public static class Order {

		public double price;
		public double amount;

		public Order() {}

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
			sb.append("{\"" + currencyPair + "\":\n");

		sb.append("{\"asks\":[");
		sb.append("\n");
		for(Order askOrder : asks) {
			sb.append(askOrder.toString());
			sb.append("\n");
		}
		sb.append("]}");
		sb.append("\n");

		sb.append("{\"bids\":[");
		sb.append("\n");
		for(Order bidOrder : bids) {
			sb.append(bidOrder.toString());
			sb.append("\n");
		}
		sb.append("]}");
		sb.append("\n");

		sb.append("{\"isFrozen\":" + isFrozen + "}");
		sb.append("\n");

		sb.append("{\"seq\":" + seq + "}");

		if(currencyPair != null)
			sb.append("\n}");

		return sb.toString();

	}

}
