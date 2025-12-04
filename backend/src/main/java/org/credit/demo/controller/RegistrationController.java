package org.credit.demo.controller;

import org.credit.demo.Result;
import org.credit.demo.service.CaptchaService;
import org.credit.demo.service.MailService;
import org.credit.demo.service.OtpService;
import org.credit.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/apply")
public class RegistrationController {

    private final CaptchaService captchaService;
    private final OtpService otpService;
    private final MailService mailService;
    private final UserService userService;

    public RegistrationController(CaptchaService captchaService, OtpService otpService, MailService mailService, UserService userService) {
        this.captchaService = captchaService;
        this.otpService = otpService;
        this.mailService = mailService;
        this.userService = userService;
    }

    // DEBUG: expose captcha code for uuid (for local debugging only)
    @GetMapping("/captcha/code/{uuid}")
    public Result getCaptchaCode(@PathVariable String uuid) {
        Result r = new Result();
        String code = captchaService.getCodeForDebug(uuid);
        if (code == null) {
            r.setCode(404);
            r.setMsg("not found");
        } else {
            r.setCode(200);
            r.setMsg("ok");
            r.setData(code);
        }
        return r;
    }

    @GetMapping("/captcha")
    public Result captcha() {
        Result r = new Result();
        try {
            CaptchaService.CaptchaResponse c = captchaService.generateCaptcha();
            Map<String, Object> data = new HashMap<>();
            data.put("uuid", c.getUuid());
            data.put("imageBase64", c.getImageBase64());
            r.setCode(200);
            r.setMsg("获取成功");
            r.setData(data);
        } catch (Exception e) {
            r.setCode(500);
            r.setMsg("生成验证码失败");
            r.setData(null);
            e.printStackTrace();
        }
        return r;
    }

    @PostMapping("/register/request")
    public Result requestRegister(@RequestBody RegisterRequest req) {
        Result r = new Result();
        try {
            System.out.println("[DEBUG] requestRegister received: email=" + req.getEmail() + ", captchaUuid=" + req.getCaptchaUuid() + ", captcha='" + req.getCaptcha() + "'");
            if (req.getEmail() == null || req.getCaptcha() == null || req.getCaptchaUuid() == null) {
                r.setCode(400);
                r.setMsg("参数错误");
                return r;
            }
            if (!captchaService.validateCaptcha(req.getCaptchaUuid(), req.getCaptcha())) {
                r.setCode(400);
                r.setMsg("人机验证码错误或已过期");
                return r;
            }
            if (userService.existsByEmail(req.getEmail())) {
                r.setCode(400);
                r.setMsg("邮箱已注册");
                return r;
            }
            String otp = otpService.generateOtp(req.getEmail(), 600); // 10 minutes
            // send email
            String subject = "您的注册验证码";
            String text = "您的验证码是：" + otp + "，10分钟内有效。请保管好，不要透露给他人。";
            try {
                mailService.sendSimpleMail(req.getEmail(), subject, text);
                r.setCode(200);
                r.setMsg("验证码已发送");
                r.setData(null);
            } catch (Exception e) {
                // Mail sending failed; for development, return otp in response and log
                System.err.println("[WARN] Failed to send email to " + req.getEmail() + ": " + e.getMessage());
                r.setCode(200);
                r.setMsg("验证码已生成（邮件发送失败，开发模式返回OTP）");
                r.setData(otp);
            }
        } catch (Exception e) {
            r.setCode(500);
            r.setMsg("请求失败");
            e.printStackTrace();
        }
        return r;
    }

    @PostMapping("/register/confirm")
    public Result confirmRegister(@RequestBody RegisterConfirm req) {
        Result r = new Result();
        try {
            if (userService.existsByEmail(req.getEmail())) {
                r.setCode(400);
                r.setMsg("邮箱已注册");
                return r;
            }
            if (!otpService.validateOtp(req.getEmail(), req.getOtp())) {
                r.setCode(400);
                r.setMsg("验证码错误或已过期");
                return r;
            }
            userService.createUser(req.getEmail(), req.getPassword());
            r.setCode(200);
            r.setMsg("注册成功");
            r.setData(null);
        } catch (Exception e) {
            r.setCode(500);
            r.setMsg("注册失败");
            e.printStackTrace();
        }
        return r;
    }

    // Login endpoint - requires email, password and captcha (uuid + captcha)
    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest req) {
        Result r = new Result();
        try {
            if (req.getEmail() == null || req.getPassword() == null || req.getCaptcha() == null || req.getCaptchaUuid() == null) {
                r.setCode(400);
                r.setMsg("参数错误");
                return r;
            }
            if (!captchaService.validateCaptcha(req.getCaptchaUuid(), req.getCaptcha())) {
                r.setCode(400);
                r.setMsg("人机验证码错误或已过期");
                return r;
            }
            if (!userService.existsByEmail(req.getEmail())) {
                r.setCode(400);
                r.setMsg("用户不存在");
                return r;
            }
            if (!userService.validatePassword(req.getEmail(), req.getPassword())) {
                r.setCode(400);
                r.setMsg("邮箱或密码不正确");
                return r;
            }
            // Login success
            Map<String, Object> data = new HashMap<>();
            data.put("email", req.getEmail());
            data.put("msg", "登录成功");
            r.setCode(200);
            r.setMsg("登录成功");
            r.setData(data);
            return r;
        } catch (Exception e) {
            r.setCode(500);
            r.setMsg("登录失败");
            e.printStackTrace();
        }
        return r;
    }

    static class RegisterRequest {
        private String email;
        private String captcha;
        private String captchaUuid;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getCaptcha() { return captcha; }
        public void setCaptcha(String captcha) { this.captcha = captcha; }
        public String getCaptchaUuid() { return captchaUuid; }
        public void setCaptchaUuid(String captchaUuid) { this.captchaUuid = captchaUuid; }
    }

    static class RegisterConfirm {
        private String email;
        private String otp;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getOtp() { return otp; }
        public void setOtp(String otp) { this.otp = otp; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    static class LoginRequest {
        private String email;
        private String password;
        private String captcha;
        private String captchaUuid;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getCaptcha() { return captcha; }
        public void setCaptcha(String captcha) { this.captcha = captcha; }
        public String getCaptchaUuid() { return captchaUuid; }
        public void setCaptchaUuid(String captchaUuid) { this.captchaUuid = captchaUuid; }
    }
}
