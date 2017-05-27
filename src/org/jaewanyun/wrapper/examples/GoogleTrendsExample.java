package org.jaewanyun.wrapper.examples;

import org.jaewanyun.wrapper.google_trends.GoogleTrends;
import org.jaewanyun.wrapper.google_trends.data.Trend;

/**
 * Example methods that can be called from GoogleTrends Class
 */
public class GoogleTrendsExample {

	private static String[] keywords = {"eth", "btc", "xmr", "ltc", "dcr"};

	public static void main(String[] args) {

		// Get all trends from 2004 to present
		Trend trendData = GoogleTrends.getTrendAll(keywords);
		System.out.println(trendData);

		// Get all trends from May 2015 to October (May+3mo) 2015
		trendData = GoogleTrends.getTrend(keywords, "05/2014", 36);
		System.out.println(trendData);

	}

}
