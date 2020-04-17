package Model;

public class MinimizedStation {
    public Integer getIdStation() {
        return idStation;
    }

    public void setIdStation(Integer idStation) {
        this.idStation = idStation;
    }

    private Integer idStation;
    private String name;
    private String type;

    public MinimizedStation() {
    }

    public MinimizedStation(Integer idStation, String name, String type) {
        this.idStation = idStation;
        this.name = name;
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
