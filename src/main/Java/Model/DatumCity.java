package Model;

import javax.persistence.*;

@Entity
@Table(name = "DatumCity")
public class DatumCity extends Datum{

    private Float pollutionLevel;

    public DatumCity() {}

    public DatumCity(DatumPK datumPK, Float temperature, Float pressure, Float humidity, Float rain, Float windModule, String windDirection, Float pollutionLevel) {
        super(datumPK,temperature,pressure,humidity,rain,windModule,windDirection);
        this.pollutionLevel = pollutionLevel;
    }

    public void setPollutionLevel(Float pollutionLevel) {
        this.pollutionLevel = pollutionLevel;
    }

    public Float getPollutionLevel() {
        return pollutionLevel;
    }

    @Override
    public String getFieldsNameAsCSV() {
        return super.getFieldsNameAsCSV() + "," + "pollutionLevel";
    }

    @Override
    public String getFieldsAsCSV() {
        return super.getFieldsAsCSV() + "," + pollutionLevel;
    }


}

