package org.credit.biz.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "captch")
public class CaptchImageGeneratorConstant {
    public  String captchaStr;
    public  String pragma;
    public  String noCache;
    public  String cacheControl;
    public  String expires;
    public  String imageJpeg;
    public  String jpeg;
}
