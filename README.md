# TransIAP RabbitMQ GPS Tracking Integration

This Eclipse-ready Java project simulates GPS producers in multiple formats, converts them through a middleware into a unified JSON schema, publishes to a RabbitMQ exchange, logs to the STC database using `STC-DAO.jar`, and provides a console visualizer.

## Prerequisites

- Java 8+ installed
- RabbitMQ running on `localhost:5672`
- MySQL running on `localhost:3306` with database `stc`
- External JARs copied into `lib/` as listed in `lib/README.md`

## Running the Components (Eclipse)

1. **Start RabbitMQ** locally (default credentials `guest`/`guest`).
2. **Start MySQL** and ensure the `stc` schema exists with the required tables.
3. Import the project in Eclipse (File → Import → Existing Projects into Workspace).
4. Run the middleware first:
   - `middleware.LogisticsLiveMiddleware`
5. Run one or more producers:
   - `producer.KMLProducer`
   - `producer.GeoJSONProducer`
   - `producer.TransIAPCSVProducer`
6. Run the visualizer to observe the unified JSON stream:
   - `visualizer.LocationVisualizer`

## Notes

- The middleware calls the SNTN REST endpoints configured in `util.SntnClient`.
- Database connection details are defined in `db.DatabaseLogger`.
- Producers publish to a direct exchange `location` with routing keys `KML`, `GeoJSON`, and `TransIAPCSV`.
- The middleware publishes unified JSON to the fanout exchange `traslados.localizaciones`.
- The project is configured to rely only on the JARs provided in `lib/`; no additional dependencies are required.
