package net.suncaper.tag_backend.hbase.utils;

import org.apache.hadoop.hbase.client.Connection;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NamespaceUtilTest {

    @Test
    void listNamespace() throws IOException {
        Connection conn = ConnectionUtil.getConn();
        List<String> strings = NamespaceUtil.listNamespace(conn);
        System.out.println(strings);
        conn.close();
    }
}