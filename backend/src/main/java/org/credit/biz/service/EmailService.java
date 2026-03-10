package org.credit.biz.service;
import org.credit.biz.common.Result;
import org.credit.biz.constant.EmailServiceConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.Random;


@Service
public class EmailService {
    private final String serviceEmail;
    private final String mailTitle;
    private final String emailTemplate;
    private final JavaMailSender mailSender;
    private final Random random = new Random();
    private final EmailServiceConstant constant;


    /*构造函数 */
    public EmailService(
            @Value("${spring.mail.username}") String serviceEmail,
            @Value("${spring.mail.title}") String mailTitle,
            @Value("${spring.mail.template}") String emailTemplate,
            JavaMailSender mailSender,
            EmailServiceConstant constant) {
        
        this.serviceEmail = serviceEmail;
        this.mailTitle = mailTitle;
        this.emailTemplate = emailTemplate;
        this.mailSender = mailSender;
        this.constant = constant;
    }
    
    public Result<Void> sendEmailCode(Map<String, String> params, HttpSession session) {
        String email = params.get(constant.emailCodeKey);
        String imageCaptcha = params.get(constant.captchaStr);

        /*  获取并校验图片验证码 */
        Object sess = session.getAttribute(constant.captchaStr);
        /* 这里是多态的很好体现，子类可以直接赋值给父类，父类可以强制为子类 */
        String sessionCaptcha = (sess instanceof String) ? (String) sess : null;

        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(imageCaptcha)) {
            return new Result<>(400, constant.msgImageError, null);
        }

        String emailCode = String.valueOf(random.nextInt(900000) + 100000);

        /*  构建并发送邮件 */
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(serviceEmail);
        message.setTo(email);
        message.setSubject(mailTitle);
        message.setText(String.format(emailTemplate, emailCode));
        mailSender.send(message);

        /*  将验证码和邮箱绑定存入 Session */
        session.setAttribute(constant.sessionEmailCode, emailCode);
        session.setAttribute(constant.sessionRegisterEmail, email);

        /*  销毁图片验证码（防止复用） */
        session.removeAttribute(constant.captchaStr);

        return new Result<>(200, constant.msgEmailSendSucess, null);
    }
}