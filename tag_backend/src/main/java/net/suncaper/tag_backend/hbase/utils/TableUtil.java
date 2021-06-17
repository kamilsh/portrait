package net.suncaper.tag_backend.hbase.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableUtil {
    public static Table getTable(Connection conn, String table, String namespace) throws IOException {
        TableName tableName = TableName.valueOf(namespace, table);
        return conn.getTable(tableName);
    }

    public static List<String> get(Connection conn, String tableName, String namespace, String rowkey) throws IOException {
        List<String> output = new ArrayList<>();
        Table table = getTable(conn, tableName, namespace);
        Get get = new Get(Bytes.toBytes(rowkey));
        Result result = table.get(get);
        if (result != null) {
            output = getResultString(result);
        }
        table.close();
        return output;
    }

    public static List<List<String>> scan(Connection conn, String tableName, String namespace) throws IOException {
        List<List<String>> output = new ArrayList<>();
        Table table = getTable(conn, tableName, namespace);
        Scan scan = new Scan();
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            output.add(getResultString(result));
        }
        table.close();
        return output;
    }

    private static List<String> getResultString(Result result) {
        List<String> list = new ArrayList<>();
        String output = "";
        Cell[] cells = result.rawCells();
        Map<String, Object> map = new HashMap<>();
        for (Cell cell:cells) {
            String row = Bytes.toString(CellUtil.cloneRow(cell));
            String family = Bytes.toString(CellUtil.cloneFamily(cell));
            String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
            String value = Bytes.toString(CellUtil.cloneValue(cell));
//            System.out.printf("row: %s, family: %s, qualifier: %s, value: %s%n", row, family, qualifier, value);
            map.put("row", row);
            map.put("family", family);
            map.put("qualifier", qualifier);
            map.put("value", value);
            output = new JSONObject(map).toJSONString();
            list.add(output);
        }
        return list;
    }

    public static List<String> getValueByRowkey(String tableName, String namespace, String rowkey) throws IOException {
        Connection conn = ConnectionUtil.getConn();
        List<String> output = new ArrayList<>();
        Table table = getTable(conn, tableName, namespace);
        Get get = new Get(Bytes.toBytes(rowkey));
        Result result = table.get(get);
        if (result != null) {
            Cell[] cells = result.rawCells();
            for (Cell cell : cells) {
                String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
                if ("purchase_goods".equals(qualifier))
                    continue;
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                String[] strings = value.split(",");
                for (String string : strings) {
                    output.add(string);
                }
            }
        }
        conn.close();
        return output;
    }
}
