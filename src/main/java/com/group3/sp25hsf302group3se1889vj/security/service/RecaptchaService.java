package com.group3.sp25hsf302group3se1889vj.security.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class RecaptchaService {
    @Value("${recaptcha.key.secret}")
    private String recaptchaSecretKey;

    @Value("${recaptcha.url}")
    private String recaptchaUrl;

    public boolean notVerifyRecaptcha(String recaptchaResponse) {
        RestTemplate restTemplate = new RestTemplate();
        String url = recaptchaUrl + "?secret=" + recaptchaSecretKey + "&response=" + recaptchaResponse;
        Map<String, Object> response = restTemplate.postForObject(url, null, Map.class);
        return response == null || !Boolean.TRUE.equals(response.get("success"));
    }
}
