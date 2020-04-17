package Controller;

import Model.DatumForGraph;
import Utils.Neo4jUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.neo4j.driver.Record;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

                String[] splittedWeatherDimension = request.getParameter("weatherDimension").toLowerCase().split(" ");
                String compliantWeatherDimension = splittedWeatherDimension[0].toLowerCase();
                for (int i = 1; i < splittedWeatherDimension.length; i++) {
                    char[] chars = splittedWeatherDimension[i].toCharArray();
                    chars[0] = Character.toUpperCase(chars[0]);
                    compliantWeatherDimension = compliantWeatherDimension.concat(String.valueOf(chars));
                }

                Map<String, Object> param = new HashMap<>();
                param.put("idStation", stationId);
                String queryString = "MATCH (n:Station), (n)-[:HAS_ACQUIRED]->(m:Datum) WHERE id(n) = $idStation AND " +
                        "m.datetime > datetime({epochSeconds:$beginTimestamp}) AND m.datetime < datetime({epochSeconds:$endTimestamp}) RETURN ";

                String avgQueryString = queryString;

                queryString += "timestamp(m.datetime) AS timestamp, m." + compliantWeatherDimension + " AS measurement ORDER BY timestamp";

                String stationInfoQueryString = "MATCH (n:Station), (n)-[:MEASURED_USING]->(m) WHERE id(n) = $idStation " +
                        "RETURN m." + compliantWeatherDimension + " AS unitOfMeasure, n.name AS name";

                param.put("beginTimestamp", beginTimestamp);
                param.put("endTimestamp", endTimestamp);

                Map<String, Object> stationInfo = ((Record) Neo4jUtil.executeSelect(stationInfoQueryString, false, param)).asMap();
                String stationName = (String) stationInfo.get("name");
                String unitOfMeasure = (String) stationInfo.get("unitOfMeasure");

                Double avg = null;
                if (isAvgRequired) {
                    avgQueryString += "avg(m." + compliantWeatherDimension + ") AS avg";
                    Map<String, Object> avgResult = ((Record) Neo4jUtil.executeSelect(avgQueryString, false, param)).asMap();
                    avg = (Double) avgResult.get("avg");
                }

                long bTime = System.currentTimeMillis();
                List<Record> mainResults = (List<Record>) Neo4jUtil.executeSelect(queryString, true, param);
                long eTime = System.currentTimeMillis();
                System.out.println(queryString + " --> " + (eTime - bTime));

                List<Double> measurements = new ArrayList<>();
                List<Long> timestamps = new ArrayList<>();

                for (Record record : mainResults) {
                    measurements.add((Double) record.asMap().get("measurement"));
                    timestamps.add((Long) record.asMap().get("timestamp"));
                }

                if (!measurements.isEmpty()) {
                    DatumForGraph data = null;
                    data = new DatumForGraph(stationId, stationName, unitOfMeasure , measurements, timestamps , avg);
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
