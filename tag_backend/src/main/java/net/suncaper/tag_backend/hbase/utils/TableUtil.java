package net.suncaper.tag_backend.hbase.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.*;

public class TableUtil {
    private static final String user_profile = "user_profile";

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
        String output;
        Cell[] cells = result.rawCells();
        Map<String, Object> map = new HashMap<>();
        for (Cell cell : cells) {
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

    public static List<JSONObject> getValueByRowkey(String tableName, String namespace, String rowkey) throws IOException {
        List<JSONObject> output = new ArrayList<>();
        Random random = new Random();

        Connection conn = ConnectionUtil.getConn();
        Table table = getTable(conn, tableName, namespace);
        Get get = new Get(Bytes.toBytes(rowkey));
        Result result = table.get(get);
        if (result != null) {
            Cell[] cells = result.rawCells();
            for (Cell cell : cells) {
                String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                String[] strings = value.split(",");
                for (String s : strings) {
                    int randomValue = random.nextInt(5000) + 1000;
                    if ("purchase_goods".equals(qualifier) || "browse_products".equals(qualifier))
                        randomValue = random.nextInt(500) + 100;
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", s);
                    map.put("value", randomValue);
                    output.add(new JSONObject(map));
                }
            }
        }
        conn.close();
        System.out.println(output);
        return output;
    }

    public static List<JSONObject> getTreeByRowkey(String tableName, String namespace, String rowkey) throws IOException {
        List<JSONObject> qualifyList = new ArrayList<>();
        List<JSONObject> familyList = new ArrayList<>();
        List<JSONObject> rowkeyList = new ArrayList<>();
        // 取hbase的行
        Connection conn = ConnectionUtil.getConn();
        Table table = getTable(conn, tableName, namespace);
        Get get = new Get(Bytes.toBytes(rowkey));
        Result result = table.get(get);
        // 处理row
        if (result != null) {
            Cell[] cells = result.rawCells();
            for (Cell cell : cells) {
                List<JSONObject> valueList = new ArrayList<>();
                String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                String[] values = value.split(",");
                for (String s : values) {
//                    Map<String, Object> mapValues = new HashMap<>();
//                    mapValues.put("name", s);
//                    valueList.add(new JSONObject(mapValues));
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", s);
                    valueList.add(jsonObject);
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("children", valueList);
                jsonObject.put("name", qualifier);
                qualifyList.add(jsonObject);
            }
        }
        if (qualifyList.isEmpty()) return null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("children", qualifyList);
        jsonObject.put("name", "cf");
        familyList.add(jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("children", familyList);
        jsonObject.put("name", String.format("rowkey: %s", rowkey));
        rowkeyList.add(jsonObject);
        return rowkeyList;
    }

    // 根据列名来查询多行
    public static ResultScanner qualifierFilter(String tableName, String qualifier) throws IOException {
        Connection conn = ConnectionUtil.getConn();
        Table table = conn.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        Filter filter = new QualifierFilter(CompareOperator.EQUAL, new SubstringComparator(qualifier));
        scan.setFilter(filter);
        return table.getScanner(scan);
    }

    public static List<JSONObject> getGender() throws IOException {
        List<JSONObject> output = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        int male = 0;
        int female = 0;

        for (Result result : qualifierFilter(user_profile, "gender")) {
            for (Cell cell : result.rawCells()) {
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                if ("男".equals(value)) {
                    male++;
                } else if ("女".equals(value)) {
                    female++;
                } else System.out.printf("性别错误，读取到性别为%s", value);
            }
        }
        map.put("男", male);
        map.put("女", female);

        for (String gender : Arrays.asList("男", "女")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", map.get(gender));
            jsonObject.put("name", gender);
            output.add(jsonObject);
        }
        return output;
    }

    public static List<JSONObject> getAgeGroup() throws IOException {
        List<JSONObject> output = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        int age50 = 0;
        int age60 = 0;
        int age70 = 0;
        int age80 = 0;
        int age90 = 0;
        int age00 = 0;
        int age10 = 0;
        int age20 = 0;

        for (Result result : qualifierFilter(user_profile, "AgeGroup")) {
            for (Cell cell : result.rawCells()) {
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                switch (value) {
                    case "50后":
                        age50++;
                        break;
                    case "60后":
                        age60++;
                        break;
                    case "70后":
                        age70++;
                        break;
                    case "80后":
                        age80++;
                        break;
                    case "90后":
                        age90++;
                        break;
                    case "00后":
                        age00++;
                        break;
                    case "10后":
                        age10++;
                        break;
                    case "20后":
                        age20++;
                        break;
                    default:
                        System.out.println("ageGroup错误，读取到ageGroup为: " + value);
                }
            }
        }
        map.put("50后", age50);
        map.put("60后", age60);
        map.put("70后", age70);
        map.put("80后", age80);
        map.put("90后", age90);
        map.put("00后", age00);
        map.put("10后", age10);
        map.put("20后", age20);

        for (String s : Arrays.asList("50后", "60后", "70后", "80后", "90后", "00后", "10后", "20后")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", map.get(s));
            jsonObject.put("name", s);
            output.add(jsonObject);
        }

        return output;
    }

    public static List<JSONObject> getPoliticalStatus() throws IOException {
        List<JSONObject> output = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        int crowd = 0;
        int party = 0;
        int nonParty = 0;

        for (Result result : qualifierFilter(user_profile, "political_status")) {
            for (Cell cell : result.rawCells()) {
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                switch (value) {
                    case "群众":
                        crowd++;
                        break;
                    case "党员":
                        party++;
                        break;
                    case "无党派人士":
                        nonParty++;
                        break;
                    default:
                        System.out.println("political_status错误，读取到political_status: " + value);
                }
            }
        }
        map.put("群众", crowd);
        map.put("党员", party);
        map.put("无党派人士", nonParty);

        for (String s : Arrays.asList("群众", "党员", "无党派人士")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", s);
            jsonObject.put("value", map.get(s));
            output.add(jsonObject);
        }

        return output;
    }

    public static List<JSONObject> getJob() throws IOException {
        List<JSONObject> output = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        int student = 0;
        int publicServant = 0;
        int soldier = 0;
        int policemen = 0;
        int teacher = 0;
        int whiteCollar = 0;

        for (Result result : qualifierFilter(user_profile, "job")) {
            for (Cell cell : result.rawCells()) {
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                switch (value) {
                    case "学生":
                        student++;
                        break;
                    case "公务员":
                        publicServant++;
                        break;
                    case "军人":
                        soldier++;
                        break;
                    case "警察":
                        policemen++;
                        break;
                    case "教师":
                        teacher++;
                        break;
                    case "白领":
                        whiteCollar++;
                        break;
                    default:
                        System.out.println("job错误，读取到job为: " + value);
                }
            }
        }
        map.put("学生", student);
        map.put("公务员", publicServant);
        map.put("军人", soldier);
        map.put("警察", policemen);
        map.put("教师", teacher);
        map.put("白领", whiteCollar);

        for (String s : Arrays.asList("学生", "公务员", "军人", "警察", "教师", "白领")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", map.get(s));
            jsonObject.put("name", s);
            output.add(jsonObject);
        }

        return output;
    }

    public static List<JSONObject> getMaritalStatus() throws IOException {
        List<JSONObject> output = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        int unmarried = 0;
        int married = 0;
        int divorced = 0;

        for (Result result : qualifierFilter(user_profile, "marital_status")) {
            for (Cell cell : result.rawCells()) {
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                switch (value) {
                    case "未婚":
                        unmarried++;
                        break;
                    case "已婚":
                        married++;
                        break;
                    case "离异":
                        divorced++;
                        break;
                    default:
                        System.out.println("marital_status错误，读取到marital_status: " + value);
                }
            }
        }
        map.put("未婚", unmarried);
        map.put("已婚", married);
        map.put("离异", divorced);

        for (String s : Arrays.asList("未婚", "已婚", "离异")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", s);
            jsonObject.put("value", map.get(s));
            output.add(jsonObject);
        }

        return output;
    }

    public static List<JSONObject> getConstellation() throws IOException {
        List<JSONObject> output = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        int 魔羯座 = 0;
        int 水瓶座 = 0;
        int 双鱼座 = 0;
        int 白羊座 = 0;
        int 金牛座 = 0;
        int 双子座 = 0;
        int 巨蟹座 = 0;
        int 狮子座 = 0;
        int 处女座 = 0;
        int 天秤座 = 0;
        int 天蝎座 = 0;
        int 射手座 = 0;

        for (Result result : qualifierFilter(user_profile, "constellation")) {
            for (Cell cell : result.rawCells()) {
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                switch (value) {
                    case "魔羯座":
                        魔羯座++;
                        break;
                    case "水瓶座":
                        水瓶座++;
                        break;
                    case "双鱼座":
                        双鱼座++;
                        break;
                    case "白羊座":
                        白羊座++;
                        break;
                    case "金牛座":
                        金牛座++;
                        break;
                    case "双子座":
                        双子座++;
                        break;
                    case "巨蟹座":
                        巨蟹座++;
                        break;
                    case "狮子座":
                        狮子座++;
                        break;
                    case "处女座":
                        处女座++;
                        break;
                    case "天秤座":
                        天秤座++;
                        break;
                    case "天蝎座":
                        天蝎座++;
                        break;
                    case "射手座":
                        射手座++;
                        break;
                    default:
                        System.out.println("constellation 错误，读取到 constellation 为: " + value);
                }
            }
        }
        map.put("魔羯座", 魔羯座);
        map.put("水瓶座", 水瓶座);
        map.put("双鱼座", 双鱼座);
        map.put("白羊座", 白羊座);
        map.put("金牛座", 金牛座);
        map.put("双子座", 双子座);
        map.put("巨蟹座", 巨蟹座);
        map.put("狮子座", 狮子座);
        map.put("处女座", 处女座);
        map.put("天秤座", 天秤座);
        map.put("天蝎座", 天蝎座);
        map.put("射手座", 射手座);

        for (String s : Arrays.asList("魔羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", map.get(s));
            jsonObject.put("name", s);
            output.add(jsonObject);
        }

        return output;
    }

    public static List<JSONObject> getNationality() throws IOException {
        List<JSONObject> output = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        int mainland = 0;
        int hongkong = 0;
        int macao = 0;
        int taiwan = 0;
        int other = 0;

        for (Result result : qualifierFilter(user_profile, "nationality")) {
            for (Cell cell : result.rawCells()) {
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                switch (value) {
                    case "中国大陆":
                        mainland++;
                        break;
                    case "中国香港":
                        hongkong++;
                        break;
                    case "中国澳门":
                        macao++;
                        break;
                    case "中国台湾":
                        taiwan++;
                        break;
                    case "其他":
                        other++;
                        break;
                    default:
                        System.out.println("nationality 错误，读取到 nationality 为: " + value);
                }
            }
        }
        map.put("中国大陆", mainland);
        map.put("中国香港", hongkong);
        map.put("中国澳门", macao);
        map.put("中国台湾", taiwan);
        map.put("其他", other);

        for (String s : Arrays.asList("中国大陆", "中国香港", "中国澳门", "中国台湾", "其他")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", map.get(s));
            jsonObject.put("name", s);
            output.add(jsonObject);
        }

        return output;
    }
}
