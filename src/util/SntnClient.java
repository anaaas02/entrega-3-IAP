package util;

import cliente_http.HTTPClient;

public class SntnClient {
	private final String baseUrl;
	private final HttpClient httpClient;

	public SntnClient(String baseUrl) {
		this(baseUrl, HTTPClient::get);
	}

	public SntnClient(String baseUrl, HttpClient httpClient) {
		this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
		this.httpClient = httpClient;
	}

	public String fetchAppKey(String vehicleId) {
		String response = httpClient.get(baseUrl + "key/" + vehicleId, "application/json");
		return JsonFieldExtractor.extractStringField(response, "appKey");
	}

	public String fetchTimestamp() {
		String response = httpClient.get(baseUrl + "timestamp", "application/json");
		return JsonFieldExtractor.extractStringField(response, "timeStamp");
	}
}
