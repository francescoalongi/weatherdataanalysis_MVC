package Model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.lang.reflect.Field;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Datum {

    /*
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE)
    @Basic(optional = false)
    private Integer idDatum;

    private Long timestamp;

    */

    @EmbeddedId
    private DatumPK datumPK;

    private Float temperature;
    private Float pressure;
    private Float humidity;
    private Float rain;
    private Float windModule;
    private String windDirection;

    /*
    @JsonIgnore //This annotation is added in order to correctly serialize into json object a Station (avoiding circular references)
    @ManyToOne
    @JoinColumn(name="idStation")
    private Station station;
    */

    public Datum() {}

    public Datum(DatumPK datumPK, Float temperature, Float pressure, Float humidity, Float rain, Float windModule, String windDirection) {
        this.datumPK = datumPK;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.rain = rain;
        this.windModule = windModule;
        this.windDirection = windDirection;
    }
/*
    public Integer getIdDatum() {
        return idDatum;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
*/
    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getPressure() {
        return pressure;
    }

    public void setPressure(Float pressure) {
        this.pressure = pressure;
    }

    public Float getHumidity() {
        return humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    public Float getRain() {
        return rain;
    }

    public void setRain(Float rain) {
        this.rain = rain;
    }

    public Float getWindModule() {
        return windModule;
    }

    public void setWindModule(Float windModule) {
        this.windModule = windModule;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }
/*
    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
*/
    public String getFieldsNameAsCSV() {
        return "timestamp" + "," + "temperature" + "," + "pressure" + "," + "humidity" + "," + "rain" + "," + "windModule" + "," + "windDirection";
    }

    public String getFieldsAsCSV() {
        //return timestamp + "," + temperature + "," + pressure + "," + humidity + "," + rain + "," + windModule + "," + windDirection;
        return this.datumPK.getTimestamp() + "," + temperature + "," + pressure + "," + humidity + "," + rain + "," + windModule + "," + windDirection;

    }
}
