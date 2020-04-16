package Utils;

import org.neo4j.driver.*;

import java.util.*;

public class Neo4jUtil implements AutoCloseable{
    private static final Driver driver = buildDriver();
    public static String neo4jPath;

    private static Driver buildDriver() {
        Configuration configuration = new Configuration("neo4j.properties");
        neo4jPath = configuration.getProperty("neo4j.path");
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

                    Result statementResult = tx.run(queryString, params);
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
                    Result statementResult = tx.run(queryString);
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
                        Result statementResult = tx.run(queryString, params);
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
                        Result statementResult = tx.run(queryString);
                        return 0;
                    }
                });
            }
        }
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }
}