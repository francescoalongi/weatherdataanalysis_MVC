package Model;

import javax.persistence.*;

@Entity
@Table(name = "DatumMountain")
public class DatumMountain extends Datum{

    private Float snowLevel;

    public DatumMountain() {}

    public DatumMountain(DatumPK datumPK, Float temperature, Float pressure, Float humidity, Float rain, Float windModule, String windDirection, Float snowLevel) {
        super(datumPK,temperature,pressure,humidity,rain,windModule,windDirection);
        this.snowLevel = snowLevel;
    }

    public void setSnowLevel(Float snowLevel) {
        this.snowLevel = snowLevel;
    }

    public Float getSnowLevel() {
        return snowLevel;
    }

    @Override
    public String getFieldsNameAsCSV() {
        return super.getFieldsNameAsCSV() + "," + "snowLevel";
    }

    @Override
    public String getFieldsAsCSV() {
        return super.getFieldsAsCSV() + "," + snowLevel;
    }
}

