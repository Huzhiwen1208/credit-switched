package org.credit.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.credit.demo.Result;

@CrossOrigin
@RestController
@RequestMapping("/apply")
public class AC {
    @GetMapping("/test")
    public Result getAllStudent() {
        Result r = new Result();
        try {
            r.setMsg("操作成功");
            r.setCode(200);
            r.setData(Object.class);
        } catch (Exception e) {
            r.setData(null);
            r.setMsg("操作失败");
            r.setCode(500);
            e.printStackTrace();
        }
        return  r;
    }
}
