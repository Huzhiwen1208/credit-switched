package org.credit.biz;


import java.util.HashMap;
import java.util.Map;

import org.credit.biz.common.Result;
import org.credit.biz.handler.AuthHandler;
import org.credit.biz.handler.EmailCodeHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import static org.junit.jupiter.api.Assertions.*;
import org.credit.biz.model.User;

@SpringBootTest
public class CreditSwitchApplicationTests {
    @Autowired
	private AuthHandler authHandler;
	@Autowired
	private EmailCodeHandler emailCodeHandler;
	MockHttpSession session = new MockHttpSession();

	@Test
	public void emailCodeHandler_sendEmailCode_should_return_sendEmail_sucesss_when_email_is_send_sucesss() {
		/* 模拟发送邮件验证码请求参数 */
		Map<String, String> params = new HashMap<>();
		params.put("email", "15039017198@163.com");
		params.put("captcha", "666666");
        
		/* 模拟 Session 中存储的验证码与发送来的不一样 */
		session.setAttribute("captcha", "666666");

		Result<Void> r =emailCodeHandler.sendEmailCode(params, session);
		assertEquals("邮箱验证码发送成功", r.getMsg(), "发送邮件验证码功能有问题");
	}

	@Test
	public void emailCodeHandler_sendEmailCode_should_return_sendEmail_fail_when_captcha_is_wrong() {
		/* 模拟发送邮件验证码请求参数 */
		Map<String, String> params = new HashMap<>();
		params.put("email", "15039017198@163.com");
		params.put("captcha", "666667");

		session.setAttribute("captcha", "666666");

		Result<Void> r =emailCodeHandler.sendEmailCode(params, session);
		assertEquals("图片验证码错误或者过期", r.getMsg(), "发送邮件验证码功能有问题");
	}

	@Test
	public void testAuthHander_register_should_return_register_success_when_sucsess_register() {
		/* 模拟注册请求请求参数 */
        Map<String, String> params = new HashMap<>();
        params.put("email", "test@qq.com");
	    params.put("code", "66666");
	    params.put("password", "123456");

        /* 模拟 Session 中存储的验证码和注册邮箱 */
		session.setAttribute("EMAIL_CODE_KEY", "66666");
		session.setAttribute("REGISTER_EMAIL", "test@qq.com");

		Result<Void> r =authHandler.register(params, session);
		assertEquals("注册成功", r.getMsg(), "注册功能有问题");
	}
    
	@Test
	public void testAuthHander_register_should_return_register_fail_when_captcha_is_wrong() {
		/* 模拟注册请求请求参数 */
        Map<String, String> params = new HashMap<>();
        params.put("email", "test@qq.com");
	    params.put("code", "66666");
	    params.put("password", "123456");

        /* 模拟 Session 中存储的验证码和注册邮箱 */
		session.setAttribute("EMAIL_CODE_KEY", "66667");
		session.setAttribute("REGISTER_EMAIL", "test@qq.com");

		Result<Void> r =authHandler.register(params, session);
		assertEquals("邮箱验证码错误或过期", r.getMsg(), "邮箱验证码检验功能有问题");
	}

	@Test
	public void testAuthHander_register_should_return_register_fail_when_registeremail_is_registed() {
		/* 模拟注册请求请求参数 */
        Map<String, String> params = new HashMap<>();
        params.put("email", "test@qq.com");
	    params.put("code", "66666");
	    params.put("password", "123456");

        /* 模拟 Session 中存储的验证码和注册邮箱 */
		session.setAttribute("EMAIL_CODE_KEY", "66666");
		session.setAttribute("REGISTER_EMAIL", "test@qq.com");
        authHandler.register(params, session);
        
		/*上一次注册操作会把session里用到的值清空 */
		session.setAttribute("REGISTER_EMAIL", "test@qq.com");
		session.setAttribute("EMAIL_CODE_KEY", "66666");

		Result<Void> r =authHandler.register(params, session);
		assertEquals("该邮箱已注册", r.getMsg(), "邮箱已注册检验功能有问题");
	}



	@Test
	public void testAuthHander_login_should_return_login_success_when_login_is_correct() {
		/* 模拟登录请求请求参数 */
        Map<String, String> params = new HashMap<>();
        params.put("email", "test@qq.com");
	    params.put("captcha", "666666");
	    params.put("password", "123456");

		session.setAttribute("captcha", "666666");
        
		/*模拟这个邮箱已经注册 */
		Map<String, String> params1 = new HashMap<>();
        params1.put("email", "test@qq.com");
	    params1.put("code", "66666");
	    params1.put("password", "123456");
		session.setAttribute("EMAIL_CODE_KEY", "66666");
		session.setAttribute("REGISTER_EMAIL", "test@qq.com");
		authHandler.register(params1, session);


		Result<User> r =authHandler.login(params, session);
		assertEquals("登录成功", r.getMsg(), "登录功能有问题");
	}

	@Test
	public void testAuthHander_login_should_return_errorCaptcha_when_captcha_is_wrong() {
		/* 模拟登录请求请求参数 */
        Map<String, String> params = new HashMap<>();
        params.put("email", "test@qq.com");
	    params.put("code", "66666");
	    params.put("password", "123456");
        
		/* 模拟 Session 中存储的验证码 */
		session.setAttribute("captcha", "666666");

		Result<User> r =authHandler.login(params, session);
		assertEquals("图片验证码错误或过期", r.getMsg(), "验证码检验功能有问题");
	}

	@Test
	public void testAuthHander_login_should_return_login_fail_when_logineamil_is_not_register() {
		/* 模拟登录请求请求参数 */
        Map<String, String> params = new HashMap<>();
        params.put("email", "test@qq.com");
	    params.put("captcha", "666666");
	    params.put("password", "123456");

		session.setAttribute("captcha", "666666");


		Result<User> r =authHandler.login(params, session);
		assertEquals("该邮箱未注册", r.getMsg(), "未能识别邮箱没有注册的问题");
	}

	@Test
	public void testAuthHander_login_should_return_login_fail_when_password_is_wrong() {
		/* 模拟登录请求请求参数 */
        Map<String, String> params = new HashMap<>();
        params.put("email", "test@qq.com");
	    params.put("captcha", "666666");
	    params.put("password", "123457");

		session.setAttribute("captcha", "666666");
        
		/*模拟这个邮箱已经注册 */
		Map<String, String> params1 = new HashMap<>();
        params1.put("email", "test@qq.com");
	    params1.put("code", "66666");
	    params1.put("password", "123456");
		session.setAttribute("EMAIL_CODE_KEY", "66666");
		session.setAttribute("REGISTER_EMAIL", "test@qq.com");
		authHandler.register(params1, session);


		Result<User> r =authHandler.login(params, session);
		assertEquals("密码错误", r.getMsg(), "未能识别密码错误的问题");
	}


}
