package Model;


import java.io.Serializable;
import java.util.Objects;

public class DatumPK implements Serializable {

    private Long timestamp;

    //@JsonIgnore//This annotation is added in order to correctly serialize into json object a Station (avoiding circular references)
    private Station station;

    public DatumPK() {}

    public DatumPK(Long timestamp, Station station) {
        this.timestamp = timestamp;
        this.station = station;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatumPK datumPK = (DatumPK) o;
        return timestamp.equals(datumPK.timestamp) &&
                station.equals(datumPK.station);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, station);
    }
}
