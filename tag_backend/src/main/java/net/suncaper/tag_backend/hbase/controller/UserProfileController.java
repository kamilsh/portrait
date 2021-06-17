package net.suncaper.tag_backend.hbase.controller;

import com.alibaba.fastjson.JSONObject;
import net.suncaper.tag_backend.hbase.utils.ConnectionUtil;
import net.suncaper.tag_backend.hbase.utils.TableUtil;
import net.suncaper.tag_backend.result.Result;
import net.suncaper.tag_backend.result.ResultFactory;
import org.apache.hadoop.hbase.client.Connection;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
public class UserProfileController {

    @CrossOrigin
    @GetMapping("/api/portrait")
    public Result getUserProfile() throws IOException {
        System.out.println("here: api/portrait 99");
        Connection conn = ConnectionUtil.getConn();
        List<String> json = TableUtil.get(conn, "user_profile", null, "99");
        conn.close();
        System.out.println(json);
        return ResultFactory.buildSuccessResult(json);
    }

    @CrossOrigin
    @GetMapping("/api/portrait/{rowkey}")
    public Result getUserProfileValueByRowkey(@PathVariable String rowkey) throws IOException {
        System.out.println(rowkey);
        List<JSONObject> output = TableUtil.getValueByRowkey("user_profile", null, rowkey);
        return ResultFactory.buildSuccessResult(output);
    }
}
