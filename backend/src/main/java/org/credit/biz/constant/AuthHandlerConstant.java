package org.credit.biz.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/** TODO: 将该类做成配置类, 搜索 springboot 配置类 */
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
    public String msg1;
    public String msg2;
    public String msg3;
    public String msg4;
    public String msg5;
    public String msg6;
    public String msg7;
    public String msg8;
    public String msg9;
}
