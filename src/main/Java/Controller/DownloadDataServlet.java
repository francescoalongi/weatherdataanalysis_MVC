package Controller;

import Model.*;
import Utils.MySQLUtil;
import com.fasterxml.jackson.databind.ObjectMapper;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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

        String stationQuery = "SELECT * FROM station AS s WHERE s.idStation = ?";
        String unitOfMeasureQuery = "SELECT * FROM unitofmeasure AS u WHERE u.idUnitOfMeasure = ?";
        List<Map<String,Object>> results = new ArrayList<>();
        try (Connection connection = MySQLUtil.getConnection();
             PreparedStatement preparedStatementSta = connection.prepareStatement(stationQuery);
             PreparedStatement preparedStatementUOM = connection.prepareStatement(unitOfMeasureQuery)) {
            preparedStatementSta.setInt(1, idStation);
            ResultSet rs = preparedStatementSta.executeQuery();
            results = MySQLUtil.resultSetToArrayList(rs);
            preparedStatementUOM.setInt(1, (Integer) results.get(0).get("idUnitOfMeasure"));
            rs = preparedStatementUOM.executeQuery();
            results.get(0).put("unitOfMeasure", MySQLUtil.resultSetToArrayList(rs).get(0));
            results.get(0).remove("idUnitOfMeasure");
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ObjectMapper mapper = new ObjectMapper();
        Station station = mapper.convertValue(results.get(0), Station.class);

        String table = "";

        switch (station.getType().toLowerCase()) {
            case "country":
                table = "DatumCountry";
                break;
            case "city":
                table = "DatumCity";
                break;
            case "mountain":
                table = "DatumMountain";
                break;
            case "sea":
                table = "DatumSea";
                break;
        }

        String selectionQuery = "SELECT * FROM " + table + " WHERE idStation = ? AND timestamp >= ? AND timestamp <= ?";

        try (Connection connection = MySQLUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectionQuery)) {
            preparedStatement.setInt(1, idStation);
            preparedStatement.setLong(2, beginTimestamp);
            preparedStatement.setLong(3, endTimestamp);
            ResultSet rs = preparedStatement.executeQuery();
            results = MySQLUtil.resultSetToArrayList(rs);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List data = new ArrayList<>();
        for (Map<String, Object> map : results) {
            map.put("type", "Datum"+station.getType());
            data.add(mapper.convertValue(map, Datum.class));

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
