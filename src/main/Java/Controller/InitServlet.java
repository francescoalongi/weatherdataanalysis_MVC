package Controller;

import Model.*;
import Utils.HibernateUtil;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

@WebServlet(name = "InitServlet", urlPatterns = "/")
public class InitServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        /*
        // instantiate session
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        //Country station
        UnitOfMeasure unitOfMeasureCountry = new UnitOfMeasure("°C","hPa","%", "mm/m^2", "km/h", "wind rose","°C", null, null, null);
        Set<Datum> datumSetCountry = new HashSet<>();

        Station stationCountry = new Station(unitOfMeasureCountry,"valmorea", "country", (float)34.0, (float)34, (float)109);

        DatumCountry datumCountry = new DatumCountry(Long.valueOf(1556909), (float)12, (float)1010.2, (float)80, (float)3, (float)4, "NNE", (float)6);
        DatumCountry datumCountry2 = new DatumCountry(Long.valueOf(15569309), (float)15, (float)1010.2, (float)81, (float)3, (float)4, "SEE", (float)6);

        // populate first the many side of the relationship, then populate the one side of the relationship.
        datumCountry.setStation(stationCountry);
        datumCountry2.setStation(stationCountry);
        datumSetCountry.add(datumCountry);
        datumSetCountry.add(datumCountry2);
        stationCountry.setDatumSet(datumSetCountry);

        //Sea station
        UnitOfMeasure unitOfMeasureSea = new UnitOfMeasure("°C","hPa","%", "mm/m^2", "km/h", "wind rose",null, "mW/cm2", null, null);
        Set<Datum> datumSetSea = new HashSet<>();

        Station stationSea = new Station(unitOfMeasureSea,"menfi", "sea", (float)34.0, (float)34, (float)109);

        DatumSea datumSea = new DatumSea(Long.valueOf(1556909), (float)12, (float)1010.2, (float)80, (float)3, (float)4, "NNE", (float)6);
        DatumSea datumSea2 = new DatumSea(Long.valueOf(15569309), (float)15, (float)1010.2, (float)81, (float)3, (float)4, "SEE", (float)6);

        // populate first the many side of the relationship, then populate the one side of the relationship.
        datumSea.setStation(stationSea);
        datumSea2.setStation(stationSea);
        datumSetSea.add(datumSea);
        datumSetSea.add(datumSea2);
        stationSea.setDatumSet(datumSetSea);

        //Mountain station
        UnitOfMeasure unitOfMeasureMountain = new UnitOfMeasure("°C","hPa","%", "mm/m^2", "km/h", "wind rose",null, null, "cm", null);
        Set<Datum> datumSetMountain = new HashSet<>();

        Station stationMountain = new Station(unitOfMeasureMountain,"Livigno", "mountain", (float)34.0, (float)34, (float)109);

        DatumMountain datumMountain = new DatumMountain(Long.valueOf(1556909), (float)12, (float)1010.2, (float)80, (float)3, (float)4, "NNE", (float)6);
        DatumMountain datumMountain2 = new DatumMountain(Long.valueOf(15569309), (float)15, (float)1010.2, (float)81, (float)3, (float)4, "SEE", (float)6);

        // populate first the many side of the relationship, then populate the one side of the relationship.
        datumMountain.setStation(stationMountain);
        datumMountain2.setStation(stationMountain);
        datumSetMountain.add(datumMountain);
        datumSetMountain.add(datumMountain2);
        stationMountain.setDatumSet(datumSetMountain);


        //City station
        UnitOfMeasure unitOfMeasureCity = new UnitOfMeasure("°C","hPa","%", "mm/m^2", "km/h", "wind rose",null, null, null, "ppm");
        Set<Datum> datumSetCity = new HashSet<>();

        Station stationCity = new Station(unitOfMeasureCity,"Milano", "city", (float)34.0, (float)34, (float)109);

        DatumCity datumCity = new DatumCity(Long.valueOf(1556909), (float)12, (float)1010.2, (float)80, (float)3, (float)4, "NNE", (float)6);
        DatumCity datumCity2 = new DatumCity(Long.valueOf(15569309), (float)15, (float)1010.2, (float)81, (float)3, (float)4, "SEE", (float)6);

        // populate first the many side of the relationship, then populate the one side of the relationship.
        datumCity.setStation(stationCity);
        datumCity2.setStation(stationCity);
        datumSetCity.add(datumCity);
        datumSetCity.add(datumCity2);
        stationCity.setDatumSet(datumSetCity);

        session.save(stationCountry);
        session.save(stationSea);
        session.save(stationMountain);
        session.save(stationCity);

        //Commit the transaction
        session.getTransaction().commit();
        HibernateUtil.shutdown();*/

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*PeriodicDataAcquirer.getInstance("http://meteovalmorea.it/ftp/realtime.csv",
                (Station) HibernateUtil.executeSelect("from Station", false));*/
        resp.sendRedirect(getServletContext().getContextPath() + "/Homepage");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
