/*package Controller;

import Model.*;
import Utils.HibernateUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PeriodicDataAcquirer {

    private ScheduledExecutorService scheduler;

    private PeriodicDataAcquirer(String path, Station station) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new DownloadDataFromFile(path, station), 0, 5, TimeUnit.MINUTES);
    }

    public void close() {
        scheduler.shutdownNow();
    }

}

class DownloadDataFromFile implements Runnable {

    private String path;
    private Station station;
    private String lastTimestamp;

    DownloadDataFromFile(String path, Station station) {
        this.path = path;
        this.station = station;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(path);
            InputStream fileContent = url.openStream();
            Reader in = new InputStreamReader(fileContent, StandardCharsets.UTF_8);
            List<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in).getRecords();
            String timestamp = records.get(0).get("timestamp");
            if (timestamp.equals(lastTimestamp)) // check if the file is updated
                return;
            lastTimestamp = timestamp;
            Float temperature = Float.parseFloat(records.get(0).get("temperature"));
            Float pressure = Float.parseFloat(records.get(0).get("pressure"));
            Float humidity = Float.parseFloat(records.get(0).get("humidity"));
            Float rain = Float.parseFloat(records.get(0).get("daily rainfall"));
            Float windModule = Float.parseFloat(records.get(0).get("wind speed"));
            String windDirection = records.get(0).get("direction (sector)");
            Float additionalField;
            Datum datum;
            switch (station.getType().toLowerCase()) {
                case "city":
                    additionalField = Float.parseFloat(records.get(0).get("pollutionLevel"));
                    datum = new DatumCity(Long.parseLong(timestamp), temperature, pressure, humidity, rain, windModule, windDirection, additionalField);
                    break;
                case "country":
                    additionalField = Float.parseFloat(records.get(0).get("dew point"));
                    datum = new DatumCountry(Long.parseLong(timestamp), temperature, pressure, humidity, rain, windModule, windDirection, additionalField);
                    break;
                case "mountain":
                    additionalField = Float.parseFloat(records.get(0).get("snowLevel"));
                    datum = new DatumMountain(Long.parseLong(timestamp), temperature, pressure, humidity, rain, windModule, windDirection, additionalField);
                    break;
                case "sea":
                    additionalField = Float.parseFloat(records.get(0).get("uvRadiation"));
                    datum = new DatumSea(Long.parseLong(timestamp), temperature, pressure, humidity, rain, windModule, windDirection, additionalField);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            datum.setStation(station);
            HibernateUtil.executeInsert(datum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/