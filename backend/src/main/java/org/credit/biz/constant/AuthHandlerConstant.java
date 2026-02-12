package org.credit.biz.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "auth.handler")
public class AuthHandlerConstant {
    public String email;
    public String code;
    public String password;
    public String emailCodeKey;
    public String registerEmailKey;
    public String captchaKey;
    public String loginUserKey;
    public String msgEmailCaptchaError;
    public String msgRegisterEmailError;
    public String msgImageCaptchaError;
    public String msgLoginSuccess;
    public String msgEmailRegisted;
    public String msgRegisterSuccess;
    public String msgEmailNotRegistered;
    public String msgPasswordError;
}
