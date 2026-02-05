package org.credit.biz.handler;
import org.credit.biz.model.CaptchaResult;
import org.credit.biz.utils.CaptchaUtils;
import org.springframework.web.bind.annotation.*;
import javax.imageio.ImageIO;
import jakarta.servlet.http.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


@CrossOrigin
@RestController
@RequestMapping("/apply")
public class CaptchaImageGenerator {
    private static final String CAPTCHA_STR="captcha";
    
    @GetMapping("/image")
    public void getImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CaptchaResult caResult = CaptchaUtils.createCaptcha(); 
        String code = caResult.getCode();
        BufferedImage image = caResult.getImage();

        /* 存储验证码到 Session */
        HttpSession session = request.getSession();
        session.setAttribute(CAPTCHA_STR, code);

        /* 设置响应头，告诉浏览器不要缓存图片 */
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        /* 输出图片到响应流 */
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }

}

