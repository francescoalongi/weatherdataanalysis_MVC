package Controller;

import Model.DatumForGraph;

import Model.Station;
import Utils.Collections;
import Utils.MongoDBUtil;
import com.mongodb.Mongo;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;

import javax.print.Doc;
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
            ArrayList<String> stationIds = new ArrayList<>();
            int id = 0;
            long beginTimestamp = 0L;
            long endTimestamp = 0L;
            while (request.getParameter("station" + id) != null && !request.getParameter("station" + id).isEmpty())
                stationIds.add(request.getParameter("station" + id++));
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
            FindIterable<Document> results;
            Document filter = new Document();
            for (String stationId : stationIds) {
                filter.append("_id", new ObjectId(stationId));
                results = MongoDBUtil.executeSelect(filter, Collections.STATIONS);
                Document station = results.first();

                String[] splittedWeatherDimension = request.getParameter("weatherDimension").toLowerCase().split(" ");
                String compliantWeatherDimension = splittedWeatherDimension[0].toLowerCase();
                for (int i = 1; i < splittedWeatherDimension.length; i++) {
                    char[] chars = splittedWeatherDimension[i].toCharArray();
                    chars[0] = Character.toUpperCase(chars[0]);
                    compliantWeatherDimension = compliantWeatherDimension.concat(String.valueOf(chars));
                }
                String stationName = (String) station.get("name");
                String unitOfMeasure = (String) ((Document)station.get("unitOfMeasure")).get(compliantWeatherDimension);

                filter.clear();
                filter.append("idStation", stationId);
                filter.append("timestamp", new Document("$gte", beginTimestamp).append("$lt", endTimestamp));
                results = MongoDBUtil.executeSelect(filter, Collections.DATA);

                List<Double> measurements = new ArrayList<>();
                List<Long> timestamps = new ArrayList<>();

                for (Document doc : results) {
                    measurements.add((Double) doc.get(compliantWeatherDimension));
                    timestamps.add((Long) doc.get("timestamp"));
                }

                Double avg = null;
                if (isAvgRequired) {
                    List<Bson> aggFilter = Arrays.asList(
                            Aggregates.match(filter),
                            Aggregates.group(null, Accumulators.avg("avg",
                                    "$" + compliantWeatherDimension))
                    );

                    AggregateIterable<Document> avgResult = MongoDBUtil.executeAggregate(aggFilter, Collections.DATA);
                    avg = (Double)avgResult.first().get("avg");
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
