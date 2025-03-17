package com.group3.sp25hsf302group3se1889vj.controller.common;

import com.group3.sp25hsf302group3se1889vj.dto.OrderTransactionDTO;
import com.group3.sp25hsf302group3se1889vj.enums.OrderStatus;
import com.group3.sp25hsf302group3se1889vj.enums.OrderTransactionStatus;
import com.group3.sp25hsf302group3se1889vj.enums.OrderTransactionType;
import com.group3.sp25hsf302group3se1889vj.service.OrderService;
import com.group3.sp25hsf302group3se1889vj.service.OrderTransactionService;
import com.group3.sp25hsf302group3se1889vj.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class VNPayController {
    private final VNPayService vnPayService;
    private final OrderTransactionService orderTransactionService;
    private final OrderService orderService;


    @GetMapping("/vnpay-return")
    public String handleVNPayReturn(
            HttpServletRequest request,
            @RequestParam Map<String, String> responseParams,
            Model model) {
        String transactionId = responseParams.get("vnp_TxnRef");
        BigDecimal amount = new BigDecimal(responseParams.get("vnp_Amount")).divide(new BigDecimal(100));
        String responseCode = responseParams.get("vnp_ResponseCode");
        OrderTransactionDTO orderTransactionDTO = new OrderTransactionDTO();
        orderTransactionDTO.setTransactionId(transactionId);
        orderTransactionDTO.setAmount(amount);
        orderTransactionDTO.setVnPayResponseCode(responseCode);
        orderTransactionDTO.setType(OrderTransactionType.PAYMENT);
        if (responseCode.equals("00")) {
            orderTransactionDTO.setStatus(OrderTransactionStatus.SUCCESS);
            orderService.updateOrderStatus(Long.parseLong(transactionId), OrderStatus.PAYMENT_SUCCESS);
            model.addAttribute("message", "Thanh toán thành công");
        } else {
            orderTransactionDTO.setStatus(OrderTransactionStatus.FAILED);
            model.addAttribute("message", "Thanh toán thất bại");
        }
        orderTransactionService.save(orderTransactionDTO);
        return "common/vnpay-return";
    }


}
