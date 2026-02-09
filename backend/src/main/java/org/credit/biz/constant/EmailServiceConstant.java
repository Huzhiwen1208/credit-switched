package org.credit.biz.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "emailservice")
public class EmailServiceConstant {
    public  String captchaStr;
    public String emailCodeKey;
    public String sessionEmailCode;
    public String sessionRegisterEmail;
    public String msgImage;
    public String msgMail;
}
