package org.jaewanyun.wrapper.examples;

import org.jaewanyun.wrapper.poloniex.Poloniex;

/**
 * Example methods that can be called from Poloniex Class
 */
public class PoloniexExample {

	public static void main(String[] args) {

		/*
		 * Public API methods
		 */

		//		TickerData[] ticker = Poloniex.PublicApi.ticker();
		//		for(int j = 0; j < ticker.length; j++) {
		//			System.out.println(ticker[j]);
		//		}

		//		VolumeData volume = Poloniex.PublicApi.volume();
		//		System.out.println(volume);

		//		OrderBookData orderBookData = Poloniex.PublicApi.orderBook("BTC_ETH");
		//		System.out.println(orderBookData);

		//		OrderBookData[] orderBookDatas = Poloniex.PublicApi.orderBookAll();
		//		for(int j = 0; j < orderBookDatas.length; j++) {
		//			System.out.println(orderBookDatas[j]);
		//		}

		//		TradeData[] tradeDatas_1 = Poloniex.PublicApi.tradeHistory("BTC_ETH");
		//		for(int j = 0; j < tradeDatas_1.length; j++) {
		//			System.out.println(tradeDatas_1[j]);
		//		}

		//		TradeData[] tradeDatas_2 = Poloniex.PublicApi.tradeHistory(
		//				Time.unixTimestamp(2017, 5, 18, 18, 0, "UTC-5:00"), // May 18, 2017 06:00 PM
		//				Time.unixTimestamp(2017, 5, 18, 20, 30, "UTC-5:00"), // May 18, 2017 08:30 PM
		//				"BTC_ETH");
		//		for(int j = 0; j < tradeDatas_2.length; j++) {
		//			System.out.println(tradeDatas_2[j]);
		//		}

		//		ChartData[] chartDatas = Poloniex.PublicApi.chartData(
		//				Time.unixTimestamp(2017, 5, 1, 10, 0, "UTC-5:00"), // May 1, 2017 10:00 AM
		//				Time.unixTimestamp(2017, 5, 8, 20, 30, "UTC-5:00"), // May 8, 2017 08:30 PM
		//				"BTC_ETH",
		//				900);
		//		for(int j = 0; j < chartDatas.length; j++) {
		//			System.out.println(chartDatas[j]);
		//		}

		//		CurrencyData[] currencyDatas = Poloniex.PublicApi.currencies();
		//		for(int j = 0; j < currencyDatas.length; j++) {
		//			System.out.println(currencyDatas[j]);
		//		}

		//		LoanOrderData loanOrderData = Poloniex.PublicApi.loanOrders("BTC");
		//		System.out.println(loanOrderData);



		/*
		 * Trading API methods
		 */

		// Trading API requires your API key and private key tied to your Poloniex account. https://poloniex.com/apiKeys
		//		String secretKey = "";
		//		String apiKey = "";
		//		Poloniex.Trade my = new Poloniex.TradeApi(secretKey, apiKey);

		// Trading API is read from res/api_keys.txt if no constructor parameter is specified
		Poloniex.TradeApi my = new Poloniex.TradeApi();

		//		my.balances();

	}

}
