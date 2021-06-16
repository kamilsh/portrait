package net.suncaper.tag_backend.hbase.dao;

import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import net.suncaper.tag_backend.hbase.pojo.UserProfile;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class UserProfileRowMapper implements RowMapper<UserProfile> {
    private static byte[] COLUMN_FAMILY = "cf".getBytes();
    private static byte[] GENDER = "gender".getBytes();

    @Override
    public UserProfile mapRow(Result result, int rowNum) throws Exception {
        String gender = Bytes.toString(result.getValue(COLUMN_FAMILY, GENDER));

        UserProfile userProfile = new UserProfile();
        userProfile.setGender(gender);
        return userProfile;
    }
}
