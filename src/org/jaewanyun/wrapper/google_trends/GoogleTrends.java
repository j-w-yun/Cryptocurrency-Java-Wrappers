package org.jaewanyun.wrapper.google_trends;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.jaewanyun.wrapper.google_trends.data.Trend;

/**
 * Unsupported operations by Google. Developer is not liable to any consequence brought by the use of this program
 * Refer to https://www.google.com/policies/terms/
 */
public class GoogleTrends {

	private static final String PUBLIC_URL = "https://trends.google.com/trends/fetchComponent?";
	private static CloseableHttpClient httpClient;

	// Increase query rate
	private static String cookieString = null;

	static {

		try {
			httpClient = getCookieHttpClient();

		} catch (IOException e) {
			System.err.println("Failed cookie retrieval");
			System.err.println("Switching to default connection");
		}

		if(httpClient == null)
			httpClient = HttpClients.createDefault();

	}


	/**
	 * Get a cookie from Google and place it in a ClosableHttpClient
	 * @return An instance of CloseableHttpClient that is critical to this Class
	 * @throws IOException When URL connection is improperly formed
	 */
	private static CloseableHttpClient getCookieHttpClient() throws IOException {

		/*
		 * Tutorial: http://www.hccp.org/java-net-cookie-how-to.html
		 */
		URL myUrl = new URL("https://trends.google.com");
		URLConnection urlConn = myUrl.openConnection();
		urlConn.connect();

		String headerName = null;
		for(int i=1; (headerName = urlConn.getHeaderFieldKey(i)) != null; i++)
			if(headerName.equals("Set-Cookie"))
				cookieString = urlConn.getHeaderField(i);
		cookieString = cookieString.substring(0, cookieString.indexOf(";"));

		/*
		 * Tutorial: http://hc.apache.org/httpcomponents-client-ga/tutorial/html/statemgmt.html#d5e499
		 */
		CookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie cookie = new BasicClientCookie("Cookie", cookieString);
		cookie.setDomain(".google.com");
		cookie.setPath("/trends");
		cookieStore.addCookie(cookie);
		return HttpClients.custom().setDefaultCookieStore(cookieStore).build();

	}

	/**
	 * Get all trend data; interval is in months. Up to five keywords may be entered. Calling this frequently will result in denied query
	 * @param keywords Keywords to query. Up to five may be queried at a time
	 * @return Trend data
	 */
	public static Trend getTrendAll(String[] keywords) {

		StringBuilder sb = new StringBuilder();
		sb.append(PUBLIC_URL);

		StringBuilder param_q = new StringBuilder();
		for(String each : keywords) {
			param_q.append(each);
			param_q.append(',');
		}
		param_q.setLength(param_q.length()-1);

		append(sb, "q", param_q.toString());
		append(sb, "cid", "TIMESERIES_GRAPH_0");
		append(sb, "export", "3");
		append(sb, "hl", "en-US");

		HttpPost post = new HttpPost(sb.toString());
		post.addHeader("Cookie", cookieString);

		String response = null;

		try(CloseableHttpResponse httpResponse = httpClient.execute(post)) {
			HttpEntity httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);
			EntityUtils.consume(httpEntity);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return parseResponse(response, keywords);

	}

	/**
	 * Get trend data; interval is in months. Up to five keywords may be entered. Calling this frequently will result in denied query
	 * @param keywords Keywords to query. Up to five may be queried at a time
	 * @param startDate Start date. Format is in "mm/yyyy"
	 * @param deltaMonths Time, in months, from start date for which to retrieve data
	 * @return Trend data
	 */
	public static Trend getTrend(String[] keywords, String startDate, int deltaMonths) {

		StringBuilder sb = new StringBuilder();
		sb.append(PUBLIC_URL);

		StringBuilder param_q = new StringBuilder();
		for(String each : keywords) {
			param_q.append(each);
			param_q.append(',');
		}
		param_q.setLength(param_q.length()-1);

		append(sb, "q", param_q.toString());
		append(sb, "cid", "TIMESERIES_GRAPH_0");
		append(sb, "export", "3");
		append(sb, "date", startDate + "+" + deltaMonths + "m");
		append(sb, "hl", "en-US");

		HttpPost post = new HttpPost(sb.toString());
		post.addHeader("Cookie", cookieString);

		String response = null;

		try(CloseableHttpResponse httpResponse = httpClient.execute(post)) {
			HttpEntity httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);
			EntityUtils.consume(httpEntity);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return parseResponse(response, keywords);

	}

	/*
	 * URL generation helper
	 */
	private static void append(StringBuilder appendTo, String appendWith_1, String appendWith_2) {

		if(appendTo.charAt(appendTo.length()-1) != '?')
			appendTo.append('&');
		appendTo.append(appendWith_1);
		appendTo.append("=");
		appendTo.append(appendWith_2);

	}

	/*
	 * Parse the output
	 */
	private static Trend parseResponse(String response, String[] keywords) {

		String[] splitStrings = response.split("\"c\":");

		Trend trendData = new Trend(keywords);

		for(String each : splitStrings)
			if(each.contains("new Date"))
				trendData.addData(each);

		return trendData;

	}

}
