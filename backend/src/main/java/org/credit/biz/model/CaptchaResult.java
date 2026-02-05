package org.credit.biz.model;

import java.awt.image.BufferedImage;

/** Note: 未来都要开发成充血模型，而不是贫血模型。什么是充血、贫血模型？ */
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