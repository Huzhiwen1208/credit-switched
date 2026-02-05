package org.credit.biz.service;

import org.credit.biz.common.Result;

import org.credit.biz.model.User;

public interface UserService {
   Result<Void> register(String email, String password);
   Result<User> login(String email, String password);
   
}
