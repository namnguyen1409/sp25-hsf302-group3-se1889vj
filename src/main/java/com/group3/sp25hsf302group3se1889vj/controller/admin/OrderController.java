package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.NotificationDTO;
import com.group3.sp25hsf302group3se1889vj.dto.OrderDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.NotificationFilterDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.OrderFilterDTO;
import com.group3.sp25hsf302group3se1889vj.enums.OrderStatus;
import com.group3.sp25hsf302group3se1889vj.service.OrderService;
import com.group3.sp25hsf302group3se1889vj.util.MetadataExtractor;
import com.group3.sp25hsf302group3se1889vj.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@PreAuthorize("hasRole('OWNER') or hasAnyAuthority('MANAGE_ORDERS')")
@Controller
@RequestMapping("/admin/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MetadataExtractor metadataExtractor;

    @GetMapping({"/list", "", "/"})
    public String list(
            Model model,
            @ModelAttribute(value = "filterDTO", binding = false) OrderFilterDTO filterDTO
    ) {
        PaginationUtil.setupPagination(
                model,
                filterDTO,
                orderService,
                metadataExtractor,
                OrderFilterDTO::new,
                OrderDTO.class,
                Arrays.asList("status", "totalPrice", "discountAmount", "finalPrice", "createdBy", "createdAt")
        );

        return "admin/order/list";
    }

    @PostMapping({"/list", "", "/"})
    public String list(
            @ModelAttribute(value = "filterDTO") OrderFilterDTO filterDTO,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("filterDTO", filterDTO);
        return "redirect:/admin/order/list";
    }


    @GetMapping("/view/{id}")
    public String view(
            @PathVariable Long id,
            Model model
    ) {
        OrderDTO orderDTO = orderService.findById(id);
        model.addAttribute("order", orderDTO);
        return "admin/order/view";
    }

    @GetMapping("/cancel/{id}")
    public String cancel(
            @PathVariable Long id,
            @RequestParam("reason") String reason
    ) {
        orderService.cancelOrder(id, reason);
        return "redirect:/admin/order/view/" + id;
    }

    @GetMapping("/confirm/{id}")
    public String confirm(
            @PathVariable Long id
    ) {
        orderService.confirmOrder(id, OrderStatus.CONFIRMED);
        return "redirect:/admin/order/view/" + id;
    }

    @GetMapping("/ship/{id}")
    public String ship(
            @PathVariable Long id
    ) {
        orderService.shipOrder(id, OrderStatus.SHIPPED);
        return "redirect:/admin/order/view/" + id;
    }

    @GetMapping("/deliver/{id}")
    public String deliver(
            @PathVariable Long id
    ) {
        orderService.deliverOrder(id, OrderStatus.DELIVERED);
        return "redirect:/admin/order/view/" + id;
    }



}