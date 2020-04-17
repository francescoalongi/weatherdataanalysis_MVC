package Controller;

import Model.Station;
import Model.UnitOfMeasure;
import Utils.MySQLUtil;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;

@WebServlet(name = "CreateStationServlet")
public class CreateStationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        String json = br.readLine();
        ObjectMapper mapper = new ObjectMapper();
        try {
            Station station = mapper.readValue(json, Station.class);
            UnitOfMeasure unitOfMeasure = station.getUnitOfMeasure();
            String unitOfMeasureQuery = "INSERT INTO unitofmeasure (temperature, pressure, humidity, rain, " +
                    "windModule, windDirection, uvRadiation, snowLevel, pollutionLevel, dewPoint) VALUES (?,?,?,?,?,?,?,?,?,?)";
            String stationQuery = "INSERT INTO station (idUnitOfMeasure, name, type, latitude, longitude, altitude) VALUES (?,?,?,?,?,?)";
            Integer idUnitOfMeasure;
            try (Connection connection = MySQLUtil.getConnection();
                 PreparedStatement preparedStatementUOM = connection.prepareStatement(unitOfMeasureQuery, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement preparedStatementSta = connection.prepareStatement(stationQuery)) {

                int i = 1;
                preparedStatementUOM.setString(i++, unitOfMeasure.getTemperature());
                preparedStatementUOM.setString(i++, unitOfMeasure.getPressure());
                preparedStatementUOM.setString(i++, unitOfMeasure.getHumidity());
                preparedStatementUOM.setString(i++, unitOfMeasure.getRain());
                preparedStatementUOM.setString(i++, unitOfMeasure.getWindModule());
                preparedStatementUOM.setString(i++, unitOfMeasure.getWindDirection());
                preparedStatementUOM.setString(i++, unitOfMeasure.getUvRadiation());
                preparedStatementUOM.setString(i++, unitOfMeasure.getSnowLevel());
                preparedStatementUOM.setString(i++, unitOfMeasure.getPollutionLevel());
                preparedStatementUOM.setString(i, unitOfMeasure.getDewPoint());

                preparedStatementUOM.executeUpdate();
                ResultSet generatedKeys = preparedStatementUOM.getGeneratedKeys();
                if (generatedKeys.next()) {
                  idUnitOfMeasure = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Unable to obtain AI id of UnitOfMeasure.");
                }

                i = 1;
                preparedStatementSta.setInt(i++, idUnitOfMeasure);
                preparedStatementSta.setString(i++, station.getName());
                preparedStatementSta.setString(i++, station.getType());
                preparedStatementSta.setFloat(i++, station.getLatitude());
                preparedStatementSta.setFloat(i++, station.getLongitude());
                preparedStatementSta.setFloat(i++, station.getAltitude());

                preparedStatementSta.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (JsonMappingException e) {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": \"false\", \"text\": \"Latitude, Longitude and Altitude must be numbers!\"}");
            return;
        }

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"success\": \"true\", \"text\": \"The new station has been created succesfully. In order to see the modification you must reload the page.\"}");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("Error", "Error! GET request not supported!");
        getServletContext().getRequestDispatcher("/Error").forward(request, response);
    }
}
