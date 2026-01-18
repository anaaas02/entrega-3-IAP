package util;

public final class RabbitMQConfig {
	public static final String INPUT_EXCHANGE = "location";
	public static final String OUTPUT_EXCHANGE = "traslados.localizaciones";

	public static final String ROUTING_KEY_KML = "KML";
	public static final String ROUTING_KEY_GEOJSON = "GeoJSON";
	public static final String ROUTING_KEY_CSV = "TransIAPCSV";

	public static final String QUEUE_KML = "location.KML";
	public static final String QUEUE_GEOJSON = "location.GeoJSON";
	public static final String QUEUE_CSV = "location.TransIAPCSV";

	private RabbitMQConfig() {
	}
}
