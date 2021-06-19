package net.suncaper.tag_backend.hbase.utils;

import org.apache.hadoop.hbase.client.Connection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class TableUtilTest {
    Connection conn = null;

    @BeforeEach
    void setUp() throws IOException {
        conn = ConnectionUtil.getConn();
    }

    @AfterEach
    void tearDown() throws IOException {
        conn.close();
    }

    @Test
    void get() throws IOException {
        Connection conn = ConnectionUtil.getConn();
        List<String> json = TableUtil.get(conn, "user_profile", null, "99");
        System.out.println(json);
        conn.close();
    }

    @Test
    void scan() throws IOException {
        Connection conn = ConnectionUtil.getConn();
        List<List<String>> json = TableUtil.scan(conn, "user_profile", null);
        System.out.println(json);
        conn.close();
    }


    @Test
    void getValueByRowkey() throws IOException {
        TableUtil.getValueByRowkey("user_profile", null, "99");
    }

    @Test
    void getTreeByRowkey() throws IOException {
        System.out.println(TableUtil.getTreeByRowkey("user_profile", null, "99"));
    }

    @Test
    void qualifierFilter() throws IOException {
        TableUtil.qualifierFilter("user_profile", "gender");
    }

    @Test
    void getGender() throws IOException {
        System.out.println(TableUtil.getGender());
    }

    @Test
    void getAgeGroup() throws IOException {
        System.out.println(TableUtil.getAgeGroup());
    }

    @Test
    void getPoliticalStatus() throws IOException {
        System.out.println(TableUtil.getPoliticalStatus());
    }

    @Test
    void getJob() throws IOException {
        System.out.println(TableUtil.getJob());
    }

    @Test
    void filterList() throws IOException {
        System.out.println(TableUtil.filterList(Arrays.asList("gender", "job"), Arrays.asList("男", "学生")));
    }

    @Test
    void getConsumeCycle() throws IOException {
        System.out.println(TableUtil.getConsumeCycle());
    }

    @Test
    void getAvgOrderAmount() throws IOException {
        System.out.println(TableUtil.getAvgOrderAmount());
    }
}