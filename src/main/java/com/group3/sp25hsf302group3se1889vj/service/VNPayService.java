package com.group3.sp25hsf302group3se1889vj.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VNPayService {

    @Value("${vnpay.vnp_Version}")
    private String vnp_Version;

    @Value("${vnpay.vnp_TmnCode}")
    private String vnp_TmnCode;

    @Value("${vnpay.vnp_CurrCode}")
    private String vnp_CurrCode;

    @Value("${vnpay.vnp_Locale}")
    private String vnp_Locale;

    @Value("${vnpay.vnp_IpAddr}")
    private String vnp_IpAddr;

    @Value("${vnpay.vnp_HashSecret}")
    private String vnp_HashSecret;

    @Value("${vnpay.vnp_Url}")
    private String vnp_Url;

    @Value("${vnpay.vnp_ReturnUrl}")
    private String vnp_ReturnUrl;

    public String createPaymentUrl(BigDecimal amount, String orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", vnp_Version);
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", vnp_TmnCode);
        params.put("vnp_Amount", amount.multiply(new BigDecimal(100)).toBigInteger().toString());
        params.put("vnp_CurrCode", vnp_CurrCode);
        params.put("vnp_TxnRef", orderId);
        params.put("vnp_OrderInfo", "Thanh toán đơn hàng: " + orderId);
        params.put("vnp_OrderType", "billpayment");
        params.put("vnp_Locale", vnp_Locale);
        params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        params.put("vnp_IpAddr", vnp_IpAddr);
        params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        return vnp_Url + "?" + buildQuery(params);
    }

    private String buildQuery(Map<String, String> params) {
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder data = new StringBuilder();
        for (String fieldName : fieldNames) {
            if (!params.get(fieldName).isEmpty()) {
                data.append(fieldName).append("=").append(URLEncoder.encode(params.get(fieldName), StandardCharsets.UTF_8)).append("&");
            }
        }
        String rawData = data.substring(0, data.length() - 1);
        String secureHash = hmacSHA512(rawData, vnp_HashSecret);
        return rawData + "&vnp_SecureHash=" + secureHash;
    }

    private String hmacSHA512(String data, String secretKey) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(secretKey.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(String.format("%02x", aByte));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }


    public boolean validateReturnUrl(Map<String, String[]> paramMap, String secureHash) {
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            params.put(entry.getKey(), entry.getValue()[0]);
        }
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder data = new StringBuilder();
        for (String fieldName : fieldNames) {
            if (!params.get(fieldName).isEmpty()) {
                data.append(fieldName).append("=").append(URLEncoder.encode(params.get(fieldName), StandardCharsets.UTF_8)).append("&");
            }
        }
        String rawData = data.substring(0, data.length() - 1);
        String secureHashResponse = hmacSHA512(rawData, vnp_HashSecret);
        return secureHash.equals(secureHashResponse);
    }
}
