package org.credit.demo.controller;

import org.credit.demo.Result;
import org.credit.demo.service.MailService;
import org.credit.demo.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class DebugController {

    private final MailService mailService;
    private final UserService userService;

    public DebugController(MailService mailService, UserService userService) {
        this.mailService = mailService;
        this.userService = userService;
    }

    // Simple test endpoint: /apply/mail/test?to=your@163.com
    @GetMapping("/apply/mail/test")
    public Result testMail(@RequestParam(value = "to", required = false) String to) {
        Result r = new Result();
        try {
            if (to == null || to.trim().isEmpty()) {
                r.setCode(400);
                r.setMsg("missing 'to' query param e.g. /apply/mail/test?to=you@163.com");
                return r;
            }
            mailService.sendSimpleMail(to, "Test Email from App", "This is a test email from your Spring Boot app.");
            r.setCode(200);
            r.setMsg("发送成功");
            r.setData(null);
        } catch (Exception e) {
            r.setCode(500);
            r.setMsg("发送失败: " + e.getMessage());
            r.setData(null);
            e.printStackTrace();
        }
        return r;
    }

    // Dev-only: Get all registered users (non-sensitive fields)
    @GetMapping("/users")
    public Result listUsers() {
        Result r = new Result();
        try {
            java.util.List<java.util.Map<String, String>> out = new java.util.ArrayList<>();
            for (org.credit.demo.model.User u : userService.listAllUsers()) {
                java.util.Map<String, String> m = new java.util.HashMap<>();
                m.put("id", u.getId());
                m.put("email", u.getEmail());
                m.put("verified", Boolean.toString(u.isVerified()));
                out.add(m);
            }
            r.setCode(200);
            r.setMsg("ok");
            r.setData(out);
        } catch (Exception e) {
            r.setCode(500);
            r.setMsg("error");
            e.printStackTrace();
        }
        return r;
    }
}
