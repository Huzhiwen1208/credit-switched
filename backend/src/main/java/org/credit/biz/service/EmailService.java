package org.credit.biz.service;
import org.credit.biz.common.Result;
import jakarta.servlet.http.HttpSession;
import java.util.Map;


public interface EmailService {
    Result<Void> sendEmailCode(Map<String, String> params, HttpSession session);
}