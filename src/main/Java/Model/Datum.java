package Model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DatumCountry.class, name = "DatumCountry"),
        @JsonSubTypes.Type(value = DatumCity.class, name = "DatumCity"),
        @JsonSubTypes.Type(value = DatumSea.class, name = "DatumSea"),
        @JsonSubTypes.Type(value = DatumMountain.class, name = "DatumMountain")
})
public abstract class Datum {

    private Long timestamp;
    private Integer idStation;
    private Float temperature;
    private Float pressure;
    private Float humidity;
    private Float rain;
    private Float windModule;
    private String windDirection;

    public Datum() {}

    public Datum(Long timestamp, Integer idStation, Float temperature, Float pressure, Float humidity, Float rain, Float windModule, String windDirection) {
        this.timestamp = timestamp;
        this.idStation = idStation;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.rain = rain;
        this.windModule = windModule;
        this.windDirection = windDirection;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getIdStation() {
        return idStation;
    }

    public void setIdStation(Integer idStation) {
        this.idStation = idStation;
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

    public String getFieldsNameAsCSV() {
        return "timestamp" + "," + "temperature" + "," + "pressure" + "," + "humidity" + "," + "rain" + "," + "windModule" + "," + "windDirection";
    }

    public String getFieldsAsCSV() {
        return this.getTimestamp() + "," + temperature + "," + pressure + "," + humidity + "," + rain + "," + windModule + "," + windDirection;

    }

}
