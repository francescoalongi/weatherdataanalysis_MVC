package Model;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class DatumPK implements Serializable {

    private Long timestamp;

    @JsonIgnore//This annotation is added in order to correctly serialize into json object a Station (avoiding circular references)
    @ManyToOne
    @JoinColumn(name="idStation")
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
}
