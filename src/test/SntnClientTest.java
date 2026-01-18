package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import util.HttpClient;
import util.SntnClient;

public class SntnClientTest {
	@Test
	public void parsesAppKeyAndTimestamp() {
		HttpClient stub = (url, mimeType) -> {
			if (url.endsWith("key/0245-MQL")) {
				return "{\"appKey\":\"TOKEN-123\"}";
			}
			return "{\"timeStamp\":\"2024-01-01T12:00:00Z\"}";
		};

		SntnClient client = new SntnClient("http://example.com/", stub);
		assertEquals("TOKEN-123", client.fetchAppKey("0245-MQL"));
		assertEquals("2024-01-01T12:00:00Z", client.fetchTimestamp());
	}
}
