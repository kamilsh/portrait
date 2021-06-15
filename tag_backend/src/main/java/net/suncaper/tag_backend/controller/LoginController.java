package net.suncaper.tag_backend.controller;

import net.suncaper.tag_backend.pojo.User;
import net.suncaper.tag_backend.result.Result;
import net.suncaper.tag_backend.result.ResultFactory;
import net.suncaper.tag_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @CrossOrigin
    @PostMapping("/api/login")
    @ResponseBody
    public Result userLogin(@RequestBody User requestUser) {
        // 对 html 标签进行转义，防止 XSS 攻击
        String uname = requestUser.getUsername();
        uname = HtmlUtils.htmlEscape(uname);
        String password = requestUser.getPassword();

        User user = userService.findByUsername(uname);
        if(user == null) {
            return ResultFactory.buildFailResult("用户不存在");
        }
        user = userService.get(uname,password);
        if(user == null) {
            return ResultFactory.buildFailResult("密码不匹配");
        }
        return ResultFactory.buildSuccessResult(uname);
    }
}
