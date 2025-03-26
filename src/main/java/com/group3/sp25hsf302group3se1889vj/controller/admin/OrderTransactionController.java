package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.OrderTransactionDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.OrderTransactionFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.OrderTransactionService;
import com.group3.sp25hsf302group3se1889vj.util.MetadataExtractor;
import com.group3.sp25hsf302group3se1889vj.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@PreAuthorize("hasRole('OWNER')")
@Controller
@RequestMapping("/admin/transaction")
@RequiredArgsConstructor
public class OrderTransactionController {

    private final OrderTransactionService orderTransactionService;
    private final MetadataExtractor metadataExtractor;

    @GetMapping({"/list", "", "/"})
    public String list(
            Model model,
            @ModelAttribute(value = "filterDTO", binding = false) OrderTransactionFilterDTO filterDTO
    ) {
        PaginationUtil.setupPagination(
                model,
                filterDTO,
                orderTransactionService,
                metadataExtractor,
                OrderTransactionFilterDTO::new,
                OrderTransactionDTO.class,
                Arrays.asList("orderId", "amount", "status", "vnPayResponseCode", "type", "createdAt")
        );
        return "admin/transaction/list";
    }

    @PostMapping({"/list", "", "/"})
    public String list(
            @ModelAttribute(value = "filterDTO") OrderTransactionFilterDTO filterDTO,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("filterDTO", filterDTO);
        return "redirect:/admin/transaction/list";
    }

    @GetMapping("/view/{id}")
    public String view(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("orderTransaction", orderTransactionService.getOrderTransactionById(id));
        return "admin/transaction/view";
    }

}