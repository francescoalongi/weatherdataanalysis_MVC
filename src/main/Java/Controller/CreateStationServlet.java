package Controller;

import Utils.Neo4jUtil;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static Utils.Maps.asFlattendMap;

@WebServlet(name = "CreateStationServlet")
public class CreateStationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        String json = br.readLine();
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> params = mapper.readValue(json, HashMap.class);
            String queryString = "CREATE (n:Station {name: $name, type: $type, latitude: $latitude, longitude: $longitude, altitude: $altitude}), " +
                    " (m:UnitOfMeasure {temperature: $temperature, pressure: $pressure, humidity: $humidity, rain: $rain, windModule: $windModule, " +
                    "windDirection: $windDirection, ";
            switch ((String)params.get("type")) {
                case "Country":
                    queryString += "dewPoint: $dewPoint})";
                    break;
                case "City":
                    queryString += "pollutionLevel: $pollutionLevel})";
                    break;
                case "Mountain":
                    queryString += "snowLevel: $snowLevel})";
                    break;
                case "Sea":
                    queryString += "uvRadiation: $uvRadiation})";
            }
            queryString += ", (n)-[:MEASURED_USING]->(m)";
            Map<String, Object> t_params = asFlattendMap(params);
            Neo4jUtil.executeInsert(queryString, t_params, false);
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
