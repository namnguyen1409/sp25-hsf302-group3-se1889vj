package com.group3.sp25hsf302group3se1889vj.service;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
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
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", orderId);
        params.put("vnp_OrderInfo", "Thanh toán đơn hàng: " + orderId);
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", vnp_Locale);
        params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        params.put("vnp_CreateDate", formatter.format(cld.getTime()));

        String queryUrl = buildQuery(params);
        log.info("Query URL: {}", queryUrl);
        return vnp_Url + "?" + queryUrl;
    }



    private String buildQuery(Map<String, String> params) {
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                try {
                    String encodedValue = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString());
                    hashData.append(fieldName).append('=').append(encodedValue).append('&');
                    log.info("Hash data: {}", hashData);
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()))
                            .append('=')
                            .append(encodedValue)
                            .append('&');
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("Lỗi mã hóa URL", e);
                }
            }
        }

        if (hashData.length() > 0) {
            hashData.setLength(hashData.length() - 1);
            query.setLength(query.length() - 1);
        }

        String vnp_SecureHash = hmacSHA512(hashData.toString(), vnp_HashSecret);
        return query.append("&vnp_SecureHash=").append(vnp_SecureHash).toString();
    }



    public String hashAllFields(Map<String, String> fields) {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                sb.append(fieldName).append("=").append(fieldValue);
                sb.append("&");
            }
        }
        String rawData = sb.substring(0, sb.length() - 1);
        return hmacSHA512(rawData, vnp_HashSecret);
    }

    public int orderReturn(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        Enumeration<String> params = request.getParameterNames();

        while (params.hasMoreElements()) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);

            if (fieldValue != null && !fieldValue.isEmpty()) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        String signValue = hashAllFields(fields);
        if (signValue.equals(vnp_SecureHash)) {
            return "00".equals(request.getParameter("vnp_TransactionStatus")) ? 1 : 0;
        } else {
            return -1;
        }
    }


    private String hmacSHA512(String data, String secretKey) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(secretKeySpec);
            byte[] bytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (Exception e) {
            e.printStackTrace();  // In stack trace chi tiết khi lỗi
            throw new RuntimeException("Error generating HMAC-SHA512", e);
        }
    }



    public boolean validateReturnUrl(Map<String, String> paramMap, String secureHash) {
        Map<String, String> fields = new HashMap<>(paramMap);
        fields.remove("vnp_SecureHash");
        String signValue = hashAllFields(fields);
        return signValue.equals(secureHash);
    }

}
