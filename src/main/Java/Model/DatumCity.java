package Model;

import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl=DatumCity.class)
public class DatumCity extends Datum{

    private Float pollutionLevel;

    public DatumCity() {}

    public DatumCity(Long timestamp, Integer idStation, Float temperature, Float pressure, Float humidity, Float rain, Float windModule, String windDirection, Float pollutionLevel) {
        super(timestamp, idStation,temperature,pressure,humidity,rain,windModule,windDirection);
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

