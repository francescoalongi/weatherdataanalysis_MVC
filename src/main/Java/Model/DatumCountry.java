package Model;

import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl=DatumCountry.class)
public class DatumCountry extends Datum {

    private Float dewPoint;

    public DatumCountry() {}

    public DatumCountry(//DatumPK datumPK,
                        Long timestamp, Integer idStation, Float temperature, Float pressure, Float humidity, Float rain, Float windModule, String windDirection, Float dewPoint) {
        super(timestamp, idStation,temperature,pressure,humidity,rain,windModule,windDirection);
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
