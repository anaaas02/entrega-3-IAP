package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.LocationUpdate;

public final class MessageParsers {
	private static final Pattern KML_COORDINATES = Pattern.compile("<coordinates>([^<]+)</coordinates>");
	private static final Pattern KML_VEHICLE = Pattern.compile("<Vehicle\\s+id=\"([^\"]+)\"\\s*/?>");
	private static final Pattern GEOJSON_COORDINATES = Pattern.compile("\"coordinates\"\\s*:\\s*\\[([^\\]]+)\\]");
	private static final Pattern GEOJSON_VEHICLE = Pattern.compile("\"vehicle\"\\s*:\\s*\"([^\"]+)\"");

	private MessageParsers() {
	}

	public static LocationUpdate parseKml(String kml) {
		String coordsText = extractGroup(KML_COORDINATES, kml, "KML coordinates not found");
		String vehicleId = extractGroup(KML_VEHICLE, kml, "KML vehicle id not found");
		String[] parts = coordsText.split(",");
		if (parts.length < 2) {
			throw new IllegalArgumentException("KML coordinates malformed: " + coordsText);
		}
		double latitude = Double.parseDouble(parts[0].trim());
		double longitude = Double.parseDouble(parts[1].trim());
		return new LocationUpdate(vehicleId, latitude, longitude);
	}

	public static LocationUpdate parseGeoJson(String json) {
		String coordsText = extractGroup(GEOJSON_COORDINATES, json, "GeoJSON coordinates not found");
		String vehicleId = extractGroup(GEOJSON_VEHICLE, json, "GeoJSON vehicle id not found");
		String[] parts = coordsText.split(",");
		if (parts.length < 2) {
			throw new IllegalArgumentException("GeoJSON coordinates malformed: " + coordsText);
		}
		double latitude = Double.parseDouble(parts[0].trim());
		double longitude = Double.parseDouble(parts[1].trim());
		return new LocationUpdate(vehicleId, latitude, longitude);
	}

	public static LocationUpdate parseCsv(String csv) {
		String[] parts = csv.split(",");
		if (parts.length < 3) {
			throw new IllegalArgumentException("CSV malformed: " + csv);
		}
		String vehicleId = parts[0].trim();
		double latitude = Double.parseDouble(parts[1].trim());
		double longitude = Double.parseDouble(parts[2].trim());
		return new LocationUpdate(vehicleId, latitude, longitude);
	}

	private static String extractGroup(Pattern pattern, String input, String errorMessage) {
		Matcher matcher = pattern.matcher(input);
		if (!matcher.find()) {
			throw new IllegalArgumentException(errorMessage + ": " + input);
		}
		return matcher.group(1);
	}
}
