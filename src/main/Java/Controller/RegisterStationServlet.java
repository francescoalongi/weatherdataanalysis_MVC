package Controller;

import Model.Station;
import Utils.HibernateUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.util.JSONPObject;
import org.json.JSONObject;

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

@WebServlet(name = "RegisterStationServlet")
public class RegisterStationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> param = new HashMap<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        String json = br.readLine();
        JSONObject data = new JSONObject(json);
        param.put("idStation", Integer.parseInt(data.getString("idStation")));
        Station station = (Station) HibernateUtil.executeSelect("FROM Station WHERE idStation = :idStation", false, param);
        new PeriodicDataAcquirer(data.getString("URL"), station, Integer.parseInt(data.getString("timeInterval")));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("Station " + station.getName() + " successfully registered!");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}