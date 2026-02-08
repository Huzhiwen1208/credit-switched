package org.credit.biz.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "userservice")
public class UserServiceConstant {
    public String msg1;
    public String msg2;
    public String msg3;
    public String msg4;
    public String msg5;
}
