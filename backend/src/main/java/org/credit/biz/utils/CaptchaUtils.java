package org.credit.biz.utils;
import org.credit.biz.model.CaptchaResult;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
public class CaptchaUtils {

    private static final char[] CHARS = "ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789".toCharArray();
    private static final int WIDTH = 100;
    private static final int HEIGHT = 40;
    private static final int CODE_LENGTH = 4;
    private static final Random random = new Random();

    private static Color getRandomColor(int fc, int bc) {
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    public static CaptchaResult createCaptcha(){
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics(); 
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        for (int i = 0; i < 20; i++) {
               int x1 = random.nextInt(WIDTH);
               int y1 = random.nextInt(HEIGHT);
               int x2 = random.nextInt(12);
               int y2 = random.nextInt(12);
               g.setColor(getRandomColor(160, 200));
               g.drawLine(x1, y1, x2, y2);
        } 

        /* 生成验证码 */
        StringBuilder code = new StringBuilder();
        g.setFont(new Font("Times New Roman", Font.BOLD, 24));
        
        for (int i = 0; i < CODE_LENGTH; i++) {
             char c = CHARS[random.nextInt(CHARS.length)];
             code.append(c);
             g.setColor(getRandomColor(20, 130));
             g.drawString(String.valueOf(c), 15 + i * 20, 28);
        }
        
        g.dispose();
        return new CaptchaResult(code.toString(), image);
    }
}
