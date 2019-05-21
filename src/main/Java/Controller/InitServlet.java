package Controller;

import Model.*;
import Utils.HibernateUtil;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.HashSet;
import java.util.Set;

@WebServlet(name = "InitServlet", urlPatterns = "/")
public class InitServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {

        // instantiate session
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();


        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("°C","hPa","%", "mm/m^2", "km/h", "wind rose","°C", null, null, null);
        Set<Datum> datumSet = new HashSet<>();

        Station station = new Station(unitOfMeasure,"valmorea", "country", (float)34.0, (float)34, (float)109);

        DatumCountry datumCountry = new DatumCountry(Long.valueOf(1556909), (float)12, (float)1010.2, (float)80, (float)3, (float)4, "NNE", (float)6);
        DatumCountry datumCountry2 = new DatumCountry(Long.valueOf(15569309), (float)15, (float)1010.2, (float)81, (float)3, (float)4, "SEE", (float)6);

        // populate first the many side of the relationship, then populate the one side of the relationship.
        datumCountry.setStation(station);
        datumCountry2.setStation(station);
        datumSet.add(datumCountry);
        datumSet.add(datumCountry2);
        station.setDatumSet(datumSet);
//session.remove();
        session.save(station);

        //Commit the transaction
        session.getTransaction().commit();
        HibernateUtil.shutdown();

        /*Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();


        UnitOfMeasure unitOfMeasure = new UnitOfMeasure("°C","hPa","%", "mm/m^2", "km/h", "wind rose",null, "bau", null, null);
        Set<DatumSea> datumCountrySet = new HashSet<>();

        Station station = new Station(unitOfMeasure,"valmorea", "sea", (float)34.0, (float)34, (float)109);

        DatumCountry datumCountry = new DatumCountry(Long.valueOf(1556909), (float)12, (float)1010.2, (float)80, (float)3, (float)4, "NNE", (float)6);
        DatumCountry datumCountry2 = new DatumCountry(Long.valueOf(15569309), (float)15, (float)1010.2, (float)81, (float)3, (float)4, "SEE", (float)6);

        // populate first the many side of the relationship, then populate the one side of the relationship.
        datumCountry.setStation(station);
        datumCountry2.setStation(station);
        datumCountrySet.add(datumCountry);
        datumCountrySet.add(datumCountry2);
        station.setDatumCountrySet(datumCountrySet);
//session.remove();
        session.save(station);

        //Commit the transaction
        session.getTransaction().commit();
        HibernateUtil.shutdown();*/
    }
}
