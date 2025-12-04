package org.credit.demo.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class CaptchaConfig {
    @Bean
    public DefaultKaptcha captchaProducer() {
        Properties properties = new Properties();
        properties.put("kaptcha.image.width", "160");
        properties.put("kaptcha.image.height", "60");
        properties.put("kaptcha.textproducer.char.length", "5");
        properties.put("kaptcha.textproducer.char.string", "abcde2345678gfynmnpwx");
        properties.put("kaptcha.textproducer.font.size", "40");
        properties.put("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
