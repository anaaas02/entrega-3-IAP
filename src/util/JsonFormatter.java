package util;

import model.LocationUpdate;

public final class JsonFormatter {
	private JsonFormatter() {
	}

	public static String formatUnified(LocationUpdate update, String appKey, String timestamp) {
		return String.format(
				"{\"coordenadas\":{\"latitud\":%s,\"longitud\":%s},\"vehiculo\":\"%s\",\"auth\":\"%s\",\"timestamp\":\"%s\"}",
				trimTrailingZeros(update.getLatitude()),
				trimTrailingZeros(update.getLongitude()),
				escape(update.getVehicleId()),
				escape(appKey),
				escape(timestamp));
	}

	private static String escape(String value) {
		return value.replace("\"", "\\\"");
	}

	private static String trimTrailingZeros(double value) {
		String text = Double.toString(value);
		if (text.contains(".")) {
			text = text.replaceAll("0+$", "").replaceAll("\\.$", "");
		}
		return text;
	}
}
