package net.suncaper.tag_backend.hbase.controller;

import net.suncaper.tag_backend.hbase.utils.TableUtil;
import net.suncaper.tag_backend.result.Result;
import net.suncaper.tag_backend.result.ResultFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class actionController {
    @CrossOrigin
    @GetMapping("/api/action/recentlyLoginTime")
    public Result getRecentlyLoginTime() throws IOException {
        System.out.println("/api/action/recentlyLoginTime");
        return ResultFactory.buildSuccessResult(TableUtil.getRecentlyLoginTime());
    }

    @CrossOrigin
    @GetMapping("/api/action/viewPage")
    public Result getViewPage() throws IOException {
        System.out.println("/api/action/viewPage");
        return ResultFactory.buildSuccessResult(TableUtil.getViewPage());
    }

    @CrossOrigin
    @GetMapping("/api/action/viewFrequency")
    public Result getViewFrequency() throws IOException {
        System.out.println("/api/action/viewFrequency");
        return ResultFactory.buildSuccessResult(TableUtil.getViewFrequency());
    }

    @CrossOrigin
    @GetMapping("/api/action/deviceType")
    public Result getDeviceType() throws IOException {
        System.out.println("/api/action/deviceType");
        return ResultFactory.buildSuccessResult(TableUtil.getDeviceType());
    }

    @CrossOrigin
    @GetMapping("/api/action/viewInterval")
    public Result getViewInterval() throws IOException {
        System.out.println("/api/action/viewInterval");
        return ResultFactory.buildSuccessResult(TableUtil.getViewInterval());
    }

    @CrossOrigin
    @GetMapping("/api/action/loginFrequency")
    public Result getLoginFrequency() throws IOException {
        System.out.println("/api/action/loginFrequency");
        return ResultFactory.buildSuccessResult(TableUtil.getLoginFrequency());
    }


}
