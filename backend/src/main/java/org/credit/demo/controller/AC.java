package org.credit.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.credit.demo.Result;

@CrossOrigin
@RestController
@RequestMapping("/demo")
public class AC {
    @PostMapping("/login-demo")
    public Result login(@RequestBody loginArgs args) {
        Result r = new Result();
        try {
            if (args.getUsername().equals("marco") && args.getPassword().equals("mk123456")) {
                r.setData("登录成功");
                r.setMsg("操作成功");
                r.setCode(200);
            } else {
                r.setData(null);
                r.setMsg("用户名或密码错误");
                r.setCode(400);
            }
        } catch (Exception e) {
            r.setData(null);
            r.setMsg("操作失败");
            r.setCode(500);
            e.printStackTrace();
        }
        return  r;
    }
}
