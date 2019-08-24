package Model;

import java.util.List;

public class DataForGraph {
    List<Long> timestamp;
    List<DatumForGraph> data;

    public List<Long> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(List<Long> timestamp) {
        this.timestamp = timestamp;
    }

    public List<DatumForGraph> getData() {
        return data;
    }

    public void setData(List<DatumForGraph> data) {
        this.data = data;
    }

    public DataForGraph(List<Long> timestamp, List<DatumForGraph> data) {
        this.timestamp = timestamp;
        this.data = data;
    }

    public DataForGraph() {
    }

}
