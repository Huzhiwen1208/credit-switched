package org.credit.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.credit.demo.Result;

@CrossOrigin
@RestController
@RequestMapping("/apply")
public class AC {
    @PostMapping("/login")
    public Result login(@RequestBody loginArgs args){
        Result r = new Result();
        try {
            if (args.getAccount().equals("mark") && args.getPasswd().equals("123456")) {
                r.setMsg("操作成功");
                r.setCode(200);
                r.setData("Success");
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
