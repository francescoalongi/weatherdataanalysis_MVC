package Model;

import javax.persistence.*;

@Entity
@Table(name = "DatumCity")
public class DatumCity extends Datum{

    private Float pollutionLevel;

    public DatumCity() {}

    public DatumCity(Long timestamp, Float temperature, Float pressure, Float humidity, Float rain, Float windModule, String windDirection, Float pollutionLevel) {
        super(timestamp,temperature,pressure,humidity,rain,windModule,windDirection);
        this.pollutionLevel = pollutionLevel;
    }

    public void setPollutionLevel(Float pollutionLevel) {
        this.pollutionLevel = pollutionLevel;
    }

    public Float getPollutionLevel() {
        return pollutionLevel;
    }
}
