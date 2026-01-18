package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JsonFieldExtractor {
	private JsonFieldExtractor() {
	}

	public static String extractStringField(String json, String fieldName) {
		Pattern pattern = Pattern.compile("\"" + Pattern.quote(fieldName) + "\"\\s*:\\s*\"([^\"]+)\"");
		Matcher matcher = pattern.matcher(json);
		if (!matcher.find()) {
			throw new IllegalArgumentException("JSON field not found: " + fieldName);
		}
		return matcher.group(1);
	}
}
