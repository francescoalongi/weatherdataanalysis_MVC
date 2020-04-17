package Controller;

import Utils.Collections;
import Utils.MongoDBUtil;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;

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


@WebServlet(name = "CreateStationServlet")
public class CreateStationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        String json = br.readLine();
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> params = mapper.readValue(json, HashMap.class);
            MongoDBUtil.executeInsert(new Document(params), Collections.STATIONS);
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
