package org.credit.biz.service; // 【修改点1】包名要对

import org.credit.biz.model.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service // 这个注解告诉 Spring：我是干活的 Service，请把我管理起来
public class UserService {

    // ============================================================
    // 【模拟数据库】
    // 这里的 static Map 就像是一个在内存里的“记事本”。
    // 程序启动时，它存在；程序关闭，数据就丢了。
    // ============================================================
    private static final Map<String, User> MOCK_DB = new ConcurrentHashMap<>();

    public void register(String email, String password) {
        if (MOCK_DB.containsKey(email)) {
            throw new RuntimeException("该邮箱已注册");
        }

        User newUser = new User(email, password);
        MOCK_DB.put(email, newUser);
    }

    public User login(String email, String password) {
        User user = MOCK_DB.get(email);
        if (user == null) {
            throw new RuntimeException("该邮箱尚未注册");
        }

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }

        return user;
    }
}