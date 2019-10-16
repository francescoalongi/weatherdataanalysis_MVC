package Model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.lang.reflect.Field;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Datum {

    @EmbeddedId
    private DatumPK datumPK;

    private Float temperature;
    private Float pressure;
    private Float humidity;
    private Float rain;
    private Float windModule;
    private String windDirection;


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
        return this.datumPK.getTimestamp() + "," + temperature + "," + pressure + "," + humidity + "," + rain + "," + windModule + "," + windDirection;

    }
}
