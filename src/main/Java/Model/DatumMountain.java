package Model;

import javax.persistence.*;

@Entity
@Table(name = "DatumMountain")
public class DatumMountain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer idDatumMountain;
    private Long timestamp;
    private Float temperature;
    private Float pressure;
    private Float humidity;
    private Float rain;
    private Float windModule;
    private String windDirection;
    private Float snowLevel;

    @ManyToOne
    @JoinColumn(name="idStation")
    private Station station;

    public DatumMountain() {}

    public DatumMountain(Long timestamp, Float temperature, Float pressure, Float humidity, Float rain, Float windModule, String windDirection, Float snowLevel) {
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.rain = rain;
        this.windModule = windModule;
        this.windDirection = windDirection;
        this.snowLevel = snowLevel;
    }

    public Integer getIdDatumMountain() {
        return idDatumMountain;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

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

    public Float getSnowLevel() {
        return snowLevel;
    }

    public void setSnowLevel(Float snowLevel) {
        this.snowLevel = snowLevel;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}

