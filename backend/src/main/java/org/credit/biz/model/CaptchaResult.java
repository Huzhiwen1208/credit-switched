package org.credit.biz.model;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpSession;

public class CaptchaResult {
    private String code;
    private BufferedImage image;

    public CaptchaResult(String code, BufferedImage image) {
        this.code = code;
        this.image = image;
    }

    public void setAttributeCaptchaStr(HttpSession session, String captchaStr) {
        session.setAttribute(captchaStr, code);
    }

    public void writeResponseImg(String jpeg, ServletOutputStream output) throws IOException {
        ImageIO.write(image, jpeg, output);
    }
}