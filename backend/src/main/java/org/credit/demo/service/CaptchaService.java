package org.credit.demo.service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CaptchaService {

    private static class CaptchaEntry {
        String code;
        long expireAt;
    }

    private final Map<String, CaptchaEntry> store = new ConcurrentHashMap<>();
    private final DefaultKaptcha captchaProducer;

    public CaptchaService(DefaultKaptcha captchaProducer) {
        this.captchaProducer = captchaProducer;
    }

    public CaptchaResponse generateCaptcha() throws Exception {
        String code = captchaProducer.createText();
        BufferedImage image = captchaProducer.createImage(code);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());
        String uuid = UUID.randomUUID().toString();
        CaptchaEntry entry = new CaptchaEntry();
        entry.code = code;
        entry.expireAt = System.currentTimeMillis() + 3 * 60 * 1000; // 3 minutes
        store.put(uuid, entry);
        System.out.println("[DEBUG] generateCaptcha uuid=" + uuid + " code='" + code + "'");

        CaptchaResponse resp = new CaptchaResponse();
        resp.setUuid(uuid);
        resp.setImageBase64("data:image/png;base64," + base64);
        return resp;
    }

    public boolean validateCaptcha(String uuid, String input) {
        if (uuid == null || input == null) return false;
        CaptchaEntry entry = store.get(uuid);
        if (entry == null) return false;
        if (System.currentTimeMillis() > entry.expireAt) {
            store.remove(uuid);
            return false;
        }
        String expected = entry.code == null ? "" : entry.code.trim();
        String actual = input == null ? "" : input.trim();
        boolean ok = expected.equalsIgnoreCase(actual);
        System.out.println("[DEBUG] validateCaptcha uuid=" + uuid + " expected='" + expected + "' actual='" + actual + "' ok=" + ok);
        // Remove after validation attempt
        store.remove(uuid);
        return ok;
    }

    // debug helper: get the expected code for given uuid (for dev only)
    public String getCodeForDebug(String uuid) {
        CaptchaEntry e = store.get(uuid);
        return e == null ? null : e.code;
    }

    public static class CaptchaResponse {
        private String uuid;
        private String imageBase64;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getImageBase64() {
            return imageBase64;
        }

        public void setImageBase64(String imageBase64) {
            this.imageBase64 = imageBase64;
        }
    }
}
