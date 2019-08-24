package Model;

import java.util.ArrayList;
import java.util.List;

public class DatumForGraph {
    private Integer idStation;
    private String stationName;
    private List<Float> measurements;
    private List<Long> timestamps;

    public DatumForGraph(Integer idStation, String stationName, List<Float> measurements, List<Long> timestamps) {
        this.idStation = idStation;
        this.stationName = stationName;
        this.measurements = measurements;
        this.timestamps = timestamps;
    }

    public Integer getIdStation() {
        return idStation;
    }

    public void setIdStation(Integer idStation) {
        this.idStation = idStation;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public List<Float> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Float> measurements) {
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