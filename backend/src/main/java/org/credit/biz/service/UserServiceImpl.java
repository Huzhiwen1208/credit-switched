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

    private final String msg_1 = "该邮箱已注册";
    private final String msg_2 = "注册成功";
    private final String msg_3 = "该邮箱尚未注册";
    private final String msg_4 = "密码错误";
    private final String msg_5 = "登录成功";
    
    @Override
    public Result<Void> register(String email, String password) {
        /* 1. 检查邮箱是否已经注册 */
        if (MOCK_DB.containsKey(email)) {
            Result<Void> result = new Result<>(400, msg_1, null);
            return result;
        }
        /* 2. 创建新用户并保存到“数据库” */
        User newUser = new User(email, password);
        MOCK_DB.put(email, newUser);

        Result<Void> result = new Result<>(200, msg_2, null);
        return result;
    }
    
    @Override
    public Result<User> login(String email, String password) {
        /*1.获取模拟数据库里与登录邮箱对应的User对象 */
        User user = MOCK_DB.get(email);
        
        /*2.检验邮箱是否注册 */
        if (user == null) {
            Result<User> result = new Result<>(400, msg_3, null);
            return result;
        }
        
            /*3.检验密码是否正确 */
        if (!user.getPassword().equals(password)) {
            Result<User> result = new Result<>(400, msg_4, null);
            return result;
        }
        
        Result<User> result = new Result<>(200, msg_5, user);
        return result;
    }
}