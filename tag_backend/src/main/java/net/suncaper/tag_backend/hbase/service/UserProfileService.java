package net.suncaper.tag_backend.hbase.service;

import net.suncaper.tag_backend.hbase.pojo.UserProfile;

import java.util.List;

public interface UserProfileService {
    List<UserProfile> query(String startRow, String stopRow);

    public UserProfile query(String row);
}
