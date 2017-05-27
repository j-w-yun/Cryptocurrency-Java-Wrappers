package org.jaewanyun.wrapper.google_trends.data;

import java.util.ArrayList;

/**
 * Trend data
 */
public class Trend {

	public ArrayList<String> dates;
	public ArrayList<Double[]> values;
	public String[] queryNames;

	public Trend(String[] queryNames) {
		this.dates = new ArrayList<>();
		this.values = new ArrayList<>();
		this.queryNames = queryNames;
	}

	public Trend(ArrayList<String> dates, ArrayList<Double[]> values, String[] queryNames) {
		this.dates = dates;
		this.values = values;
		this.queryNames = queryNames;
	}

	public void addData(String data) {

		this.addDate(data);
		this.addValues(data);

	}

	private void addDate(String data) {

		/*
		 * Store dates in format "DAY, MONTH dd, yyyy"
		 */
		//		int beginIndex = data.indexOf("),\"f\":\"") + 7;
		//		int endIndex = data.indexOf(" 20") + 5;

		/*
		 * Store dates in format "yyyy,(mm-1),(dd-1)"
		 */
		int beginIndex = data.indexOf("(") + 1;
		int endIndex = data.indexOf(")");

		dates.add(data.substring(beginIndex, endIndex));

	}

	private void addValues(String data) {

		Double[] value = new Double[queryNames.length];

		String[] splitStrings = data.split(",");

		int index = 0;
		for(int j = 2; j < splitStrings.length; j++) {
			if(splitStrings[j].startsWith("{\"v\":")) {
				String valueString = splitStrings[j].substring(5, splitStrings[j].length());
				value[index++] = new Double(valueString);
			}
		}

		this.values.add(value);

	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		for(int j = 0; j < dates.size(); j++) {
			sb.append(dates.get(j) + " : ");
			for(int k = 0; k < values.get(j).length; k++) {
				sb.append(queryNames[k] + ":" + values.get(j)[k] + " ");
			}
			sb.append(System.lineSeparator());
		}

		return sb.toString();

	}

}
