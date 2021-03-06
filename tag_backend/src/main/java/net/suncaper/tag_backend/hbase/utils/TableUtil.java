package net.suncaper.tag_backend.hbase.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
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
        table.close();
        conn.close();
        System.out.println(output);
        return output;
    }

    public static List<JSONObject> getTreeByRowkey(String tableName, String namespace, String rowkey) throws IOException {
        List<JSONObject> qualifyList = new ArrayList<>();
        List<JSONObject> familyList = new ArrayList<>();
        List<JSONObject> rowkeyList = new ArrayList<>();
        // ???hbase??????
        Connection conn = ConnectionUtil.getConn();
        Table table = getTable(conn, tableName, namespace);
        Get get = new Get(Bytes.toBytes(rowkey));
        Result result = table.get(get);
        // ??????row
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
        table.close();
        conn.close();
        return rowkeyList;
    }

    // ???????????????????????????
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
                if ("???".equals(value)) {
                    male++;
                } else if ("???".equals(value)) {
                    female++;
                } else System.out.printf("?????????????????????????????????%s", value);
            }
        }
        map.put("???", male);
        map.put("???", female);

        for (String gender : Arrays.asList("???", "???")) {
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
                    case "50???":
                        age50++;
                        break;
                    case "60???":
                        age60++;
                        break;
                    case "70???":
                        age70++;
                        break;
                    case "80???":
                        age80++;
                        break;
                    case "90???":
                        age90++;
                        break;
                    case "00???":
                        age00++;
                        break;
                    case "10???":
                        age10++;
                        break;
                    case "20???":
                        age20++;
                        break;
                    default:
                        System.out.println("ageGroup??????????????????ageGroup???: " + value);
                }
            }
        }
        map.put("50???", age50);
        map.put("60???", age60);
        map.put("70???", age70);
        map.put("80???", age80);
        map.put("90???", age90);
        map.put("00???", age00);
        map.put("10???", age10);
        map.put("20???", age20);

        for (String s : Arrays.asList("50???", "60???", "70???", "80???", "90???", "00???", "10???", "20???")) {
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
                    case "??????":
                        crowd++;
                        break;
                    case "??????":
                        party++;
                        break;
                    case "???????????????":
                        nonParty++;
                        break;
                    default:
                        System.out.println("political_status??????????????????political_status: " + value);
                }
            }
        }
        map.put("??????", crowd);
        map.put("??????", party);
        map.put("???????????????", nonParty);

        for (String s : Arrays.asList("??????", "??????", "???????????????")) {
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
                    case "??????":
                        student++;
                        break;
                    case "?????????":
                        publicServant++;
                        break;
                    case "??????":
                        soldier++;
                        break;
                    case "??????":
                        policemen++;
                        break;
                    case "??????":
                        teacher++;
                        break;
                    case "??????":
                        whiteCollar++;
                        break;
                    default:
                        System.out.println("job??????????????????job???: " + value);
                }
            }
        }
        map.put("??????", student);
        map.put("?????????", publicServant);
        map.put("??????", soldier);
        map.put("??????", policemen);
        map.put("??????", teacher);
        map.put("??????", whiteCollar);

        for (String s : Arrays.asList("??????", "?????????", "??????", "??????", "??????", "??????")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", map.get(s));
            jsonObject.put("name", s);
            output.add(jsonObject);
        }

        return output;
    }

    public static List<JSONObject> getViewPage() throws IOException {
        List<JSONObject> output = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        int order = 0;
        int catalog = 0;
        int goods = 0;
        int login = 0;
        int home = 0;
        int other = 0;

        for (Result result : qualifierFilter(user_profile, "view_page")) {
            for (Cell cell : result.rawCells()) {
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                for (String s : value.split(",")) {
                    String[] split = s.split(":");
                    switch (split[0]) {
                        case "?????????":
                            order += Integer.parseInt(split[1]);
                            break;
                        case "?????????":
                            catalog += Integer.parseInt(split[1]);
                            break;
                        case "?????????":
                            goods += Integer.parseInt(split[1]);
                            break;
                        case "?????????":
                            login += Integer.parseInt(split[1]);
                            break;
                        case "??????":
                            home += Integer.parseInt(split[1]);
                            break;
                        case "??????":
                            other += Integer.parseInt(split[1]);
                            break;
                        default:
                            System.out.println("view_page ?????????????????? view_page ???: " + value);
                    }
                }
            }
        }
        map.put("?????????", order);
        map.put("?????????", catalog);
        map.put("?????????", goods);
        map.put("?????????", login);
        map.put("??????", home);
        map.put("??????", other);

        for (String s : Arrays.asList("?????????", "?????????", "?????????", "?????????", "??????", "??????")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", map.get(s));
            jsonObject.put("name", s);
            output.add(jsonObject);
        }

        return output;
    }

    public static List<JSONObject> getDeviceType() throws IOException {
        List<JSONObject> output = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        int windows = 0;
        int iOS = 0;
        int mac = 0;
        int android = 0;
        int linux = 0;
        int other = 0;

        for (Result result : qualifierFilter(user_profile, "device_type")) {
            for (Cell cell : result.rawCells()) {
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                for (String s : value.split(",")) {
                    String[] split = s.split(":");
                    switch (split[0]) {
                        case "Windows":
                            windows += Integer.parseInt(split[1]);
                            break;
                        case "iOS":
                            iOS += Integer.parseInt(split[1]);
                            break;
                        case "Mac":
                            mac += Integer.parseInt(split[1]);
                            break;
                        case "Android":
                            android += Integer.parseInt(split[1]);
                            break;
                        case "Linux":
                            linux += Integer.parseInt(split[1]);
                            break;
                        case "other":
                            other += Integer.parseInt(split[1]);
                            break;
                        default:
                            System.out.println("device_type ?????????????????? device_type ???: " + value);
                    }
                }
            }
        }
        map.put("Windows", windows);
        map.put("iOS", iOS);
        map.put("Mac", mac);
        map.put("Android", android);
        map.put("Linux", linux);
        map.put("other", other);

        for (String s : Arrays.asList("Windows", "iOS", "Mac", "Android", "Linux", "other")) {
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
                    case "??????":
                        unmarried++;
                        break;
                    case "??????":
                        married++;
                        break;
                    case "??????":
                        divorced++;
                        break;
                    default:
                        System.out.println("marital_status??????????????????marital_status: " + value);
                }
            }
        }
        map.put("??????", unmarried);
        map.put("??????", married);
        map.put("??????", divorced);

        for (String s : Arrays.asList("??????", "??????", "??????")) {
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
        int ????????? = 0;
        int ????????? = 0;
        int ????????? = 0;
        int ????????? = 0;
        int ????????? = 0;
        int ????????? = 0;
        int ????????? = 0;
        int ????????? = 0;
        int ????????? = 0;
        int ????????? = 0;
        int ????????? = 0;
        int ????????? = 0;

        for (Result result : qualifierFilter(user_profile, "constellation")) {
            for (Cell cell : result.rawCells()) {
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                switch (value) {
                    case "?????????":
                        ?????????++;
                        break;
                    case "?????????":
                        ?????????++;
                        break;
                    case "?????????":
                        ?????????++;
                        break;
                    case "?????????":
                        ?????????++;
                        break;
                    case "?????????":
                        ?????????++;
                        break;
                    case "?????????":
                        ?????????++;
                        break;
                    case "?????????":
                        ?????????++;
                        break;
                    case "?????????":
                        ?????????++;
                        break;
                    case "?????????":
                        ?????????++;
                        break;
                    case "?????????":
                        ?????????++;
                        break;
                    case "?????????":
                        ?????????++;
                        break;
                    case "?????????":
                        ?????????++;
                        break;
                    default:
                        System.out.println("constellation ?????????????????? constellation ???: " + value);
                }
            }
        }
        map.put("?????????", ?????????);
        map.put("?????????", ?????????);
        map.put("?????????", ?????????);
        map.put("?????????", ?????????);
        map.put("?????????", ?????????);
        map.put("?????????", ?????????);
        map.put("?????????", ?????????);
        map.put("?????????", ?????????);
        map.put("?????????", ?????????);
        map.put("?????????", ?????????);
        map.put("?????????", ?????????);
        map.put("?????????", ?????????);

        for (String s : Arrays.asList("?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????", "?????????")) {
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
                    case "????????????":
                        mainland++;
                        break;
                    case "????????????":
                        hongkong++;
                        break;
                    case "????????????":
                        macao++;
                        break;
                    case "????????????":
                        taiwan++;
                        break;
                    case "??????":
                        other++;
                        break;
                    default:
                        System.out.println("nationality ?????????????????? nationality ???: " + value);
                }
            }
        }
        map.put("????????????", mainland);
        map.put("????????????", hongkong);
        map.put("????????????", macao);
        map.put("????????????", taiwan);
        map.put("??????", other);

        for (String s : Arrays.asList("????????????", "????????????", "????????????", "????????????", "??????")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", map.get(s));
            jsonObject.put("name", s);
            output.add(jsonObject);
        }

        return output;
    }

    public static List<JSONObject> getViewInterval() throws IOException {
        List<JSONObject> output = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        int interval1 = 0;
        int interval8 = 0;
        int interval13 = 0;
        int interval18 = 0;
        int interval22 = 0;

        for (Result result : qualifierFilter(user_profile, "view_interval")) {
            for (Cell cell : result.rawCells()) {
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                switch (value) {
                    case "1:00-7:00":
                        interval1++;
                        break;
                    case "8:00-12:00":
                        interval8++;
                        break;
                    case "13:00-17:00":
                        interval13++;
                        break;
                    case "18:00-21:00":
                        interval18++;
                        break;
                    case "22:00-24:00":
                        interval22++;
                        break;
                    default:
                        System.out.println("view_interval ?????????????????? view_interval ???: " + value);
                }
            }
        }
        map.put("1:00-7:00", interval1);
        map.put("8:00-12:00", interval8);
        map.put("13:00-17:00", interval13);
        map.put("18:00-21:00", interval18);
        map.put("22:00-24:00", interval22);

        for (String s : Arrays.asList("1:00-7:00", "8:00-12:00", "13:00-17:00", "18:00-21:00", "22:00-24:00")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", map.get(s));
            jsonObject.put("name", s);
            output.add(jsonObject);
        }

        return output;
    }

    public static List<JSONObject> getViewFrequency() throws IOException {
        List<JSONObject> output = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        int never = 0;
        int seldom = 0;
        int sometime = 0;
        int often = 0;
        int frequent = 0;

        for (Result result : qualifierFilter(user_profile, "view_frequency")) {
            for (Cell cell : result.rawCells()) {
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                switch (value) {
                    case "??????":
                        never++;
                        break;
                    case "??????":
                        seldom++;
                        break;
                    case "??????":
                        sometime++;
                        break;
                    case "??????":
                        often++;
                        break;
                    case "??????":
                        frequent++;
                        break;
                    default:
                        System.out.println("view_frequency ?????????????????? view_frequency ???: " + value);
                }
            }
        }
        map.put("??????", never);
        map.put("??????", seldom);
        map.put("??????", sometime);
        map.put("??????", often);
        map.put("??????", frequent);

        for (String s : Arrays.asList("??????", "??????", "??????", "??????", "??????")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", map.get(s));
            jsonObject.put("name", s);
            output.add(jsonObject);
        }

        return output;
    }

    public static List<JSONObject> getPaymentCode() throws IOException {
        List<JSONObject> output = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        int alipay = 0;
        int wechat = 0;
        int deposit = 0;
        int credit = 0;

        for (Result result : qualifierFilter(user_profile, "paymentCode")) {
            for (Cell cell : result.rawCells()) {
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                switch (value) {
                    case "alipay":
                        alipay++;
                        break;
                    case "wxpay":
                        wechat++;
                        break;
                    case "?????????":
                        deposit++;
                        break;
                    case "cod":
                        credit++;
                        break;
                    default:
                        System.out.println("paymentCode ?????????????????? paymentCode ???: " + value);
                }
            }
        }
        map.put("?????????", alipay);
        map.put("??????", wechat);
        map.put("?????????", deposit);
        map.put("?????????", credit);

        for (String s : Arrays.asList("?????????", "??????", "?????????", "?????????")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", map.get(s));
            jsonObject.put("name", s);
            output.add(jsonObject);
        }

        return output;
    }

    public static List<JSONObject> getLoginFrequency() throws IOException {
        List<JSONObject> output = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        int never = 0;
        int seldom = 0;
        int sometime = 0;
        int often = 0;

        for (Result result : qualifierFilter(user_profile, "login_frequency")) {
            for (Cell cell : result.rawCells()) {
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                switch (value) {
                    case "???":
                        never++;
                        break;
                    case "??????":
                        seldom++;
                        break;
                    case "??????":
                        sometime++;
                        break;
                    case "??????":
                        often++;
                        break;
                    default:
                        System.out.println("loginFrequency ?????????????????? loginFrequency ???: " + value);
                }
            }
        }
        map.put("???", never);
        map.put("??????", seldom);
        map.put("??????", sometime);
        map.put("??????", often);

        for (String s : Arrays.asList("???", "??????", "??????", "??????")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", map.get(s));
            jsonObject.put("name", s);
            output.add(jsonObject);
        }

        return output;
    }

    public static List<JSONObject> getConsumeCycle() throws IOException {
        List<JSONObject> output = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        int day7 = 0;
        int week2 = 0;
        int month1 = 0;
        int month2 = 0;
        int month3 = 0;
        int month4 = 0;
        int month5 = 0;
        int month6 = 0;
        int other = 0;

        for (Result result : qualifierFilter(user_profile, "consume_cycle")) {
            for (Cell cell : result.rawCells()) {
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                switch (value) {
                    case "7???":
                        day7++;
                        break;
                    case "2???":
                        week2++;
                        break;
                    case "1???":
                        month1++;
                        break;
                    case "2???":
                        month2++;
                        break;
                    case "3???":
                        month3++;
                        break;
                    case "4???":
                        month4++;
                        break;
                    case "5???":
                        month5++;
                        break;
                    case "6???":
                        month6++;
                        break;
                    case "??????":
                        other++;
                        break;
                    default:
                        System.out.println("consume_cycle ?????????????????? consume_cycle ???: " + value);
                }
            }
        }
        map.put("7???", day7);
        map.put("2???", week2);
        map.put("1???", month1);
        map.put("2???", month2);
        map.put("3???", month3);
        map.put("4???", month4);
        map.put("5???", month5);
        map.put("6???", month6);
        map.put("??????", other);

        for (String s : Arrays.asList("7???", "2???", "1???", "2???", "3???", "4???", "5???", "6???", "??????")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", map.get(s));
            jsonObject.put("name", s);
            output.add(jsonObject);
        }

        return output;
    }

    public static List<List<Double>> getAvgOrderAmount() throws IOException {
        List<List<Double>> output = new ArrayList<>();

        for (Result result : qualifierFilter(user_profile, "avg_order_amount")) {
            for (Cell cell : result.rawCells()) {
                List<Double> doubleList = new ArrayList<>();
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                String rowkey = Bytes.toString(CellUtil.cloneRow(cell));
                doubleList.add(Double.parseDouble(rowkey));
                doubleList.add(Double.parseDouble(value));
                output.add(doubleList);
            }
        }

        return output;
    }

    public static List<List<Double>> getMaxOrderAmount() throws IOException {
        List<List<Double>> output = new ArrayList<>();

        for (Result result : qualifierFilter(user_profile, "max_order_amount")) {
            for (Cell cell : result.rawCells()) {
                List<Double> doubleList = new ArrayList<>();
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                String rowkey = Bytes.toString(CellUtil.cloneRow(cell));
                doubleList.add(Double.parseDouble(rowkey));
                doubleList.add(Double.parseDouble(value));
                output.add(doubleList);
            }
        }

        return output;
    }

    // ?????????????????????
    public static List<JSONObject> filterList(List<String> remarks, List<String> names) throws IOException {
        List<JSONObject> output = new ArrayList<>();

        Connection conn = ConnectionUtil.getConn();
        Table table = getTable(conn, user_profile, null);
        Scan scan = new Scan();

        FilterList filterList = new FilterList();

        int length = remarks.size();
        for (int i = 0; i < length; i++) {
            SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter("cf".getBytes(),
                    remarks.get(i).getBytes(), CompareOperator.EQUAL, names.get(i).getBytes());
            singleColumnValueFilter.setFilterIfMissing(true);
            filterList.addFilter(singleColumnValueFilter);
        }

        scan.setFilter(filterList);
        ResultScanner scanner = table.getScanner(scan);

        for (Result result : scanner) {
            String rowkey = Bytes.toString(result.getRow());
            List<JSONObject> jsonObjectList = new ArrayList<>();
            for (Cell cell : result.rawCells()) {
                String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
                if ("purchase_goods".equals(qualifier) || "browse_products".equals(qualifier)) continue;
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                String[] strings = value.split(",");
                for (String s : strings) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(qualifier, s);
                    jsonObjectList.add(jsonObject);
                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rowkey: " + rowkey, jsonObjectList);
            output.add(jsonObject);
        }
        table.close();
        conn.close();

        return output;
    }

    public static List<JSONObject> getRecentlyLoginTime() throws IOException {
        List<JSONObject> output = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        int day1 = 0;
        int day7 = 0;
        int day14 = 0;
        int day30 = 0;
        int other = 0;

        for (Result result : qualifierFilter(user_profile, "recently_login_time")) {
            for (Cell cell : result.rawCells()) {
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                switch (value) {
                    case "1??????":
                        day1++;
                        break;
                    case "7??????":
                        day7++;
                        break;
                    case "14??????":
                        day14++;
                        break;
                    case "30??????":
                        day30++;
                        break;
                    case "??????":
                        other++;
                        break;
                    default:
                        System.out.println("recently_login_time ?????????????????? recently_login_time ???: " + value);
                }
            }
        }
        map.put("1??????", day1);
        map.put("7??????", day7);
        map.put("14??????", day14);
        map.put("30??????", day30);
        map.put("??????", other);

        for (String s : Arrays.asList("1??????", "7??????", "14??????", "30??????", "??????")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", map.get(s));
            jsonObject.put("name", s);
            output.add(jsonObject);
        }

        return output;
    }
}
