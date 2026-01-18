package db;

import model.LocationUpdate;
import stc.dao.DAOFactory;
import stc.dao.LocalizacionGPSDAO;
import stc.dao.TrasladoDAO;
import stc.model.LocalizacionGPS;
import stc.model.Traslado;

public class DatabaseLogger {
	private final LocalizacionGPSDAO localizacionGPSDAO;
	private final TrasladoDAO trasladoDAO;

	public DatabaseLogger(String host, String port, String user, String password, String database) {
		DAOFactory daoFactory = DAOFactory.getCurrentInstance();
		daoFactory.connect(host, port, user, password, database);
		this.localizacionGPSDAO = daoFactory.getLocalizacionGPSDAO();
		this.trasladoDAO = daoFactory.getTrasladoDAO();
	}

	public void logLocation(LocationUpdate update) {
		Traslado traslado = trasladoDAO.getTrasladoActivoPorVehiculo(update.getVehicleId());
		if (traslado == null) {
			System.out.println("No active traslado for vehicle " + update.getVehicleId());
			return;
		}
		LocalizacionGPS localizacion = new LocalizacionGPS();
		localizacion.setLatitud(update.getLatitude());
		localizacion.setLongitud(update.getLongitude());
		localizacion.setTraslado(traslado);
		localizacionGPSDAO.saveLocalizacionGPS(localizacion);
		trasladoDAO.updateUltimaLocalizacionTraslado(traslado, localizacion);
	}
}
