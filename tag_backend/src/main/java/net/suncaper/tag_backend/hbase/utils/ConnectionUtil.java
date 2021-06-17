package net.suncaper.tag_backend.hbase.utils;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class ConnectionUtil {
    public static Connection getConn() throws IOException {
        Connection connection = ConnectionFactory.createConnection();
        return connection;
    }

    public static void close(Connection connection) throws IOException {
        if (null != connection) {
            connection.close();
        }
    }
}
