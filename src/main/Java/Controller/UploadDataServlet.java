package Controller;

import Model.*;

import Utils.Collections;
import Utils.MongoDBUtil;
import com.mongodb.client.FindIterable;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@WebServlet(name = "UploadDataServlet")
@MultipartConfig
public class UploadDataServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String idStation = request.getParameter("idStation");

        Document filter = new Document("_id", new ObjectId(idStation));

        ObjectMapper mapper = new ObjectMapper();
        FindIterable<Document> result = MongoDBUtil.executeSelect(filter, Collections.STATIONS);
        Document doc = result.first();
        doc.append("_id", doc.get("_id").toString());
        Station station = mapper.convertValue(doc, Station.class);

        Part filePart = request.getPart("newData");
        InputStream fileContent = filePart.getInputStream();
        Reader in = new InputStreamReader(fileContent, StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);

        List<Document> docs = new ArrayList<>();
        Integer insertionCount = 0;
        for (CSVRecord record : records) {
            try {
                Long timestamp = Long.parseLong(record.get("timestamp"));
                Float temperature = Float.parseFloat(record.get("temperature"));
                Float pressure = Float.parseFloat(record.get("pressure"));
                Float humidity = Float.parseFloat(record.get("humidity"));
                Float rain = Float.parseFloat(record.get("rain"));
                Float windModule = Float.parseFloat(record.get("windModule"));
                String windDirection = record.get("windDirection");

                Float additionalField;
                Datum datum;
                switch (station.getType().toLowerCase()) {
                    case "city":
                        additionalField = Float.parseFloat(record.get("pollutionLevel"));
                        datum = new DatumCity(timestamp,idStation,temperature,pressure,humidity, rain, windModule, windDirection, additionalField);
                        break;
                    case "country":
                        additionalField = Float.parseFloat(record.get("dewPoint"));
                        datum = new DatumCountry(timestamp, idStation,temperature,pressure,humidity, rain, windModule, windDirection, additionalField);
                        break;
                    case "mountain":
                        additionalField = Float.parseFloat(record.get("snowLevel"));
                        datum = new DatumMountain(timestamp, idStation,temperature,pressure,humidity, rain, windModule, windDirection, additionalField);
                        break;
                    case "sea":
                        additionalField = Float.parseFloat(record.get("uvRadiation"));
                        datum = new DatumSea(timestamp, idStation,temperature,pressure,humidity, rain, windModule, windDirection, additionalField);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }

                Document newDoc = new Document(mapper.convertValue(datum, HashMap.class));
                docs.add(newDoc);
                insertionCount++;
                if (insertionCount % 500 == 0 || !records.iterator().hasNext()) {
                    MongoDBUtil.executeInsert(docs, Collections.DATA);
                    docs.clear();
                    insertionCount = 0;
                }

            } catch (NumberFormatException e) {
                // if a datum contains an invalid field, skip it
            }
        }

        request.setAttribute("outcomeUpload", "Your .csv file has been successfully uploaded.");
        request.getRequestDispatcher("/LoadStations").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("Error", "Error! GET request not supported");
        getServletContext().getRequestDispatcher("/Error").forward(request, response);
    }
}
