package Model;

import javax.persistence.*;

@Entity
@Table(name = "DatumSea")
public class DatumSea extends Datum {

    private Float uvRadiation;

    public DatumSea() {}

    public DatumSea(Long timestamp, Float temperature, Float pressure, Float humidity, Float rain, Float windModule, String windDirection, Float uvRadiation) {
        super(timestamp,temperature,pressure,humidity,rain,windModule,windDirection);
        this.uvRadiation = uvRadiation;
    }

    public void setUvRadiation(Float uvRadiation) {
        this.uvRadiation = uvRadiation;
    }

    public Float getUvRadiation() {
        return uvRadiation;
    }
}
