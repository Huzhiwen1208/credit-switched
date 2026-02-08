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
    public String msg_1;
    public String msg_2;
    public String msg_3;
    public String msg_4;
}
