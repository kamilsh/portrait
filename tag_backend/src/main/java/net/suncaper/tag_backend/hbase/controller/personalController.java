package net.suncaper.tag_backend.hbase.controller;

import net.suncaper.tag_backend.hbase.utils.TableUtil;
import net.suncaper.tag_backend.result.Result;
import net.suncaper.tag_backend.result.ResultFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class personalController {
    @CrossOrigin
    @GetMapping("/api/personal/gender")
    public Result getGender() throws IOException {
        System.out.println("/api/personal/gender");
        return ResultFactory.buildSuccessResult(TableUtil.getGender());
    }

    @CrossOrigin
    @GetMapping("/api/personal/ageGroup")
    public Result getAgeGroup() throws IOException {
        System.out.println("/api/personal/ageGroup");
        return ResultFactory.buildSuccessResult(TableUtil.getAgeGroup());
    }

    @CrossOrigin
    @GetMapping("/api/personal/politicalStatus")
    public Result getPoliticalStatus() throws IOException {
        System.out.println("/api/personal/politicalStatus");
        return ResultFactory.buildSuccessResult(TableUtil.getPoliticalStatus());
    }

    @CrossOrigin
    @GetMapping("/api/personal/job")
    public Result getJob() throws IOException {
        System.out.println("/api/personal/job");
        return ResultFactory.buildSuccessResult(TableUtil.getJob());
    }

    @CrossOrigin
    @GetMapping("/api/personal/maritalStatus")
    public Result getMaritalStatus() throws IOException {
        System.out.println("/api/personal/maritalStatus");
        return ResultFactory.buildSuccessResult(TableUtil.getMaritalStatus());
    }

    @CrossOrigin
    @GetMapping("/api/personal/constellation")
    public Result getConstellation() throws IOException {
        System.out.println("/api/personal/constellation");
        return ResultFactory.buildSuccessResult(TableUtil.getConstellation());
    }

    @CrossOrigin
    @GetMapping("/api/personal/nationality")
    public Result getNationality() throws IOException {
        System.out.println("/api/personal/nationality");
        return ResultFactory.buildSuccessResult(TableUtil.getNationality());
    }

}
