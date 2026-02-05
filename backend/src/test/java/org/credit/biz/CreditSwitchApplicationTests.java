package org.credit.biz;


import org.credit.biz.common.Result;
import org.credit.biz.handler.AuthHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import lombok.RequiredArgsConstructor;

/** TODO: 补充方法的单元测试, 并跑起来 */
@SpringBootTest
@RequiredArgsConstructor
class CreditSwitchApplicationTests {

	private AuthHandler authHandler;

	@Test
	void testAuthHander_login_should_return_NotRegister_when_not_register() {
		Result<Void> r = authHandler.login(null, null);
		Assert.isTrue(r.getMsg() == "该邮箱尚未注册", "null");
	}

}
