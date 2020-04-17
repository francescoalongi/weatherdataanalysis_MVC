package Controller;

import Model.*;
import Utils.Neo4jUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.neo4j.driver.Record;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;

@WebServlet(name = "DownloadDataServlet")
public class DownloadDataServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("Error", "Error! POST request not supported");
        getServletContext().getRequestDispatcher("/Error").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long beginTimestamp = 0L;// = Long.parseLong(request.getParameter("begin_timestamp")) / 1000; //1565340900L;
        long endTimestamp = 0L;// = Long.parseLong(request.getParameter("end_timestamp")) / 1000;  //1565343600L;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date begin_date = dateFormat.parse(request.getParameter("begin_date"));
            Date end_date = dateFormat.parse(request.getParameter("end_date"));
            beginTimestamp = begin_date.getTime() / 1000;
            endTimestamp = end_date.getTime() / 1000;
        } catch (Exception e) { //this generic but you can control another types of exception
            // look the origin of exception
        }
        Integer idStation = Integer.parseInt(request.getParameter("station_id"));

        Map<String, Object> param = new HashMap<>();
        param.put("idStation", idStation);

        String stationInfoQueryString = "MATCH (n:Station) WHERE id(n) = $idStation RETURN n";
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> result = ((Record) Neo4jUtil.executeSelect(stationInfoQueryString, false, param)).get(0).asMap();
        Station station = mapper.convertValue(result, Station.class);

        param.put("beginTimestamp", beginTimestamp);
        param.put("endTimestamp", endTimestamp);

        String queryString = "MATCH (n:Station), (n)-[:HAS_ACQUIRED]->(m:Datum) WHERE id(n) = $idStation AND " +
                "m.datetime > datetime({epochSeconds:$beginTimestamp}) AND m.datetime < datetime({epochSeconds:$endTimestamp}) RETURN m";

        //retrieve the data required
        List<Record> results = (List<Record>) Neo4jUtil.executeSelect(queryString, true, param);

        List data = new ArrayList<>();
        Map<String,Object> recordMap = new HashMap<>();
        Map<String,Object> map = new HashMap<>();
        for (Record record : results) {
            map = record.get(0).asMap();
            ZonedDateTime datetime = (ZonedDateTime) map.get("datetime");
            recordMap.put("idStation", map.get("id"));
            recordMap.put("timestamp", datetime.toEpochSecond());
            recordMap.put("temperature", map.get("temperature"));
            recordMap.put("pressure", map.get("pressure"));
            recordMap.put("humidity", map.get("humidity"));
            recordMap.put("rain", map.get("rain"));
            recordMap.put("windModule", map.get("windModule"));
            recordMap.put("windDirection", map.get("windDirection"));
            switch (station.getType().toLowerCase()) {
                case "city":
                    recordMap.put("pollutionLevel", map.get("pollutionLevel"));
                    break;
                case "country":
                    recordMap.put("dewPoint", map.get("dewPoint"));
                    break;
                case "mountain":
                    recordMap.put("snowLevel", map.get("snowLevel"));
                    break;
                case "sea":
                    recordMap.put("uvRadiation", map.get("uvRadiation"));
                    break;
            }
            recordMap.put("type", "Datum"+station.getType());
            data.add(mapper.convertValue(recordMap, Datum.class));
        }

        if (data.size() == 0) {
            request.setAttribute("Error", "The csv file generated is empty! Please redo the procedure selecting other dates");
            getServletContext().getRequestDispatcher("/Error").forward(request, response);
            return;
        }
        //create the file
        PrintWriter printWriter = new PrintWriter("data_" + request.getParameter("begin_date").replace('/', '-') + "_" + request.getParameter("end_date").replace('/', '-') + ".csv");
        printWriter.println(((Datum) data.get(0)).getFieldsNameAsCSV());
        for (Object datum : data)
            printWriter.println(((Datum) datum).getFieldsAsCSV());
        printWriter.close();
        //download the file
        response.setContentType("Application/CSV");
        response.setHeader("Content-Disposition", "attachment; filename=" + "data_" + request.getParameter("begin_date").replace('/', '-') + "_" + request.getParameter("end_date").replace('/', '-') + ".csv");
        OutputStream out = response.getOutputStream();
        FileInputStream in = new FileInputStream("data_" + request.getParameter("begin_date").replace('/', '-') + "_" + request.getParameter("end_date").replace('/', '-') + ".csv");
        byte[] buffer = new byte[4096];
        int length;
        while ((length = in.read(buffer)) > 0)
            out.write(buffer, 0, length);
        in.close();
        out.flush();
    }
}
