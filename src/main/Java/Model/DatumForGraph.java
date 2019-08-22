package Model;

public class DatumForGraph {
    private Long timestamp;
    private Float measurement;

    public DatumForGraph() {
    }

    public DatumForGraph(Long timestamp, Float measurement) {
        this.measurement = measurement;
        this.timestamp = timestamp;
    }

    public Float getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Float measurement) {
        this.measurement = measurement;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
