package Controller;

import Model.DatumCountry;
import Model.DatumForGraph;
import Model.Station;
import Model.UnitOfMeasure;
import Utils.MySQLUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "QueryDataGraphServlet")
public class QueryDataGraphServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("Error", "Error! POST request not supported");
        getServletContext().getRequestDispatcher("/Error").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // the back-end side must check whether at least one station, the weather dimension and both start date and end date
        // are filled. Moreover it must check if the chosen weather dimension match the type of all the selected stations (already
        // checked at client-side). A full query string sent to the server has the following format:
        // "?station0=1&station1=2&weatherDimension=Temperature&startDate=09/08/2019&endDate=23/08/2019&showAvg=true&showVar=true"
        // where the right-hand side of the stations parameter is the id of the station.
        // If a parameter is missing from the query string, it means that the user has not selected it.
        //check parameters
        if (request.getParameter("station0") == null || request.getParameter("station0").isEmpty() ||
                request.getParameter("weatherDimension") == null || request.getParameter("weatherDimension").isEmpty() ||
                request.getParameter("startDate") == null || request.getParameter("startDate").isEmpty() ||
                request.getParameter("endDate") == null || request.getParameter("endDate").isEmpty()) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": \"false\", \"text\": \"You have to fill all the form to get a result.\"}");
        } else {
            boolean isAvgRequired = false;
            if (request.getParameter("showAvg") != null && request.getParameter("showAvg").equals("true"))
                isAvgRequired = true;
            ArrayList<Integer> stationIds = new ArrayList<>();
            int id = 0;
            long beginTimestamp = 0L;
            long endTimestamp = 0L;
            while (request.getParameter("station" + id) != null && !request.getParameter("station" + id).isEmpty())
                stationIds.add(Integer.valueOf(request.getParameter("station" + id++)));
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date begin_date = dateFormat.parse(request.getParameter("startDate"));
                Date end_date = dateFormat.parse(request.getParameter("endDate"));
                beginTimestamp = begin_date.getTime() / 1000;
                endTimestamp = end_date.getTime() / 1000;
            } catch (Exception e) { //this generic but you can control another types of exception
                // look the origin of exception
            }

            List<DatumForGraph> dataOfStations = new ArrayList<>();
            for (Integer stationId : stationIds) {

                String stationQuery = "SELECT * FROM station AS s WHERE s.idStation = ?";
                String unitOfMeasureQuery = "SELECT * FROM unitofmeasure AS u WHERE u.idUnitOfMeasure = ?";
                List<Map<String,Object>> results = new ArrayList<>();
                try (Connection connection = MySQLUtil.getConnection();
                     PreparedStatement preparedStatementSta = connection.prepareStatement(stationQuery);
                     PreparedStatement preparedStatementUOM = connection.prepareStatement(unitOfMeasureQuery)) {
                    preparedStatementSta.setInt(1, stationId);
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

                String[] splittedWeatherDimension = request.getParameter("weatherDimension").toLowerCase().split(" ");
                String compliantWeatherDimension = splittedWeatherDimension[0].toLowerCase();
                for (int i = 1; i < splittedWeatherDimension.length; i++) {
                    char[] chars = splittedWeatherDimension[i].toCharArray();
                    chars[0] = Character.toUpperCase(chars[0]);
                    compliantWeatherDimension = compliantWeatherDimension.concat(String.valueOf(chars));
                }

                String table = "";

                switch (station.getType().toLowerCase()) {
                    case "country":
                        table = "DatumCountry";
                        break;
                    case "city":
                        table = "DatumCity";
                        break;
                    case "mountain":
                        table = "DatumMountain";
                        break;
                    case "sea":
                        table = "DatumSea";
                        break;
                }

                String selectionQuery = "SELECT timestamp, " + compliantWeatherDimension + " AS measurement";
                selectionQuery += " FROM " + table + " WHERE idStation = ? AND timestamp >= ? AND timestamp <= ?";

                try (Connection connection = MySQLUtil.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(selectionQuery)) {
                    preparedStatement.setInt(1, stationId);
                    preparedStatement.setLong(2, beginTimestamp);
                    preparedStatement.setLong(3, endTimestamp);
                    ResultSet rs = preparedStatement.executeQuery();
                    results = MySQLUtil.resultSetToArrayList(rs);
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                String stationName = station.getName();
                String weatherDimForReflection = compliantWeatherDimension.substring(0, 1).toUpperCase() +
                        compliantWeatherDimension.substring(1);
                UnitOfMeasure unitOfMeasure = station.getUnitOfMeasure();
                String unit = "";
                try {
                    unit = (String) unitOfMeasure.getClass().getMethod("get" + weatherDimForReflection).invoke(unitOfMeasure);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }

                List<Float> measurements = new ArrayList<>();
                List<Long> timestamps = new ArrayList<>();

                for (Map<String, Object> map : results) {
                    measurements.add((Float) map.get("measurement"));
                    timestamps.add((((Number)map.get("timestamp")).longValue())*1000);
                }

                Double avg = null;
                if (isAvgRequired) {
                    String avgQuery = "SELECT avg(" + compliantWeatherDimension + ") FROM " + table + " WHERE idStation = ? AND timestamp >= ? AND timestamp <= ?";
                    try (Connection connection = MySQLUtil.getConnection();
                         PreparedStatement preparedStatement = connection.prepareStatement(avgQuery)) {
                        preparedStatement.setInt(1, stationId);
                        preparedStatement.setLong(2, beginTimestamp);
                        preparedStatement.setLong(3, endTimestamp);
                        ResultSet rs = preparedStatement.executeQuery();
                        results = MySQLUtil.resultSetToArrayList(rs);
                        rs.close();
                        avg = (Double) results.get(0).get("avg(" + compliantWeatherDimension + ")");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (!measurements.isEmpty()) {
                    DatumForGraph data = null;
                    data = new DatumForGraph(stationId, stationName, unit , measurements, timestamps , avg);
                    dataOfStations.add(data);
                }
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            if (dataOfStations.isEmpty()) {
                response.getWriter().write("{\"success\": \"false\", \"text\": \"There are no data for the selected station in the selected time frame. Try to change the request parameter or to upload some data.\"}");
            } else {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(dataOfStations);
                response.getWriter().write("{\"success\": \"true\", \"text\":" + json + "}");
            }
        }
    }
}
