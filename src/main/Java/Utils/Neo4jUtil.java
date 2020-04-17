package Utils;

import org.neo4j.driver.*;
import org.neo4j.driver.summary.ResultSummary;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Neo4jUtil implements AutoCloseable{
    private static final Driver driver = buildDriver();
    public static String neo4jPath;

    private static Driver buildDriver() {
        Configuration configuration = new Configuration("neo4j.properties");
        neo4jPath = configuration.getProperty("neo4j.path");
        return GraphDatabase.driver(configuration.getProperty("neo4j.url"), AuthTokens.basic(configuration.getProperty("neo4j.username"),
                configuration.getProperty("neo4j.password")));
    }

    public static Object executeSelect(final String queryString, final boolean isResultList, final Map<String, Object> params) {
        try ( Session session = driver.session() )
        {
            return session.readTransaction( new TransactionWork<Object>()
            {
                @Override
                public Object execute( Transaction tx )
                {
                    Result statementResult = tx.run(queryString, params);
                    if (isResultList) return statementResult.list();
                    else return statementResult.single();
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
                    if (isResultList) return statementResult.list();
                    else return statementResult.single();
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
                        tx.run(queryString, params);
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
                        tx.run(queryString);
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