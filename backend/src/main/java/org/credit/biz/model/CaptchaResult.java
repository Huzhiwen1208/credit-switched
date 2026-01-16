package org.credit.biz.model;

import java.awt.image.BufferedImage;


public class CaptchaResult {

    private String code;
    private BufferedImage image;

    public CaptchaResult(String code, BufferedImage image) {
        this.code = code;
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}