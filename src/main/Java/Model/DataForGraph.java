package Model;

import java.util.ArrayList;
import java.util.List;

public class DataForGraph {
    private Integer idStation;
    private List<DatumForGraph> data;

    public DataForGraph(Integer idStation, List<DatumForGraph> data) {
        this.idStation = idStation;
        this.data = data;
    }

    public DataForGraph() {
    }

    public Integer getIdStation() {
        return idStation;
    }

    public void setIdStation(Integer idStation) {
        this.idStation = idStation;
    }

    public List<DatumForGraph> getData() {
        return data;
    }

    public void setData(List<DatumForGraph> data) {
        this.data = data;
    }
}
