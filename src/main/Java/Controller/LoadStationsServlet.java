package Controller;

import Model.Station;
import Utils.HibernateUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "LoadStationsServlet")
public class LoadStationsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO: An error page must be created to redirect users there when they perform illegal actions
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        List results = (List) HibernateUtil.executeSelect("FROM Station", true);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(results);

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } else {
            request.setAttribute("stations", json);
            request.getRequestDispatcher("/Homepage").forward(request, response);
        }
    }
}
