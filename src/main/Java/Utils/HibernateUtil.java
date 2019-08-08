package Utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.RollbackException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class HibernateUtil
{
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory()
    {
        try
        {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
            //return new AnnotationConfiguration().configure(new File("hibernate.cgf.xml")).buildSessionFactory();
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        sessionFactory.close();
    }

    public static Object executeSelect(String queryString, boolean isResultList, Map<String, Object> params) {
        //Params <- null => no parameters
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        Query query = session.createQuery(queryString);
        if ((params == null && !query.getParameters().isEmpty()) ||
                params != null && query.getParameters().isEmpty())
            throw new IllegalArgumentException();
        if (params != null)
            for (Map.Entry<String, Object> entry : params.entrySet())
                query.setParameter(entry.getKey(), entry.getValue());
        Object results;
        if (isResultList)
            results = query.list();
        else
            results = query.getSingleResult();
        try {
            tr.commit();
        } catch (RollbackException e) {
            System.err.println(e.getMessage());
            tr.rollback();
        } finally {
            session.close();
        }
        return results;
    }

    public static Object executeSelect(String queryString, boolean isResultList) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        Query query = session.createQuery(queryString);
        Object results;
        if (isResultList)
            results = query.list();
        else
            results = query.getSingleResult();
        try {
            tr.commit();
        } catch (RollbackException e) {
            System.err.println(e.getMessage());
            tr.rollback();
        } finally {
            session.close();
        }
        return results;
    }

    public static void executeInsert(Collection<?> data) {
        if (!data.iterator().next().getClass().getPackage().getName().equals("Model"))
            throw new IllegalArgumentException();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        int i = 0;
        for (Object datum : data) {
            session.save(datum);
            if (i % Integer.parseInt(new Configuration().configure().getProperty("hibernate.jdbc.batch_size")) == 0) {
                session.flush();
                session.clear();
            }
            i++;
        }
        try {
            tr.commit();
        } catch (RollbackException e) {
            System.err.println(e.getMessage());
            tr.rollback();
        } finally {
            session.close();
        }
    }

    public static void executeInsert(Object data) {
        if (!data.getClass().getPackage().getName().equals("Model"))
            throw new IllegalArgumentException();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        session.save(data);
        try {
            tr.commit();
        } catch (RollbackException e) {
            System.err.println(e.getMessage());
            tr.rollback();
        } finally {
            session.close();
        }
    }

}
