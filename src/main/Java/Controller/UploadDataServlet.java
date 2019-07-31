package Controller;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "UploadDataServlet")
@MultipartConfig
public class UploadDataServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Part filePart = request.getPart("file");
        InputStream fileContent = filePart.getInputStream();
        Reader in = new InputStreamReader(fileContent, StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
        for (CSVRecord record : records) {
            String timestamp = record.get("\uFEFFtimestamp"); // /uFEFF is the Byte Order Mark
            String firstName = record.get("humidity");
        }
        response.sendRedirect(getServletContext().getContextPath() + "/Homepage");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO: An error page must be created to redirect users there when they perform illegal actions
    }
}
