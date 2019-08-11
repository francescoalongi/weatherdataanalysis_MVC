package Model;

import javax.persistence.*;

@Entity
@Table(name = "DatumCountry")
public class DatumCountry extends Datum {

    private Float dewPoint;

    public DatumCountry() {}

    public DatumCountry(Long timestamp, Float temperature, Float pressure, Float humidity, Float rain, Float windModule, String windDirection, Float dewPoint) {
        super(timestamp,temperature,pressure,humidity,rain,windModule,windDirection);
        this.dewPoint = dewPoint;
    }

    public Float getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(Float dewPoint) {
        this.dewPoint = dewPoint;
    }

    @Override
    public String getFieldsNameAsCSV() {
        return super.getFieldsNameAsCSV() + "," + "dewPoint";
    }

    @Override
    public String getFieldsAsCSV() {
        return super.getFieldsAsCSV() + "," + dewPoint;
    }
}
