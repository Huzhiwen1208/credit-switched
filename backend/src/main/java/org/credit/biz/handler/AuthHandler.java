package org.credit.biz.handler;
import org.credit.biz.common.Result;
import org.credit.biz.constant.AuthHandlerConstant;
import org.credit.biz.model.User;
import org.credit.biz.service.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@CrossOrigin
@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class AuthHandler {
    @Autowired
    private AuthHandlerConstant constant;
    private final UserService userService;

    @PostMapping("/register")
    public Result<Void> register(@RequestBody Map<String, String> params, HttpSession session) {
        String email = params.get(constant.email);
        String code = params.get(constant.code);
        String password = params.get(constant.password);

        /* 1. 校验邮箱验证码和注册邮箱与接收验证码的邮箱是否一致 */
        String sessionCode = (String) session.getAttribute(constant.emailCodeKey);
        String sessionEmail = (String) session.getAttribute(constant.registerEmailKey);

        if (sessionCode == null || !sessionCode.equals(code)) {
            return new Result<>(400, constant.msgEmailCaptchaError, null);
        }
        if (!email.equals(sessionEmail)) {
            return new Result<>(400, constant.msgRegisterEmailError, null);
        }
        
        /* 2. 调用 UserService 完成注册 */
        Result<Void> result =  userService.register(email, password);
        

        /* 3. 注册成功后清理 Session */
        session.removeAttribute(constant.emailCodeKey);
        session.removeAttribute(constant.registerEmailKey);

        return result;
    }

    @PostMapping("/login")
    public Result<User> login(@RequestBody Map<String, String> params, HttpSession session) {
        String email = params.get(constant.email);
        String password = params.get(constant.password);
        String userCaptcha = params.get(constant.captchaKey);

        /* 1. 校验图片验证码 */
        String sessionCaptcha = (String) session.getAttribute(constant.captchaKey);
        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(userCaptcha)) {
            return new Result<>(400, constant.msgImageCaptchaError, null);
        }
        session.removeAttribute(constant.captchaKey); // 销毁验证码，防止复用

        /* 2. 调用 Service 进行登录校验 (从 static Map 读取) */
        Result<User> loginResult = userService.login(email, password);

        /* 3. 登录成功，存入 Session */
        if (loginResult.getCode() == 200) {
            session.setAttribute(constant.loginUserKey, loginResult.getData());
        }
        return loginResult;
    }
}
