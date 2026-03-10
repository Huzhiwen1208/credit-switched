package org.credit.biz.handler;
import org.credit.biz.model.CaptchaResult;
import org.credit.biz.utils.CaptchaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.credit.biz.constant.CaptchImageGeneratorConstant;
import javax.imageio.ImageIO;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;


import java.awt.image.BufferedImage;
import java.io.IOException;


@CrossOrigin
@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class CaptchaImageGenerator {
    @Autowired
    private CaptchImageGeneratorConstant constant;
    
    @GetMapping("/image")
    public void getImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CaptchaResult caResult = CaptchaUtils.createCaptcha(); 
        String code = caResult.getCode();
        BufferedImage image = caResult.getImage();

        /* 存储验证码到 Session */
        HttpSession session = request.getSession();
        session.setAttribute(constant.captchaStr, code);

        /* 设置响应头，告诉浏览器不要缓存图片 */
        response.setHeader(constant.pragma, constant.noCache);
        response.setHeader(constant.cacheControl, constant.noCache);
        response.setDateHeader(constant.expires, 0);
        response.setContentType(constant.imageJpeg);

        /* 输出图片到响应流 */
        ImageIO.write(image, constant.jpeg, response.getOutputStream());
    }

}

