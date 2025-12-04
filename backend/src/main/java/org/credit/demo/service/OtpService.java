package org.credit.demo.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private static class OtpEntry {
        String otp;
        long expireAt;
    }

    private final Map<String, OtpEntry> otps = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public String generateOtp(String email, int ttlSeconds) {
        int code = 100000 + random.nextInt(900000);
        OtpEntry e = new OtpEntry();
        e.otp = String.valueOf(code);
        e.expireAt = Instant.now().getEpochSecond() + ttlSeconds;
        otps.put(email.toLowerCase(), e);
        return e.otp;
    }

    public boolean validateOtp(String email, String otp) {
        if (email == null || otp == null) return false;
        OtpEntry e = otps.get(email.toLowerCase());
        if (e == null) return false;
        if (Instant.now().getEpochSecond() > e.expireAt) {
            otps.remove(email.toLowerCase());
            return false;
        }
        boolean ok = e.otp.equals(otp);
        if (ok) otps.remove(email.toLowerCase()); // one-time
        return ok;
    }
}
