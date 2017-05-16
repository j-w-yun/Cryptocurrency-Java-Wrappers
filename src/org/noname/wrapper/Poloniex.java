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
		 * @param currencyPair Pair of cryptocurrencies (e.g. BTC_ETH)
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
		 * @param currencyPair Pair of cryptocurrencies (e.g. BTC_ETH)
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
		 * @param currencyPair Pair of cryptocurrencies (e.g. BTC_ETH)
		 */
		public static void tradeHistory(long unixStartDate, long unixEndDate, String currencyPair) {
			// TODO
		}

		/**
		 * Returns candlestick chart data for the specified date range for the data returned in UNIX timestamps
		 * @param unixStartDate Start date in UNIX timestamp
		 * @param unixEndDate End date in UNIX timestamp
		 * @param currencyPair Pair of cryptocurrencies (e.g. BTC_ETH)
		 */
		public static void chartData(long unixStartDate, long unixEndDate, String currencyPair) {
			// TODO
		}

		/**
		 * Returns information about currencies
		 */
		public static void currencies() {

			HttpGet get = new HttpGet(PUBLIC_URL + "?command=returnCurrencies");
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
		 * Returns the list of loan offers and demands for a given currency
		 * @param currency Cryptocurrency name in symbol (e.g. BTC)
		 */
		public static void loanOrders(String currency) {

			HttpGet get = new HttpGet(PUBLIC_URL + "?command=returnLoanOrders&currency=" + currency);
			String response = null;

			try {
				CloseableHttpResponse httpResponse = httpClient.execute(get);
				response = EntityUtils.toString(httpResponse.getEntity());
			} catch (IOException e) {
				e.printStackTrace();
			}

			parse(response);

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
		public void balances() {

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

		/**
		 * Returns all of your balances, including available balance, balance on orders, and the estimated BTC value of your balance. By default, this call is limited to your exchange account
		 */
		public void completeBalances() {
			// TODO
		}

		/**
		 * Returns all of your deposit addresses
		 */
		public void depositAddresses() {
			// TODO
		}

		/**
		 * Generates a new deposit address for the currency specified by the currency parameter
		 * @param currency Currency for which to generate a new deposit (e.g. BTC)
		 */
		public void generateNewAddress(String currency) {
			// TODO
		}

		/**
		 * Returns your deposit and withdrawal history within a range, specified by the "start" and "end" parameters
		 * @param unixStartDate Start date in UNIX timestamp
		 * @param unixEndDate End date in UNIX timestamp
		 */
		public void depositWithdrawals(long unixStartDate, long unixEndDate) {
			// TODO
		}

		/**
		 * Returns your open orders for a given market, specified by the "currencyPair" parameter
		 * @param currencyPair Pair of cryptocurrencies (e.g. BTC_ETH)
		 */
		public void openOrders(String currencyPair) {
			// TODO
		}

		/**
		 * Returns your open orders for all markets
		 */
		public void openOrdersAll() {
			// TODO
		}

		/**
		 * Returns your trade history for a given market, specified by the "currencyPair" parameter
		 * @param currencyPair Pair of cryptocurrencies (e.g. BTC_ETH)
		 */
		public void tradeHistory(String currencyPair) {
			// TODO
		}

		/**
		 * Returns your trade history for all markets
		 */
		public void tradeHistoryAll() {
			// TODO
		}

		/**
		 * Returns all trades involving a given order, specified by the "orderNumber" parameter
		 * @param orderNumber Order number
		 */
		public void orderTrades(String orderNumber) {
			// TODO
		}

		/**
		 * Places a limit buy order in a given market. Required parameters are "currencyPair", "rate", and "amount". If successful, the method will return the order number
		 * @param currencyPair Pair of cryptocurrencies (e.g. BTC_ETH)
		 * @param rate Rate at which to buy
		 * @param amount Amount to buy
		 * @param options You may optionally set this to "fillOrKill", "immediateOrCancel", "postOnly"; otherwise leave as null. A fill-or-kill order will either fill in its entirety or be completely aborted. An immediate-or-cancel order can be partially or completely filled, but any portion of the order that cannot be filled immediately will be canceled rather than left on the order book. A post-only order will only be placed if no portion of it fills immediately; this guarantees you will never pay the taker fee on any part of the order that fills
		 */
		public void buy(String currencyPair, double rate, double amount, String options) {
			// TODO
		}

		/**
		 * Places a sell order in a given market. Required parameters are "currencyPair", "rate", and "amount"
		 * @param currencyPair Pair of cryptocurrencies (e.g. BTC_ETH)
		 * @param rate Rate at which to sell
		 * @param amount Amount to sell
		 */
		public void sell(String currencyPair, double rate, double amount) {
			// TODO
		}

		/**
		 * Cancels an order you have placed in a given market. Required parameter is "orderNumber"
		 * @param orderNumber Order number
		 * @return If successful, the method will return true
		 */
		public boolean cancelOrder(String orderNumber) {
			// TODO
			return false;
		}

		/**
		 * Cancels an order and places a new one of the same type in a single atomic transaction, meaning either both operations will succeed or both will fail. Required parameters are "orderNumber" and "rate"; you may optionally specify "amount" if you wish to change the amount of the new order
		 * @param orderNumber Order number
		 * @param rate Rate
		 * @param amount Optional parameter. Default is null
		 */
		public void moveOrder(String orderNumber, double rate, Double amount) {
			// TODO
		}

		/**
		 * Immediately places a withdrawal for a given currency, with no email confirmation. In order to use this method, the withdrawal privilege must be enabled for your API key. Required parameters are "currency", "amount", and "address"
		 * @param currency Currency to withdraw (e.g. BTC)
		 * @param amount Amount to withdraw
		 * @param address Address into which to withdraw
		 */
		public void withdraw(String currency, double amount, String address) {
			// TODO
		}

		/**
		 * If you are enrolled in the maker-taker fee schedule, returns your current trading fees and trailing 30-day volume in BTC. This information is updated once every 24 hours
		 */
		public void feeInfo() {
			// TODO
		}

		/**
		 * Returns your balances sorted by account. You may optionally specify the "account" parameter if you wish to fetch only the balances of one account. Please note that balances in your margin account may not be accessible if you have any open margin positions or orders
		 * @param account Optional parameter. Default is null
		 */
		public void availableAccountBalances(String account) {
			// TODO
		}

		/**
		 * Returns your current tradable balances for each currency in each market for which margin trading is enabled. Please note that these balances may vary continually with market conditions
		 */
		public void tradableBalances() {
			// TODO
		}

		/**
		 * Transfers funds from one account to another (e.g. from your exchange account to your margin account). Required parameters are "currency", "amount", "fromAccount", and "toAccount"
		 * @param currency Currency to transfer (e.g. BTC)
		 * @param amount Amount to transfer
		 * @param fromAccount Account from which to transfer
		 * @param toAccount Account into which to transfer
		 */
		public void transferBalance(String currency, double amount, String fromAccount, String toAccount) {
			// TODO
		}

		/**
		 * Returns a summary of your entire margin account. This is the same information you will find in the Margin Account section of the Margin Trading page, under the Markets list
		 */
		public void marginAccountSummary() {
			// TODO
		}

		/**
		 * Places a margin buy order in a given market. Required parameters are "currencyPair", "rate", and "amount". You may optionally specify a maximum lending rate using the "lendingRate" parameter
		 * @param currencyPair Pair of cryptocurrencies (e.g. BTC_ETH)
		 * @param rate Rate at which to buy
		 * @param amount Amount to buy
		 * @param lendingRate Optional parameter. Default is null
		 */
		public void marginBuy(String currencyPair, double rate, double amount, Double lendingRate) {
			// TODO
		}

		/**
		 * Places a margin sell order in a given market. Required parameters are "currencyPair", "rate", and "amount"
		 * @param currencyPair Pair of cryptocurrencies (e.g. BTC_ETH)
		 * @param rate Rate at which to sell
		 * @param amount Amount to sell
		 */
		public void marginSell(String currencyPair, double rate, double amount) {
			// TODO
		}

		/**
		 * Returns information about your margin position in a given market, specified by the "currencyPair" parameter
		 * @param currencyPair Pair of cryptocurrencies (e.g. BTC_ETH)
		 */
		public void getMarginPosition(String currencyPair) {
			// TODO
		}

		/**
		 * Fetch all of your margin positions at once
		 */
		public void getMarginPositionAll() {
			// TODO
		}

		/**
		 * Closes your margin position in a given market (specified by the "currencyPair" parameter) using a market order
		 * @param currencyPair Pair of cryptocurrencies (e.g. BTC_ETH)
		 */
		public void closeMarginPosition(String currencyPair) {
			// TODO
		}

		/**
		 * Creates a loan offer for a given currency. Required parameters are "currency", "amount", "duration", "autoRenew" (0 or 1), and "lendingRate"
		 * @param currency Currency for which to create a loan offer
		 * @param amount Amount to offer
		 * @param duration Duration of loan
		 * @param autoRenew Automatically renew
		 * @param lendingRate Rate at which to lend
		 */
		public void createLoanOffer(String currency, double amount, double duration, boolean autoRenew, double lendingRate) {
			// TODO
		}

		/**
		 * Cancels a loan offer specified by the "orderNumber" POST parameter
		 * @param orderNumber Order number
		 */
		public void cancelLoanOffer(String orderNumber) {
			// TODO
		}

		/**
		 * Returns your open loan offers for each currency
		 */
		public void openLoanOffers() {
			// TODO
		}

		/**
		 * Returns your active loans for each currency
		 */
		public void activeLoans() {
			// TODO
		}

		/**
		 * Returns your lending history within a time range specified by the "start" and "end" parameters as UNIX timestamps. "limit" may also be specified to limit the number of rows returned
		 * @param unixStartDate Start date in UNIX timestamp
		 * @param unixEndDate End date in UNIX timestamp
		 * @param limit Optional parameter. Default is null
		 */
		public void lendingHistory(long unixStartDate, long unixEndDate, Integer limit) {
			// TODO
		}

		/**
		 * Toggles the autoRenew setting on an active loan, specified by the "orderNumber" parameter
		 * @param orderNumber Order number
		 * @return If successful, "message" will indicate the new autoRenew setting
		 */
		public boolean autoRenew(String orderNumber) {
			// TODO
			return false;
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

		/*
		 * 7 public API methods
		 * 5/7 complete
		 */

		//		Poloniex.Public.ticker();

		//		Poloniex.Public.volume();

		//		Poloniex.Public.orderBook("BTC_ETH");

		//		Poloniex.Public.tradeHistory("BTC_ETH");

		//		TODO: Poloniex.Public.tradeHistory(long unixStartDate, long unixEndDate, String currencyPair);

		//		TODO: Poloniex.Public.chartData(long unixStartDate, long unixEndDate, String currencyPair);

		//		Poloniex.Public.currencies();

		//		Poloniex.Public.loanOrders("BTC");

		/*
		 * 31 trading API methods
		 * 1/31 complete
		 */

		// Trading API requires your API key and private key tied to your Poloniex account. https://poloniex.com/apiKeys
		String secretKey = "";
		String apiKey = "";

		Poloniex.Trade my = new Poloniex.Trade(secretKey, apiKey);

		my.balances();

	}
}
