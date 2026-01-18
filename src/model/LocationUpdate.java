package model;

public class LocationUpdate {
	private final String vehicleId;
	private final double latitude;
	private final double longitude;

	public LocationUpdate(String vehicleId, double latitude, double longitude) {
		this.vehicleId = vehicleId;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
}
