package org.credit.biz.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "emailservice")
public class EmailServiceConstant {
    public  String CAPTCHA_STR;
    public String EMAIL_CODE_KEY;
    public String SESSION_EMAIL_CODE;
    public String SESSION_REGISTER_EMAIL;
    public String msg_image;
    public String msg_mail;
}
