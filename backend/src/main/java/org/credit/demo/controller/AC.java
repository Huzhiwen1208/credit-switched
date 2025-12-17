package org.credit.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.credit.demo.Result;
import org.credit.demo.service.UserService;
import org.credit.demo.entity.User;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletRequest; // ✅ 改成 jakarta
import jakarta.servlet.http.HttpServletResponse;
// 只需要修改这一行
import jakarta.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
// Spring Web 注解
import org.springframework.web.bind.annotation.PostMapping; // 解决 PostMapping
import org.springframework.web.bind.annotation.RequestBody; // 解决 RequestBody
// Spring 邮件相关 (解决 JavaMailSender 和 SimpleMailMessage)
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@CrossOrigin
@RestController
@RequestMapping("/apply")
public class AC {
    private static final char[] CHARS = "ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789".toCharArray();
    private static final int WIDTH = 100;  // 图片宽度
    private static final int HEIGHT = 40;  // 图片高度
    private static final int CODE_LENGTH = 4; // 验证码长度

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    @GetMapping("/image")
    public void getImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 创建图片
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();//绑定画笔
        Random random = new Random();

        // 填充背景色
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        for (int i = 0; i < 20; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(12);
            int y2 = random.nextInt(12);
            g.setColor(getRandomColor(160, 200));
    // ...算出两个随机坐标点 (x1,y1) 和 (x2,y2)...
            g.drawLine(x1, y1, x2, y2);
       } 

        // 生成随机验证码
        StringBuilder code = new StringBuilder();
        g.setFont(new Font("Times New Roman", Font.BOLD, 24));
        
        for (int i = 0; i < CODE_LENGTH; i++) {
            char c = CHARS[random.nextInt(CHARS.length)];
            code.append(c);
            
            // 设置随机颜色 (深一点的颜色，方便用户看)
            g.setColor(getRandomColor(20, 130));
            
            // 绘制字符 (x坐标递增，y坐标随机微调增加防破解难度)
            // x: 10 + i * 20 -> 让字符横向拉开距离
            // y: 25 -> 基准线
            g.drawString(String.valueOf(c), 15 + i * 20, 28);
        }

        // 存储验证码到Session
        HttpSession session = request.getSession();
        session.setAttribute("captcha", code.toString());

        // 设置响应头，告诉浏览器不要缓存图片
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        // 输出图片到响应流
        ImageIO.write(image, "JPEG", response.getOutputStream());
        g.dispose();
    }
    private Color getRandomColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    @PostMapping("/send-email-code")
    public String sendEmailCode(@RequestBody Map<String, String> params, HttpSession session) {
        String email = params.get("email");
        String imageCaptcha = params.get("captcha"); // 前端传来的图片验证码

        // 1. 从 Session 获取之前生成的图片验证码 (注意这里 Key 要和 CaptchaController 里一致)
        String sessionCaptcha = (String) session.getAttribute("captcha");

        // 2. 校验图片验证码
        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(imageCaptcha)) {
            // 验证失败，返回错误信息
            // 这里的 400 是为了让前端 axios 进入 catch 逻辑，或者你返回 200 自己判断也行
            throw new RuntimeException("图片验证码错误或已过期"); 
        }

        // 3. 验证通过，生成 6 位随机数字作为邮箱验证码
        String emailCode = String.valueOf(new Random().nextInt(899999) + 100000);

        // 4. 发送邮件 (建议生产环境放入异步任务，防止阻塞)
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2587879285@qq.com"); // 必须和配置文件一致
        message.setTo(email);
        message.setSubject("yihao界面注册验证码");
        message.setText("您的注册验证码是：" + emailCode + "，有效期5分钟。");
        mailSender.send(message);

        // 5. 【关键】将邮箱验证码存入 Session，供后续注册接口校验
        session.setAttribute("EMAIL_CODE_KEY", emailCode);
        session.setAttribute("REGISTER_EMAIL", email); // 绑定邮箱，防止用户发给A邮箱却用B邮箱注册
        
        // 6. 销毁图片验证码（防止同一个验证码被重复使用）
        session.removeAttribute("captcha");

        return "验证码已发送";
    }

    @PostMapping("/register")
    public String register(@RequestBody Map<String, String> params, HttpSession session) {
        String email = params.get("email");
        String code = params.get("code");
        String password = params.get("password");

        // 1. 校验邮箱验证码
        String sessionCode = (String) session.getAttribute("EMAIL_CODE_KEY");
        String sessionEmail = (String) session.getAttribute("REGISTER_EMAIL");

        // 【新增】这两行打印代码，专门用来捉虫！
        System.out.println(">>> 真正有效的验证码是：" + sessionCode);
        System.out.println(">>> 你刚才输入的验证码是：" + code);

        if (sessionCode == null || !sessionCode.equals(code)) {
            throw new RuntimeException("邮箱验证码错误");
        }
        if (!email.equals(sessionEmail)) {
            throw new RuntimeException("注册邮箱与发送验证码邮箱不一致");
        }

        // 2. 调用 UserService 完成注册
        userService.register(email, password);
        

        // 3. 注册成功后清理 Session
        session.removeAttribute("EMAIL_CODE_KEY");
        session.removeAttribute("REGISTER_EMAIL");

        return "注册成功";
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> params, HttpSession session) {
        String email = params.get("email");
        String password = params.get("password");
        String userCaptcha = params.get("captcha");

        // 1. 校验图片验证码
        String sessionCaptcha = (String) session.getAttribute("captcha");
        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(userCaptcha)) {
            throw new RuntimeException("图片验证码错误或已失效");
        }
        session.removeAttribute("captcha");

        // 2. 调用 Service 进行登录校验 (从 static Map 读取)
        User user = userService.login(email, password);

        // 3. 登录成功，存入 Session
        session.setAttribute("LOGIN_USER", user);

        return "登录成功";
    }

    
}


    
    

