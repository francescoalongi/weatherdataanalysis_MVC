package Model;

import javax.persistence.*;

@Entity
@Table(name="UnitOfMeasure")
public class UnitOfMeasure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUnitOfMeasure;
    private String temperature;
    private String pressure;
    private String humidity;
    private String rain;
    private String windModule;
    private String windDirection;
    private String dewPt;
    private String uvRadiation;
    private String snowLevel;
    private String pollutionLevel;

    //mandatory
    public UnitOfMeasure() {
    }

    public UnitOfMeasure(String temperature, String pressure, String humidity, String rain, String windModule, String windDirection, String dewPt, String uvRadiation, String snowLevel, String pollutionLevel) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.rain = rain;
        this.windModule = windModule;
        this.windDirection = windDirection;
        this.dewPt = dewPt;
        this.uvRadiation = uvRadiation;
        this.snowLevel = snowLevel;
        this.pollutionLevel = pollutionLevel;
    }

    public Integer getIdUnitOfMeasure() {
        return idUnitOfMeasure;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public String getWindModule() {
        return windModule;
    }

    public void setWindModule(String windModule) {
        this.windModule = windModule;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getDewPt() {
        return dewPt;
    }

    public void setDewPt(String dewPt) {
        this.dewPt = dewPt;
    }

    public String getUvRadiation() {
        return uvRadiation;
    }

    public void setUvRadiation(String uvRadiation) {
        this.uvRadiation = uvRadiation;
    }

    public String getSnowLevel() {
        return snowLevel;
    }

    public void setSnowLevel(String snowLevel) {
        this.snowLevel = snowLevel;
    }

    public String getPollutionLevel() {
        return pollutionLevel;
    }

    public void setPollutionLevel(String pollutionLevel) {
        this.pollutionLevel = pollutionLevel;
    }

}
