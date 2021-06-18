package net.suncaper.tag_backend.controller;

import net.suncaper.tag_backend.pojo.Tag;
import net.suncaper.tag_backend.result.Result;
import net.suncaper.tag_backend.result.ResultFactory;
import net.suncaper.tag_backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TagController {
    @Autowired
    TagService tagService;

    @CrossOrigin
    @GetMapping("/api/basic/personal")
    public Result personalTags() {
        System.out.println("/api/basic/personal");
//        List<Object[]> tags = tagService.getTagsByPid(3);
        List<Tag> tags = tagService.findTagsByPid(3);
        return ResultFactory.buildSuccessResult(tags);
    }
}
