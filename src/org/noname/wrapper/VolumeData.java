package org.noname.wrapper;

import java.util.ArrayList;

public class VolumeData {

	public ArrayList<Pair> pairs;
	public ArrayList<Total> totals;

	public VolumeData() {
		pairs = new ArrayList<>();
		totals = new ArrayList<>();
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		for(Pair each : pairs) {
			sb.append(each.toString() + "\n");
		}

		for(Total each : totals) {
			sb.append("\n" + each.toString());
		}

		return sb.toString();

	}

	public static class Pair {

		public String currencyPair;
		public String currencyA;
		public double volumeA;
		public String currencyB;
		public double volumeB;

		public Pair(String currencyPair, String currencyA, double volumeA, String currencyB, double volumeB) {
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
					"\"" + currencyB + "\":" + volumeB + "}}";
		}

	}

	public static class Total {

		public String name;
		public double totalVolume;

		public Total(String name, double totalVolume) {
			this.name = name;
			this.totalVolume = totalVolume;
		}

		@Override
		public String toString() {
			return name + ":" + totalVolume;
		}

	}

}
