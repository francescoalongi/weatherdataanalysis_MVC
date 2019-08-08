package Controller;

import Model.*;
import Utils.HibernateUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "UploadDataServlet")
@MultipartConfig
public class UploadDataServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String idStation = request.getParameter("radios");
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String hql = "FROM Station WHERE idStation = :idStation";
        Query query = session.createQuery(hql);
        query.setParameter("idStation", Integer.parseInt(idStation));
        Station station = (Station) query.list().get(0); //wrong! use getSingleResult() instead

        Part filePart = request.getPart("newData");
        InputStream fileContent = filePart.getInputStream();
        Reader in = new InputStreamReader(fileContent, StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
        try {
            for (CSVRecord record : records) {
                Long timestamp = Long.parseLong(record.get("\uFEFFtimestamp")); // /uFEFF is the Byte Order Mark
                Float temperature = Float.parseFloat(record.get("temperature"));
                Float pressure = Float.parseFloat(record.get("pressure"));
                Float humidity = Float.parseFloat(record.get("humidity"));
                Float rain = Float.parseFloat(record.get("rain"));
                Float windModule = Float.parseFloat(record.get("windModule"));
                String windDirection = record.get("windDirection");
                Float additionalField;
                Datum datum;

                switch(station.getType()) {
                    case "city":
                        additionalField = Float.parseFloat(record.get("pollutionLevel"));
                        datum = new DatumCity(timestamp,temperature,pressure,humidity, rain, windModule, windDirection, additionalField);
                        break;
                    case "country":
                        additionalField = Float.parseFloat(record.get("dewPoint"));
                        datum = new DatumCountry(timestamp,temperature,pressure,humidity, rain, windModule, windDirection, additionalField);
                        break;
                    case "mountain":
                        additionalField = Float.parseFloat(record.get("snowLevel"));
                        datum = new DatumMountain(timestamp,temperature,pressure,humidity, rain, windModule, windDirection, additionalField);
                        break;
                    case "sea":
                        additionalField = Float.parseFloat(record.get("uvRadiation"));
                        datum = new DatumSea(timestamp,temperature,pressure,humidity, rain, windModule, windDirection, additionalField);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }

                datum.setStation(station);
                session.save(datum);
            }
        } catch (IllegalArgumentException e) {
            //TODO: this exception is raised if one of the parameter of the record.get function is not mapped in the header,
            // this case must be handled. Namely, if we chose a sea station but we are uploading data corresponding to
            // a country station, the program flow ends up here.
        }

        session.getTransaction().commit();
        session.close();
        //TODO: notificate the user of the successful operation
        response.sendRedirect(getServletContext().getContextPath() + "/Homepage");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO: An error page must be created to redirect users there when they perform illegal actions
    }
}
