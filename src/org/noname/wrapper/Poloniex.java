package org.noname.wrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Wrapper for Poloniex public API and trading API. https://poloniex.com/support/api/
 * @author Jaewan Yun
 * @author Zachary A. Hankinson
 */
public class Poloniex {

	private static final String TRADING_URL = "https://poloniex.com/tradingApi";
	private static final String PUBLIC_URL = "https://poloniex.com/public";
	private static CloseableHttpClient httpClient = HttpClients.createDefault();

	/**
	 * Wrapper for Poloniex public API. According to Poloniex API documentation, there are six public methods, all of which take HTTP GET requests and return output in JSON format
	 */
	public static class Public {

		/**
		 * Returns the ticker for all markets
		 */
		public static void ticker() {

			HttpGet get = new HttpGet(PUBLIC_URL + "?command=returnTicker");
			String response = null;

			try {
				CloseableHttpResponse httpResponse = httpClient.execute(get);
				response = EntityUtils.toString(httpResponse.getEntity());
			} catch (IOException e) {
				e.printStackTrace();
			}

			parse(response);

		}

		/**
		 * Returns the 24-hour volume for all markets, plus totals for primary currencies
		 */
		public static void volume() {

			HttpGet get = new HttpGet(PUBLIC_URL + "?command=return24hVolume");
			String response = null;

			try {
				CloseableHttpResponse httpResponse = httpClient.execute(get);
				response = EntityUtils.toString(httpResponse.getEntity());
			} catch (IOException e) {
				e.printStackTrace();
			}

			parse(response);

		}

		/**
		 * Returns the order book for a given market, as well as a sequence number for use with the Push API and an indicator specifying whether the market is frozen
		 * @param currencyPair Data of a specified pair of cryptocurrencies to return
		 */
		public static void orderBook(String currencyPair) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?command=returnOrderBook&currencyPair=" + currencyPair);
			String response = null;

			try {
				CloseableHttpResponse httpResponse = httpClient.execute(get);
				response = EntityUtils.toString(httpResponse.getEntity());
			} catch (IOException e) {
				e.printStackTrace();
			}

			parse(response);

		}

		/**
		 * Returns the order book for all markets, as well as a sequence number for use with the Push API and an indicator specifying whether the market is frozen
		 */
		public static void orderBookAll() {

			HttpGet get = new HttpGet(PUBLIC_URL + "?command=returnOrderBook&currencyPair=all");
			String response = null;

			try {
				CloseableHttpResponse httpResponse = httpClient.execute(get);
				response = EntityUtils.toString(httpResponse.getEntity());
			} catch (IOException e) {
				e.printStackTrace();
			}

			parse(response);

		}

		/**
		 * Returns the past 200 trades for a given market
		 * @param currencyPair Data of a specified pair of cryptocurrencies to return
		 */
		public static void tradeHistory(String currencyPair) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?command=returnTradeHistory&currencyPair=all");
			String response = null;

			try {
				CloseableHttpResponse httpResponse = httpClient.execute(get);
				response = EntityUtils.toString(httpResponse.getEntity());
			} catch (IOException e) {
				e.printStackTrace();
			}

			parse(response);

		}

		/**
		 * Returns past trades for a given market, up to 50,000 trades in a range specified in UNIX timestamps
		 * @param unixStartDate Start date in UNIX timestamp
		 * @param unixEndDate End date in UNIX timestamp
		 * @param currencyPair Data of a specified pair of cryptocurrencies to return
		 */
		public static void tradeHistory(long unixStartDate, long unixEndDate, String currencyPair) {

		}

		/**
		 * Returns candlestick chart data for the specified date range for the data returned in UNIX timestamps
		 * @param unixStartDate Start date in UNIX timestamp
		 * @param unixEndDate End date in UNIX timestamp
		 * @param currencyPair Data of a specified pair of cryptocurrencies to return
		 */
		public static void chartData(long unixStartDate, long unixEndDate, String currencyPair) {

		}

		/**
		 * Returns information about currencies
		 */
		public static void currencies() {

		}

		/**
		 * Returns the list of loan offers and demands for a given currency
		 * @param currencyPair Data of a specified pair of cryptocurrencies to return
		 */
		public static void loanOrders(String currencyPair) {

		}

	}

	/**
	 * Wrapper for Poloniex trading API. According to Poloniex API documentation, there is a default limit of 6 calls per second
	 */
	public static class Trade {

		private String secretKey;
		private String key;

		/**
		 * Uses the HMAC-SHA512 method to sign the query's POST data
		 * @param secretKey Your secret key provided by Poloniex
		 * @param queryArgs Your query's POST data
		 * @return Signed query's POST data
		 */
		public static String sign(String secretKey, String queryArgs) {

			String sign = null;

			try {
				Mac shaMac = Mac.getInstance("HmacSHA512");
				SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA512");
				shaMac.init(keySpec);
				final byte[] macData = shaMac.doFinal(queryArgs.getBytes());
				sign = Hex.encodeHexString(macData);
			} catch (NoSuchAlgorithmException nsae) {
				nsae.printStackTrace();
			} catch (InvalidKeyException ike) {
				ike.printStackTrace();
			}

			return sign;

		}

		/**
		 * @param secretKey Your secret key provided by Poloniex
		 * @param key Your API key
		 */
		public Trade(String secretKey, String key) {
			this.secretKey = secretKey;
			this.key = key;
		}

		/**
		 * Returns all of your available balances
		 */
		public void returnBalances() {

			String nonce = String.valueOf(System.currentTimeMillis());
			String queryArgs = "command=returnBalances&nonce=" + nonce;

			String sign = sign(secretKey, queryArgs);

			HttpPost post = new HttpPost(TRADING_URL);
			try {
				post.addHeader("Key", key);
				post.addHeader("Sign", sign);
				post.setEntity(new ByteArrayEntity(queryArgs.getBytes("UTF-8")));

				List<NameValuePair> params = new ArrayList<>();
				params.add(new BasicNameValuePair("command", "returnBalances"));
				params.add(new BasicNameValuePair("nonce", nonce));
				post.setEntity(new UrlEncodedFormEntity(params));
			} catch (UnsupportedEncodingException uee) {
				uee.printStackTrace();
			}

			String response = null;
			try {
				CloseableHttpResponse httpResponse = httpClient.execute(post);
				response = EntityUtils.toString(httpResponse.getEntity());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			parse(response);

		}

	}

	/**
	 * Parses Poloniex API response in JSON format
	 * @param parse Poloniex API response in JSON format
	 */
	private static CustomNameValuePair<String, Object> parse(String parse) {

		System.out.println(parse);

		String[] splitStrings = parse.split("");

		StringBuilder sb = new StringBuilder();

		Stack<String> stack = new Stack<>();

		for(String each : splitStrings) {
			if(each.equals("{") || each.equals("[") || each.equals("}") || each.equals("]") || each.equals(",") || each.equals("\"") || each.equals(":")) {
				if(!sb.toString().equals(""))
					stack.add(sb.toString());
				if(!each.equals("\"") && !each.equals(","))
					stack.add(each);
				sb.setLength(0);
			} else {
				sb.append(each);
			}
		}

		for(String each : stack) {
			System.out.println(each);
		}

		// TODO
		return null;
	}


	public static void main(String[] args) {

		//		Poloniex.Public.ticker();

		//		Poloniex.Public.volume();

		//		Poloniex.Public.orderBook("BTC_ETH");

		//		Poloniex.Public.tradeHistory("BTC_ETH");

		/*
		 * Trading API requires your API key and private key tied to your Poloniex account
		 */
		String secretKey = "";
		String apiKey = "";

		Poloniex.Trade trade = new Poloniex.Trade(secretKey, apiKey);

		trade.returnBalances();

	}
}
