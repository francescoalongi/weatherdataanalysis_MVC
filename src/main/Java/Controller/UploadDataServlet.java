package Controller;

import Model.*;
import Utils.MySQLUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import com.fasterxml.jackson.databind.ObjectMapper;


import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

@WebServlet(name = "UploadDataServlet")
@MultipartConfig
public class UploadDataServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String idStation = request.getParameter("idStation");

        String stationQuery = "SELECT * FROM station AS s WHERE s.idStation = ?";
        String unitOfMeasureQuery = "SELECT * FROM unitofmeasure AS u WHERE u.idUnitOfMeasure = ?";
        List<Map<String,Object>> results = new ArrayList<>();
        try (Connection connection = MySQLUtil.getConnection();
             PreparedStatement preparedStatementSta = connection.prepareStatement(stationQuery);
             PreparedStatement preparedStatementUOM = connection.prepareStatement(unitOfMeasureQuery)) {
            preparedStatementSta.setInt(1, Integer.parseInt(idStation));
            ResultSet rs = preparedStatementSta.executeQuery();
            results = MySQLUtil.resultSetToArrayList(rs);
            preparedStatementUOM.setInt(1, (Integer) results.get(0).get("idUnitOfMeasure"));
            rs = preparedStatementUOM.executeQuery();
            results.get(0).put("unitOfMeasure", MySQLUtil.resultSetToArrayList(rs).get(0));
            results.get(0).remove("idUnitOfMeasure");
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ObjectMapper mapper = new ObjectMapper();
        Station station = mapper.convertValue(results.get(0), Station.class);

        String insertQuery = "INSERT INTO ";
        Part filePart = request.getPart("newData");
        InputStream fileContent = filePart.getInputStream();
        Reader in = new InputStreamReader(fileContent, StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
        try {
            switch (station.getType().toLowerCase()) {
                case "city":
                    insertQuery += "DatumCity VALUES (?,?,?,?,?,?,?,?,?)";
                    try {
                        parseCSV(insertQuery, "pollutionLevel", records, Integer.parseInt(idStation));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "country":
                    insertQuery += "DatumCountry VALUES (?,?,?,?,?,?,?,?,?)";
                    try {
                        parseCSV(insertQuery, "dewPoint", records, Integer.parseInt(idStation));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "mountain":
                    insertQuery += "DatumMountain VALUES (?,?,?,?,?,?,?,?,?)";
                    try {
                        parseCSV(insertQuery, "snowLevel", records, Integer.parseInt(idStation));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "sea":
                    insertQuery += "DatumSea VALUES (?,?,?,?,?,?,?,?,?)";
                    try {
                        parseCSV(insertQuery, "uvRadiation", records, Integer.parseInt(idStation));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    throw new IllegalArgumentException();
            }

        } catch (IllegalArgumentException e) {
            request.setAttribute("outcomeUpload", "The .csv file you are trying to upload does not fit for the selected station. Use another one.");
            request.getRequestDispatcher("/LoadStations").forward(request,response);
        }

        request.setAttribute("outcomeUpload", "Your .csv file has been successfully uploaded.");
        request.getRequestDispatcher("/LoadStations").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("Error", "Error! GET request not supported");
        getServletContext().getRequestDispatcher("/Error").forward(request, response);
    }

    private void parseCSV(String insertQuery, String additionalFieldS, Iterable<CSVRecord> records, Integer idStation) throws SQLException{
        Connection connection = MySQLUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
        int counter = 0;
        int index = 1;
        long executionTime = 0;
        for (CSVRecord record : records) {
            try {
                Long timestamp = Long.parseLong(record.get("timestamp")); // /uFEFF is the Byte Order Mark --> removed, it gave problems
                Float temperature = Float.parseFloat(record.get("temperature"));
                Float pressure = Float.parseFloat(record.get("pressure"));
                Float humidity = Float.parseFloat(record.get("humidity"));
                Float rain = Float.parseFloat(record.get("rain"));
                Float windModule = Float.parseFloat(record.get("windModule"));
                String windDirection = record.get("windDirection");
                Float additionalField = Float.parseFloat(record.get(additionalFieldS));

                preparedStatement.setInt(index++, idStation);
                preparedStatement.setLong(index++, timestamp);
                preparedStatement.setFloat(index++, temperature);
                preparedStatement.setFloat(index++, pressure);
                preparedStatement.setFloat(index++, humidity);
                preparedStatement.setFloat(index++, rain);
                preparedStatement.setFloat(index++, windModule);
                preparedStatement.setString(index++, windDirection);
                preparedStatement.setFloat(index++, additionalField);

                preparedStatement.addBatch();
                index = 1;

                if (counter % 500 == 0 || !records.iterator().hasNext()) {
                    long bTime = System.currentTimeMillis();
                    preparedStatement.executeBatch();
                    long eTime = System.currentTimeMillis();
                    executionTime += eTime - bTime;
                }
                counter++;
            } catch (NumberFormatException e) {
                // if a datum contains an invalid field, skip it
            }
        }
        System.out.println(insertQuery + " --> " + executionTime);
        preparedStatement.close();
        connection.close();
    }
}
