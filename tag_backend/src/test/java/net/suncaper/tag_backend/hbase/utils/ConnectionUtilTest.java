package net.suncaper.tag_backend.hbase.utils;

import org.junit.jupiter.api.Test;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionUtilTest {

    @Test
    void getConn() throws IOException {
        System.out.println(ConnectionUtil.getConn());
    }
}