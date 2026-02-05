package org.credit.biz.service;
import org.credit.biz.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.Random;


@Service
public class EmailServiceImpl implements EmailService {
    
    private final String serviceEmail;
    private final String mailTitle;
    private final String emailTemplate;
    private final JavaMailSender mailSender;

    public EmailServiceImpl(
            @Value("${spring.mail.username}") String serviceEmail,
            @Value("${spring.mail.title}") String mailTitle,
            @Value("${spring.mail.template}") String emailTemplate,
            JavaMailSender mailSender) {
        
        this.serviceEmail = serviceEmail;
        this.mailTitle = mailTitle;
        this.emailTemplate = emailTemplate;
        this.mailSender = mailSender;
    }
    private static final String CAPTCHA_STR = "captcha";
    private static final String EMAIL_CODE_KEY = "email";
    private static final String SESSION_EMAIL_CODE = "EMAIL_CODE_KEY";
    private static final String SESSION_REGISTER_EMAIL = "REGISTER_EMAIL";
    private final Random random = new Random();

    @Override
    public Result<Void> sendEmailCode(Map<String, String> params, HttpSession session) {
        String email = params.get(EMAIL_CODE_KEY);
        String imageCaptcha = params.get(CAPTCHA_STR);

        /*  获取并校验图片验证码 */
        Object sess = session.getAttribute(CAPTCHA_STR);
        String sessionCaptcha = (sess instanceof String) ? (String) sess : null;

        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(imageCaptcha)) {
            Result<Void> result = new Result<>(400, "图片验证码错误或过期", null);
            return result;
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
        session.setAttribute(SESSION_EMAIL_CODE, emailCode);
        session.setAttribute(SESSION_REGISTER_EMAIL, email);

        /*  销毁图片验证码（防止复用） */
        session.removeAttribute(CAPTCHA_STR);

        String msg="邮箱验证码发送成功";
        Result<Void> result = new Result<>(200, msg, null);
        return result;
    }
}