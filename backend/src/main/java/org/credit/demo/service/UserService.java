package org.credit.demo.service; // 【修改点1】包名要对

import org.credit.demo.entity.User; // 【修改点2】引入你刚才建的 User 类
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service // 【关键】这个注解告诉 Spring：我是干活的 Service，请把我管理起来
public class UserService {

    // ============================================================
    // 【模拟数据库】
    // 这里的 static Map 就像是一个在内存里的“记事本”。
    // 程序启动时，它存在；程序关闭，数据就丢了。
    // ============================================================
    private static final Map<String, User> MOCK_DB = new ConcurrentHashMap<>();

    // 预存一个测试账号
    static {
        MOCK_DB.put("test@qq.com", new User("test@qq.com", "123456"));
    }

    /**
     * 注册方法
     */
    public void register(String email, String password) {
        // 1. 查重
        if (MOCK_DB.containsKey(email)) {
            throw new RuntimeException("该邮箱已注册");
        }

        // 2. 保存
        User newUser = new User(email, password);
        MOCK_DB.put(email, newUser);
        
        System.out.println(">>> 模拟数据库：写入新用户 " + email);
    }

    /**
     * 登录方法
     */
    public User login(String email, String password) {
        // 1. 查人
        User user = MOCK_DB.get(email);

        // 2. 校验是否存在
        if (user == null) {
            throw new RuntimeException("该邮箱尚未注册");
        }

        // 3. 校验密码
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }

        return user;
    }
}