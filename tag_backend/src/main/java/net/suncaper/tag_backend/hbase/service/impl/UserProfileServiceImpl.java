package net.suncaper.tag_backend.hbase.service.impl;

import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import net.suncaper.tag_backend.hbase.dao.UserProfileRowMapper;
import net.suncaper.tag_backend.hbase.pojo.UserProfile;
import net.suncaper.tag_backend.hbase.service.UserProfileService;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    @Autowired
    private HbaseTemplate hbaseTemplate;


    @Override
    public List<UserProfile> query(String startRow, String stopRow) {
        Scan scan = new Scan(Bytes.toBytes(startRow), Bytes.toBytes(stopRow));
        scan.setCaching(5000);
        List<UserProfile> userProfileList = this.hbaseTemplate.find("user_profile", scan, new UserProfileRowMapper());
        return userProfileList;
    }

    @Override
    public UserProfile query(String row) {
        System.out.println("service: query");
        UserProfile userProfile = this.hbaseTemplate.get("user_profile", row, new UserProfileRowMapper());
        System.out.println("connected");
        System.out.println(userProfile.toString());
        return userProfile;
    }
}
