package Controller;

import Model.MinimizedStation;
import Utils.Collections;
import Utils.MongoDBUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "LoadStationsServlet")
public class LoadStationsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        FindIterable<Document> results = MongoDBUtil.executeSelect(new Document(), Collections.STATIONS);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<MinimizedStation> list = new ArrayList<>();
        for (Document doc : results) {
            // transform the ObjectId object into a plain String containing the id, used in order to correctly map the
            // json object to the POJO class
            doc.append("_id", doc.get("_id").toString());
            list.add(mapper.convertValue(doc, MinimizedStation.class));
        }
        String json = mapper.writeValueAsString(list);

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } else {
            // this branch is taken only if the request is a non-ajax request, this happens only when we need to reload the Homepage
            request.setAttribute("stations", json);
            //when the QueryDataGraphServlet will be ready, this servlet must forward to that servlet
            request.getRequestDispatcher("/Homepage").forward(request, response);
        }
    }
}