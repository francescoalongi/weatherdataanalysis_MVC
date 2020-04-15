package Model;

import java.util.List;

public class DatumForGraph {
    private String idStation;
    private String stationName;
    private String unitOfMeasure;
    private List<Double> measurements;
    private List<Long> timestamps;
    private Double avg;

    public DatumForGraph(String idStation, String stationName, String unitOfMeasure, List<Double> measurements, List<Long> timestamps, Double avg) {
        this.idStation = idStation;
        this.stationName = stationName;
        this.unitOfMeasure = unitOfMeasure;
        this.measurements = measurements;
        this.timestamps = timestamps;
        this.avg = avg;
    }

    public Double getAvg() {
        return avg;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getIdStation() {
        return idStation;
    }

    public void setIdStation(String idStation) {
        this.idStation = idStation;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public List<Double> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Double> measurements) {
        this.measurements = measurements;
    }

    public List<Long> getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(List<Long> timestamps) {
        this.timestamps = timestamps;
    }

    public DatumForGraph() {
    }


}
