package Utils;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.cj.jdbc.*;


public class MySQLUtil{
    private static MysqlDataSource dataSource = instantiateDataSource();

    private static MysqlDataSource instantiateDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        Configuration configuration = new Configuration("mysql.properties");
        dataSource.setDatabaseName(configuration.getProperty("mysql.dbname"));
        dataSource.setUser(configuration.getProperty("mysql.username"));
        dataSource.setPassword(configuration.getProperty("mysql.password"));
        dataSource.setServerName(configuration.getProperty("mysql.servername"));
        dataSource.setPort(Integer.parseInt(configuration.getProperty("mysql.port")));
        dataSource.setURL(dataSource.getURL() + configuration.getProperty("mysql.querystring"));
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static List<Map<String,Object>> resultSetToArrayList(ResultSet rs) throws SQLException{
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        while (rs.next()){
            HashMap row = new HashMap(columns);
            for(int i=1; i<=columns; ++i){
                row.put(md.getColumnLabel(i),rs.getObject(i));
            }
            list.add(row);
        }
        return list;
    }

}