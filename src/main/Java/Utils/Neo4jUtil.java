package Utils;

import org.neo4j.driver.internal.InternalRecord;
import org.neo4j.driver.v1.*;


import java.util.*;

public class Neo4jUtil implements AutoCloseable{
    private static final Driver driver = buildDriver();


    private static Driver buildDriver() {
        Configuration configuration = new Configuration("neo4j.properties");
        return GraphDatabase.driver(configuration.getProperty("neo4j.url"), AuthTokens.basic(configuration.getProperty("neo4j.username"),
                configuration.getProperty("neo4j.password")));
    }

    public static Object executeSelect(final String queryString, final boolean isResultList, boolean isReturningPropertyKey, final Map<String, Object> params) {
        try ( Session session = driver.session() )
        {
            return session.readTransaction( new TransactionWork<Object>()
            {
                @Override
                public Object execute( Transaction tx )
                {
                    StatementResult statementResult = tx.run(queryString, params);
                    if (isResultList) {
                        List<Map<String, Object>> result = new ArrayList<>();

                        for (Record record : statementResult.list()) {
                            Map<String, Object> map = null;
                            if (isReturningPropertyKey) {
                                map = new HashMap<>(record.asMap());
                            } else {
                                map = new HashMap<>(record.fields().get(0).value().asNode().asMap());
                                map.put("id", record.fields().get(0).value().asNode().id());
                            }
                            result.add(map);
                        }
                        return result;
                    } else {
                        if (isReturningPropertyKey)
                            return statementResult.single().asMap();
                        else return statementResult.single().fields().get(0).value().asNode().asMap();
                    }
                }
            } );
        }
    }

    public static Object executeSelect(final String queryString, final boolean isResultList) {
        try ( Session session = driver.session() )
        {
            return session.readTransaction( new TransactionWork<Object>()
            {
                @Override
                public Object execute( Transaction tx )
                {
                    StatementResult statementResult = tx.run(queryString);
                    if (isResultList) {
                        List<Map<String,Object>> result = new ArrayList<>();

                        for (Record record : statementResult.list()) {
                            Map<String,Object> map = new HashMap<>(record.fields().get(0).value().asNode().asMap());
                            map.put("id", record.fields().get(0).value().asNode().id());
                            result.add(map);
                        }
                        return result;
                    } else return statementResult.single().fields().get(0).value().asNode().asMap();
                }
            } );
        }
    }

    public static void executeInsert(final String queryString, final Map<String,Object> params, boolean autoCommit) {
        try ( Session session = driver.session() )
        {

            if (autoCommit) session.run(queryString,params);
            else {
                session.writeTransaction(new TransactionWork<Object>() {
                    @Override
                    public Object execute(Transaction tx) {
                        StatementResult statementResult = tx.run(queryString, params);
                        return 0;
                    }
                });
            }
        }
    }

    public static void executeInsert(String queryString, boolean autoCommit) {
        try ( Session session = driver.session() )
        {
            if (autoCommit) session.run(queryString);
            else {
                session.writeTransaction(new TransactionWork<Object>() {
                    @Override
                    public Object execute(Transaction tx) {
                        StatementResult statementResult = tx.run(queryString);
                        return 0;
                    }
                });
            }
        }
    }


//    public static void executeInsert(Collection<?> data) {
//        if (!data.iterator().next().getClass().getPackage().getName().equals("Model"))
//            throw new IllegalArgumentException();
//        Session session = Neo4jUtil.getSessionFactory().openSession();
//        Transaction tr = session.beginTransaction();
//        int i = 0;
//        int batchSize = Integer.parseInt(new OgmConfiguration().configure().getProperty("hibernate.jdbc.batch_size"));
//        for (Object datum : data) {
//            i++;
//            session.save(datum);
//            if (i % batchSize == 0) {
//                session.flush();
//                session.clear();
//            }
//        }
//        try {
//            tr.commit();
//        } catch (RollbackException e) {
//            System.err.println(e.getMessage());
//            tr.rollback();
//        } finally {
//            session.close();
//        }
//    }
//
//    public static void executeInsert(Object data) {
//        if (!data.getClass().getPackage().getName().equals("Model"))
//            throw new IllegalArgumentException();
//        Session session = Neo4jUtil.getSessionFactory().openSession();
//        Transaction tr = session.beginTransaction();
//        session.save(data);
//        try {
//            tr.commit();
//        } catch (RollbackException e) {
//            System.err.println(e.getMessage());
//            tr.rollback();
//        } finally {
//            session.close();
//        }
//    }

    @Override
    public void close() throws Exception {
        driver.close();
    }
}