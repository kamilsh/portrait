package net.suncaper.tag_backend.hbase.controller;

import net.suncaper.tag_backend.hbase.pojo.UserProfile;
import net.suncaper.tag_backend.hbase.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @CrossOrigin
    @GetMapping("/api/portrait")
    public UserProfile getUserProfile() {
        System.setProperty("hadoop.home.dir", "C:\\Users\\Administrator\\AppData\\Local\\hadoop-2.9.2");
        System.out.println("here: api/portrait 99");
        return userProfileService.query("99");
    }

    @CrossOrigin
    @GetMapping("/api/hello")
    public void hello() {
        System.out.print("Hello");
    }
}
