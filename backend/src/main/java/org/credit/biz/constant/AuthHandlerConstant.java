package org.credit.biz.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/** TODO: 将该类做成配置类, 搜索 springboot 配置类 */
@Configuration
public class AuthHandlerConstant {
    public String EMAIL;
    public String CODE="code";
    public String PASSWORD="password";
    public String EMAIL_CODE_KEY="EMAIL_CODE_KEY";
    public String REGISTER_EMAIL="REGISTER_EMAIL";
    public String CAPTCHA_STR = "captcha";
    public String LOGIN_USER="LOGIN_USER";
}
