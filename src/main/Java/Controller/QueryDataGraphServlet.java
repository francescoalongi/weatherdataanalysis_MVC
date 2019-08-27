package Controller;

import Model.DatumForGraph;
import Model.Station;
import Model.UnitOfMeasure;
import Utils.HibernateUtil;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "QueryDataGraphServlet")
public class QueryDataGraphServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // the back-end side must check whether at least one station, the weather dimension and both start date and end date
        // are filled. Moreover it must check if the chosen weather dimension match the type of all the selected stations (already
        // checked at client-side). A full query string sent to the server has the following format:
        // "?station0=1&station1=2&weatherDimension=Temperature&startDate=09/08/2019&endDate=23/08/2019&showAvg=true&showVar=true"
        // where the right-hand side of the stations parameter is the id of the station.
        // If a parameter is missing from the query string, it means that the user has not selected it.
        //check parameters
        if (request.getParameter("station0").isEmpty() || request.getParameter("weatherDimension").isEmpty() ||
                request.getParameter("startDate").isEmpty() || request.getParameter("endDate").isEmpty()) {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": \"false\", \"text\": \"You have to fill all the form to get a result.\"}");
        } else {
            ArrayList<Integer> stationIds = new ArrayList<>();
            int id = 0;
            Long beginTimestamp = 0L;
            Long endTimestamp = 0L;
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
                Map<String, Object> param = new HashMap<>();
                param.put("idStation", stationId);
                Station station = (Station) HibernateUtil.executeSelect(
                        "from Station where idStation = :idStation", false, param);
                param.put("begin_timestamp", beginTimestamp);
                param.put("end_timestamp", endTimestamp);
                String getDataToDownloadQuery = "where d.datumPK.timestamp between :begin_timestamp AND :end_timestamp AND d.datumPK.station.id = :idStation";
                switch (station.getType().toLowerCase()) {
                    case "city":
                        getDataToDownloadQuery = "from DatumCity as d " + getDataToDownloadQuery;
                        break;
                    case "country":
                        getDataToDownloadQuery = "from DatumCountry as d " + getDataToDownloadQuery;
                        break;
                    case "mountain":
                        getDataToDownloadQuery = "from DatumMountain as d " + getDataToDownloadQuery;
                        break;
                    case "sea":
                        getDataToDownloadQuery = "from DatumSea as d " + getDataToDownloadQuery;
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                //retrieve the data required
                String getTimestampQuery = "select d.datumPK.timestamp*1000 " + getDataToDownloadQuery;
                String getMeasurementsQuery = "select " + request.getParameter("weatherDimension").toLowerCase()
                        + " " + getDataToDownloadQuery;
                List<Float> measurements =
                        (List<Float>) HibernateUtil.executeSelect(getMeasurementsQuery, true, param);
                List<Long> timestamp =
                        (List<Long>) HibernateUtil.executeSelect(getTimestampQuery, true, param);
                String weatherDimForReflection = request.getParameter("weatherDimension").substring(0, 1).toUpperCase() +
                        request.getParameter("weatherDimension").substring(1).toLowerCase();
                DatumForGraph data = null;
                UnitOfMeasure unitOfMeasure = station.getUnitOfMeasure();
                try {
                    data = new DatumForGraph(stationId, station.getName(), (String) unitOfMeasure.getClass()
                            .getMethod("get" + weatherDimForReflection).invoke(unitOfMeasure), measurements, timestamp);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
                dataOfStations.add(data);
            }
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(dataOfStations);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }
}
