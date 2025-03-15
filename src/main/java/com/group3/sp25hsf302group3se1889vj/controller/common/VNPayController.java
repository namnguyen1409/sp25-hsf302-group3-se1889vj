package com.group3.sp25hsf302group3se1889vj.controller.common;

import com.group3.sp25hsf302group3se1889vj.dto.OrderTransactionDTO;
import com.group3.sp25hsf302group3se1889vj.enums.OrderTransactionStatus;
import com.group3.sp25hsf302group3se1889vj.enums.OrderTransactionType;
import com.group3.sp25hsf302group3se1889vj.service.OrderTransactionService;
import com.group3.sp25hsf302group3se1889vj.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class VNPayController {
    private final VNPayService vnPayService;
    private final OrderTransactionService orderTransactionService;


    @GetMapping("/vnpay-return")
    public String handleVNPayReturn(HttpServletRequest request, Model model) {
        Map<String, String[]> paramMap = request.getParameterMap();

        String transactionId = paramMap.get("vnp_TxnRef")[0];
        String responseCode = paramMap.get("vnp_ResponseCode")[0];
        String secureHash = paramMap.get("vnp_SecureHash")[0];
        BigDecimal amount = new BigDecimal(paramMap.get("vnp_Amount")[0]).divide(BigDecimal.valueOf(100));

        if(!vnPayService.validateReturnUrl(paramMap, secureHash)) {
            model.addAttribute("message", "Invalid request");
            return "vnpay-return";
        }

        OrderTransactionDTO orderTransactionDTO = new OrderTransactionDTO();
        orderTransactionDTO.setTransactionId(transactionId);
        orderTransactionDTO.setAmount(amount);
        orderTransactionDTO.setVnPayResponseCode(responseCode);
        orderTransactionDTO.setType(OrderTransactionType.PAYMENT);
        if (responseCode.equals("00")) {
            orderTransactionDTO.setStatus(OrderTransactionStatus.SUCCESS);
            model.addAttribute("message", "Payment successful");
        } else {
            orderTransactionDTO.setStatus(OrderTransactionStatus.FAILED);
            model.addAttribute("message", "Payment failed");
        }
        orderTransactionService.save(orderTransactionDTO);
        return "vnpay-return";
    }
}
