package org.credit.biz.handler;
import org.credit.biz.common.Result;
import org.credit.biz.constant.AuthHandlerConstant;
import org.credit.biz.model.User;
import org.credit.biz.service.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

/** TODO: 去除警告 */
@CrossOrigin
@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class AuthHandler {

    private  AuthHandlerConstant constant;
    private final UserService userService;

    @PostMapping("/register")
    public Result<Void> register(@RequestBody Map<String, String> params, HttpSession session) {
        String email = params.get(constant.EMAIL);
        String code = params.get(constant.CODE);
        String password = params.get(constant.PASSWORD);
        /* 1. 校验邮箱验证码和注册邮箱与接收验证码的邮箱是否一致 */
        String sessionCode = (String) session.getAttribute(constant.EMAIL_CODE_KEY);
        String sessionEmail = (String) session.getAttribute(constant.REGISTER_EMAIL);

        if (sessionCode == null || !sessionCode.equals(code)) {
            Result<Void> result = new Result<>(400, constant.msg_1, null);
            return result;
        }
        if (!email.equals(sessionEmail)) {
            Result<Void> result = new Result<>(400, constant.msg_2, null);
            return result;
        }

        /* 2. 调用 UserService 完成注册 */
        Result<Void> result =  userService.register(email, password);
        

        /* 3. 注册成功后清理 Session */
        session.removeAttribute(constant.EMAIL_CODE_KEY);
        session.removeAttribute(constant.REGISTER_EMAIL);

        return result;
    }

    @PostMapping("/login")
    public Result<User> login(@RequestBody Map<String, String> params, HttpSession session) {
        String email = params.get(constant.EMAIL);
        String password = params.get(constant.PASSWORD);
        String userCaptcha = params.get(constant.CAPTCHA_STR);

        /* 1. 校验图片验证码 */
        String sessionCaptcha = (String) session.getAttribute(constant.CAPTCHA_STR);
        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(userCaptcha)) {
            Result<User> result = new Result<>(400, constant.msg_3, null);
            return result;
        }
        session.removeAttribute(constant.CAPTCHA_STR);

        /* 2. 调用 Service 进行登录校验 (从 static Map 读取) */
        Result<User> loginResult = userService.login(email, password);

        /* 3. 登录成功，存入 Session */
        if (loginResult.getCode() == 200) {
            session.setAttribute(constant.LOGIN_USER, loginResult.getData());
        }
        return loginResult;
    }
}
