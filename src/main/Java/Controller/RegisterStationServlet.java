package Controller;

import Model.Station;
import Utils.HibernateUtil;
import Utils.PeriodicDataAcquirer;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "RegisterStationServlet")
public class RegisterStationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> param = new HashMap<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        JSONObject data = new JSONObject(br.readLine());
        URL url;
        try {
             url = new URL(data.getString("URL"));
        } catch (MalformedURLException e) {
            response.getWriter().write("{\"success\": \"false\", \"text\": \"The URL must be valid.\"}");
            return;
        }
        Integer timeInterval;
        try {
            timeInterval = Integer.parseInt(data.getString("timeInterval"));
        } catch (NumberFormatException e) {
            response.getWriter().write("{\"success\": \"false\", \"text\": \"The time interval must be an integer.\"}");
            return;
        }

        param.put("idStation", Integer.parseInt(data.getString("idStation")));
        Station station = (Station) HibernateUtil.executeSelect("FROM Station WHERE idStation = :idStation", false, param);
        new PeriodicDataAcquirer(url.toString(), station, timeInterval);
        response.getWriter().write("{\"success\": \"true\", \"text\": \"Station " + station.getName() + " successfully registered!\"}");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("Error", "Error! GET request not supported");
        getServletContext().getRequestDispatcher("/Error").forward(request, response);
    }
}