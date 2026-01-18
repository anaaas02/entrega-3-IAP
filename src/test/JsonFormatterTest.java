package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import model.LocationUpdate;
import util.JsonFormatter;

public class JsonFormatterTest {
	@Test
	public void formatsUnifiedJson() {
		LocationUpdate update = new LocationUpdate("0245-MQL", 38.3456, -0.4839);
		String json = JsonFormatter.formatUnified(update, "APPKEY123", "2024-01-01T12:00:00Z");
		assertEquals(
				"{\"coordenadas\":{\"latitud\":38.3456,\"longitud\":-0.4839},\"vehiculo\":\"0245-MQL\",\"auth\":\"APPKEY123\",\"timestamp\":\"2024-01-01T12:00:00Z\"}",
				json);
	}
}
