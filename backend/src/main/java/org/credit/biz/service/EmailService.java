package org.credit.biz.service;
import org.credit.biz.common.Result;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

/** Note: 去除 interface + impl 的模式 */
public interface EmailService {
    Result<Void> sendEmailCode(Map<String, String> params, HttpSession session);
}