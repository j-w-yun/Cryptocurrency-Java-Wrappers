package org.jaewanyun.wrapper.google_trends;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Unsupported operations by Google. Developer is not liable to any consequence brought by the use of this program
 * Refer to https://www.google.com/policies/terms/
 */
public class GoogleTrends {

	private static final String PUBLIC_URL = "https://trends.google.com/trends/fetchComponent";
	private static CloseableHttpClient httpClient;

	static {httpClient = HttpClients.createDefault();}


	public static void timeSeries(String[] keywords) {

		HttpGet get = new HttpGet(PUBLIC_URL);

		get.addHeader("hl", "en-US");
		get.addHeader("q", "btc");
		get.addHeader("cid", "TIMESERIES_GRAPH_0");
		get.addHeader("export", "5");
		get.addHeader("w", "1200");
		get.addHeader("h", "800");


		String response = null;

		try(CloseableHttpResponse httpResponse = httpClient.execute(get)) {
			HttpEntity httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);
			EntityUtils.consume(httpEntity);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public static void main(String[] args) {

	}

}
