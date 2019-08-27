package Model;

import javax.persistence.*;

@Entity
@Table(name = "DatumSea")
public class DatumSea extends Datum {

    private Float uvRadiation;

    public DatumSea() {}

    public DatumSea(DatumPK datumPK, Float temperature, Float pressure, Float humidity, Float rain, Float windModule, String windDirection, Float uvRadiation) {
        super(datumPK,temperature,pressure,humidity,rain,windModule,windDirection);
        this.uvRadiation = uvRadiation;
    }

    public void setUvRadiation(Float uvRadiation) {
        this.uvRadiation = uvRadiation;
    }

    public Float getUvRadiation() {
        return uvRadiation;
    }

    @Override
    public String getFieldsNameAsCSV() {
        return super.getFieldsNameAsCSV() + "," + "uvRadiation";
    }

    @Override
    public String getFieldsAsCSV() {
        return super.getFieldsAsCSV() + "," + uvRadiation;
    }
}
