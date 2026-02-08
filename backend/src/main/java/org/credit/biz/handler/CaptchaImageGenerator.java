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
    private static final String PRAGMA="Pragma";
    private static final String NO_CACHE="no-cache";
    private static final String CACHE_CONTROL="Cache-Control";
    private static final String EXPIRES="Expires";
    private static final String IMAGE_JPEG="image/jpeg";
    private static final String JPEG="JPEG";
    
    @GetMapping("/image")
    public void getImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CaptchaResult caResult = CaptchaUtils.createCaptcha(); 
        String code = caResult.getCode();
        BufferedImage image = caResult.getImage();

        /* 存储验证码到 Session */
        HttpSession session = request.getSession();
        session.setAttribute(CAPTCHA_STR, code);

        /* 设置响应头，告诉浏览器不要缓存图片 */
        response.setHeader(PRAGMA, NO_CACHE);
        response.setHeader(CACHE_CONTROL, NO_CACHE);
        response.setDateHeader(EXPIRES, 0);
        response.setContentType(IMAGE_JPEG);

        /* 输出图片到响应流 */
        ImageIO.write(image, JPEG, response.getOutputStream());
    }

}

