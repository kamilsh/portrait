package net.suncaper.tag_backend.hbase.controller;

import net.suncaper.tag_backend.hbase.utils.TableUtil;
import net.suncaper.tag_backend.result.Result;
import net.suncaper.tag_backend.result.ResultFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class businessController {
    @CrossOrigin
    @GetMapping("/api/business/consumeCycle")
    public Result getConsumeCycle() throws IOException {
        System.out.println("/api/business/consumeCycle");
        return ResultFactory.buildSuccessResult(TableUtil.getConsumeCycle());
    }

    @CrossOrigin
    @GetMapping("/api/business/avgOrderAmount")
    public Result getAvgOrderAmount() throws IOException {
        System.out.println("/api/business/avgOrderAmount");
        return ResultFactory.buildSuccessResult(TableUtil.getAvgOrderAmount());
    }

    @CrossOrigin
    @GetMapping("/api/business/paymentCode")
    public Result getPaymentCode() throws IOException {
        System.out.println("/api/business/paymentCode");
        return ResultFactory.buildSuccessResult(TableUtil.getPaymentCode());
    }

    @CrossOrigin
    @GetMapping("/api/business/maxOrderAmount")
    public Result getMaxOrderAmount() throws IOException {
        System.out.println("/api/business/maxOrderAmount");
        return ResultFactory.buildSuccessResult(TableUtil.getMaxOrderAmount());
    }

}
