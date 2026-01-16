package org.credit.biz.service;

import org.credit.biz.common.Result;
import org.credit.biz.model.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service 
public class UserServiceImpl implements UserService {

    /* ============================================================
    【模拟数据库】
    这里的 static Map 就像是一个在内存里的“记事本”。
    程序启动时，它存在；程序关闭，数据就丢了。
     ============================================================*/
    private static final Map<String, User> MOCK_DB = new ConcurrentHashMap<>();
    
    @Override
    public Result<Void> register(String email, String password) {
        if (MOCK_DB.containsKey(email)) {
            Result<Void> result = new Result<>(400, "该邮箱已注册", null);
            return result;
        }

        User newUser = new User(email, password);
        MOCK_DB.put(email, newUser);

        String msg="注册成功";
        Result<Void> result = new Result<>(200, msg, null);
        return result;
    }
    
    @Override
    public Result<User> login(String email, String password) {
        User user = MOCK_DB.get(email);
        if (user == null) {
            Result<User> result = new Result<>(400, "该邮箱尚未注册", null);
            return result;
        }

        if (!user.getPassword().equals(password)) {
            Result<User> result = new Result<>(400, "密码错误", null);
            return result;
        }
        String msg="登录成功";
        Result<User> result = new Result<>(200, msg, user);
        return result;
    }
}