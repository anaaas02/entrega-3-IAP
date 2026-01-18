package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import model.LocationUpdate;
import util.MessageParsers;

public class MessageParsersTest {
	@Test
	public void parsesKml() {
		String kml = "<kml><Placemark><Point><coordinates>38.5,-0.4</coordinates></Point>"
				+ "<Vehicle id=\"0245-MQL\"/></Placemark></kml>";
		LocationUpdate update = MessageParsers.parseKml(kml);
		assertEquals("0245-MQL", update.getVehicleId());
		assertEquals(38.5, update.getLatitude());
		assertEquals(-0.4, update.getLongitude());
	}

	@Test
	public void parsesGeoJson() {
		String json = "{"
				+ "\"type\":\"Feature\","
				+ "\"geometry\":{\"type\":\"Point\",\"coordinates\":[41.2,2.1]},"
				+ "\"properties\":{\"vehicle\":\"7788-ABC\"}"
				+ "}";
		LocationUpdate update = MessageParsers.parseGeoJson(json);
		assertEquals("7788-ABC", update.getVehicleId());
		assertEquals(41.2, update.getLatitude());
		assertEquals(2.1, update.getLongitude());
	}

	@Test
	public void parsesCsv() {
		LocationUpdate update = MessageParsers.parseCsv("9900-RTY,37.0,-5.9");
		assertEquals("9900-RTY", update.getVehicleId());
		assertEquals(37.0, update.getLatitude());
		assertEquals(-5.9, update.getLongitude());
	}
}
