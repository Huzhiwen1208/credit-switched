package org.credit.biz.handler;
import org.credit.biz.common.Result;
import org.credit.biz.constant.AuthHandlerConstant;
import org.credit.biz.constant.CaptchImageGeneratorConstant;
import org.credit.biz.model.CaptchaResult;
import org.credit.biz.model.User;
import org.credit.biz.service.EmailService;
import org.credit.biz.service.UserService;
import org.credit.biz.utils.CaptchaUtils;

import java.io.IOException;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@CrossOrigin
@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class AuthHandler {
    @Autowired
    private AuthHandlerConstant authHandlerConstant;
    @Autowired
    private CaptchImageGeneratorConstant captchImageGeneratorConstant;

    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/send-email-code")
    public Result<Void> sendEmailCode(@RequestBody Map<String, String> params, HttpSession session) {
        return emailService.sendEmailCode(params, session);
    }

    @GetMapping("/image")
    public void getImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CaptchaResult captcha = CaptchaUtils.createCaptcha(); 

        /* 存储验证码到 Session */
        HttpSession session = request.getSession();
        captcha.setAttributeCaptchaStr(session, captchImageGeneratorConstant.captchaStr);

        /* 设置响应头，告诉浏览器不要缓存图片 */
        response.setHeader(captchImageGeneratorConstant.pragma, captchImageGeneratorConstant.noCache);
        response.setHeader(captchImageGeneratorConstant.cacheControl, captchImageGeneratorConstant.noCache);
        response.setDateHeader(captchImageGeneratorConstant.expires, 0);
        response.setContentType(captchImageGeneratorConstant.imageJpeg);

        /* 输出图片到响应流 */
        captcha.writeResponseImg(captchImageGeneratorConstant.jpeg, response.getOutputStream());
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody Map<String, String> params, HttpSession session) {
        String email = params.get(authHandlerConstant.email);
        String code = params.get(authHandlerConstant.code);
        String password = params.get(authHandlerConstant.password);

        /* 1. 校验邮箱验证码和注册邮箱与接收验证码的邮箱是否一致 */
        String sessionCode = (String) session.getAttribute(authHandlerConstant.emailCodeKey);
        String sessionEmail = (String) session.getAttribute(authHandlerConstant.registerEmailKey);

        if (sessionCode == null || !sessionCode.equals(code)) {
            return new Result<>(400, authHandlerConstant.msgEmailCaptchaError, null);
        }
        if (!email.equals(sessionEmail)) {
            return new Result<>(400, authHandlerConstant.msgRegisterEmailError, null);
        }
        
        /* 2. 调用 UserService 完成注册 */
        Result<Void> result =  userService.register(email, password);
        

        /* 3. 注册成功后清理 Session */
        session.removeAttribute(authHandlerConstant.emailCodeKey);
        session.removeAttribute(authHandlerConstant.registerEmailKey);

        return result;
    }

    @PostMapping("/login")
    public Result<User> login(@RequestBody Map<String, String> params, HttpSession session) {
        String email = params.get(authHandlerConstant.email);
        String password = params.get(authHandlerConstant.password);
        String userCaptcha = params.get(authHandlerConstant.captchaKey);

        /* 1. 校验图片验证码 */
        String sessionCaptcha = (String) session.getAttribute(authHandlerConstant.captchaKey);
        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(userCaptcha)) {
            return new Result<>(400, authHandlerConstant.msgImageCaptchaError, null);
        }
        session.removeAttribute(authHandlerConstant.captchaKey); // 销毁验证码，防止复用

        /* 2. 调用 Service 进行登录校验 (从 static Map 读取) */
        Result<User> loginResult = userService.login(email, password);

        /* 3. 登录成功，存入 Session */
        if (loginResult.getCode() == 200) {
            session.setAttribute(authHandlerConstant.loginUserKey, loginResult.getData());
        }
        return loginResult;
    }
}
