package in.savegenie.savegenie.internetcommunication;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class AppHttpClient {
	private HttpClient httpClient;
	private static AppHttpClient appHttpClient;

	/*
	 * A private Constructor prevents any other class from instantiating.
	 */

	private AppHttpClient() {
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 15000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);
		int timeoutSocket = 15000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		httpClient = new DefaultHttpClient(httpParameters);
	}

	public static AppHttpClient getAppHttpClientInstance() {
		if (appHttpClient == null) {
			appHttpClient = new AppHttpClient();
		}
		return appHttpClient;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

}
