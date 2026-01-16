package org.credit.biz.handler;
import org.credit.biz.common.Result;
import org.credit.biz.model.User;
import org.credit.biz.service.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@CrossOrigin
@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class AuthHandler {

    private static final String EMAIL = "email";
    private static final String Code="code";
    private static final String Password="password";
    private static final String EMAIL_CODE_KEY="EMAIL_CODE_KEY";
    private static final String REGISTER_EMAIL="REGISTER_EMAIL";
    private static final String CAPTCHA_STR = "captcha";
    private static final String Passord="password";
    private static final String LOGIN_USER="LOGIN_USER";
    private final UserService userService;

    @PostMapping("/register")
    public Result<Void> register(@RequestBody Map<String, String> params, HttpSession session) {
        String email = params.get(EMAIL);
        String code = params.get(Code);
        String password = params.get(Password);
        /* 1. 校验邮箱验证码 */
        String sessionCode = (String) session.getAttribute(EMAIL_CODE_KEY);
        String sessionEmail = (String) session.getAttribute(REGISTER_EMAIL);

        /*这两行打印代码，专门用来捉虫！*/
        System.out.println(">>> 真正有效的验证码是：" + sessionCode);
        System.out.println(">>> 你刚才输入的验证码是：" + code);

        if (sessionCode == null || !sessionCode.equals(code)) {
            Result<Void> result = new Result<>(400, "邮箱验证码错误或过期", null);
            return result;
        }
        if (!email.equals(sessionEmail)) {
            Result<Void> result = new Result<>(400, "注册邮箱与发送验证码邮箱不一致", null);
            return result;
        }

        /* 2. 调用 UserService 完成注册 */
        Result<Void> result =  userService.register(email, password);
        

        /* 3. 注册成功后清理 Session */
        session.removeAttribute(EMAIL_CODE_KEY);
        session.removeAttribute(REGISTER_EMAIL);

        return result;
    }

    @PostMapping("/login")
    public Result<Void> login(@RequestBody Map<String, String> params, HttpSession session) {
        String email = params.get(EMAIL_CODE_KEY);
        String password = params.get(Passord);
        String userCaptcha = params.get(CAPTCHA_STR);

        /* 1. 校验图片验证码 */
        String sessionCaptcha = (String) session.getAttribute(CAPTCHA_STR);
        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(userCaptcha)) {
            Result<Void> result = new Result<>(400, "图片验证码错误或过期", null);
            return result;
        }
        session.removeAttribute(CAPTCHA_STR);

        /* 2. 调用 Service 进行登录校验 (从 static Map 读取) */
        Result<User> loginResult = userService.login(email, password);

        /* 3. 登录成功，存入 Session */
        session.setAttribute(LOGIN_USER, loginResult.getData());
        
        if (loginResult.getCode()!= 200) {
            Result<Void> result = new Result<>(loginResult.getCode(), loginResult.getMsg(), null);
            return result;
        }

        String msg="登录成功";
        Result<Void> result = new Result<>(200, msg, null);
        return result;
    }
}
