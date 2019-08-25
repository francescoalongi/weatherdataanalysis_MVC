package Model;


import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Station")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer idStation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="idUnitOfMeasure")
    private UnitOfMeasure unitOfMeasure;

    private String name;
    private String type;
    private Float latitude;
    private Float longitude;
    private Float altitude;


    //Cascade is here necessary because otherwise the statement session.save(station) will just insert the station (without its inner field)
    @OneToMany(fetch= FetchType.EAGER, cascade=CascadeType.ALL, mappedBy = "station")
    private Set<Datum> datumSet = new HashSet<Datum>();

    public Set<Datum> getDatumSet() {
        return datumSet;
    }

    public void setDatumSet(Set<Datum> datumSet) {
        this.datumSet = datumSet;
    }

    //mandatory
    public Station() {
    }

    public Station(UnitOfMeasure unitOfMeasure, String name, String type, Float latitude, Float longitude, Float altitude) {
        this.unitOfMeasure = unitOfMeasure;
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        //this.datumCountrySet = datumCountrySet;
    }

    public Integer getIdStation() {
        return idStation;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
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

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getAltitude() {
        return altitude;
    }

    public void setAltitude(Float altitude) {
        this.altitude = altitude;
    }

}
