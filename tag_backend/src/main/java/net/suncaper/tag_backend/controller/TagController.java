package net.suncaper.tag_backend.controller;

import com.alibaba.fastjson.JSONObject;
import net.suncaper.tag_backend.hbase.utils.TableUtil;
import net.suncaper.tag_backend.pojo.Tag;
import net.suncaper.tag_backend.result.Result;
import net.suncaper.tag_backend.result.ResultFactory;
import net.suncaper.tag_backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class TagController {
    @Autowired
    TagService tagService;

    @CrossOrigin
    @GetMapping("/api/basic/personal")
    public Result personalTags() {
        System.out.println("/api/basic/personal");
        List<Tag> tags = tagService.findTagsByPid(3);
        return ResultFactory.buildSuccessResult(tags);
    }

    @CrossOrigin
    @GetMapping("/api/basic/business")
    public Result businessTags() {
        System.out.println("/api/basic/business");
        List<Tag> tags = tagService.findTagsByPid(4);
        return ResultFactory.buildSuccessResult(tags);
    }

    @CrossOrigin
    @GetMapping("/api/basic/action")
    public Result actionTags() {
        System.out.println("/api/basic/action");
        List<Tag> tags = tagService.findTagsByPid(5);
        return ResultFactory.buildSuccessResult(tags);
    }

    @CrossOrigin
    @GetMapping("/api/combination")
    public Result combinationTags() {
        System.out.println("/api/combination");
        List<JSONObject> combinationTags = tagService.getCombinationTags();
        return ResultFactory.buildSuccessResult(combinationTags);
    }

    @CrossOrigin
    @PostMapping("/api/search/combination")
    public Result searchCombinationTags(@RequestBody JSONObject jsonObject) throws IOException {
        System.out.println("/api/search/combination");
        ArrayList valueList = (ArrayList) jsonObject.get("valueList");
        List<Integer> tagList = new ArrayList<>();
        List<Integer> attributeList = new ArrayList<>();
        for (Object row : valueList) {
            String s = row.toString();
            s = s.substring(1, s.length() - 1);
            String[] split = s.split(", ");
            int tag = Integer.parseInt(split[1]);
            tagList.add(tag);
            int attribute = Integer.parseInt(split[2]);
            attributeList.add(attribute);
        }
        List<String> remarks = tagService.getRemarkByID(tagList);
        List<String> names = tagService.getNameByID(attributeList);
        List<JSONObject> tags = TableUtil.filterList(remarks, names);
        if (tags.isEmpty())
            return ResultFactory.buildFailResult("未找到该描述用户");
        return ResultFactory.buildSuccessResult(tags);
    }

    @CrossOrigin
    @GetMapping("/api/test")
    public Result test() throws IOException {
        return ResultFactory.buildSuccessResult(TableUtil.filterList(Arrays.asList("gender", "job", "constellation"),
                Arrays.asList("男", "学生", "白羊座")));
    }
}
