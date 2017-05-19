package org.noname.wrapper;

import java.util.ArrayList;

public class LoanOrderData {

	public String currency;
	public ArrayList<Order> offers;
	public ArrayList<Order> demands;

	public LoanOrderData(String currency) {
		this(currency, new ArrayList<>(), new ArrayList<>());
	}

	public LoanOrderData(String currency, ArrayList<Order> offers, ArrayList<Order> demands) {
		this.currency = currency;
		this.offers = offers;
		this.demands = demands;
	}

	public static class Order {

		public int rangeMin;
		public int rangeMax;
		public double rate;
		public double amount;

		public Order() {}

		public Order(int rangeMin, int rangeMax, double rate, double amount) {
			this.rangeMin = rangeMin;
			this.rangeMax = rangeMax;
			this.rate = rate;
			this.amount = amount;
		}

		@Override
		public String toString() {
			return "[\"rate\"" + rate + "," +
					"\"amount\":" + amount + "," +
					"\"rangeMin\":" + rangeMin + "," +
					"\"rangeMax\":" + rangeMax + "]";
		}

	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		if(currency != null)
			sb.append("{\"" + currency + "\":\n");

		sb.append("{\"offers\":[");
		sb.append("\n");
		for(Order offerOrder : offers) {
			sb.append(offerOrder.toString());
			sb.append("\n");
		}
		sb.append("]}");
		sb.append("\n");

		sb.append("{\"demands\":[");
		sb.append("\n");
		for(Order demandOrder : demands) {
			sb.append(demandOrder.toString());
			sb.append("\n");
		}
		sb.append("]}");

		if(currency != null)
			sb.append("\n}");

		return sb.toString();

	}

}
