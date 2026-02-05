package org.credit.biz.handler;
import lombok.RequiredArgsConstructor;
import org.credit.biz.common.Result;
import org.credit.biz.service.EmailService;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor

public class EmailCodeHandler {
    private final EmailService emailService;

    @PostMapping("/send-email-code")
    public Result<Void> sendEmailCode(@RequestBody Map<String, String> params, HttpSession session) {
        Result<Void> result = emailService.sendEmailCode(params, session);
        return result;
    }
}
