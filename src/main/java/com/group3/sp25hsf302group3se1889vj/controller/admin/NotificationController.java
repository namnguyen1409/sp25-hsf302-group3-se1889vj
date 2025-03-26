package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.NotificationDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.NotificationFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.NotificationService;
import com.group3.sp25hsf302group3se1889vj.util.MetadataExtractor;
import com.group3.sp25hsf302group3se1889vj.util.PaginationUtil;
import com.group3.sp25hsf302group3se1889vj.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequestMapping("/admin/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final MetadataExtractor metadataExtractor;
    private final NotificationService notificationService;

    @GetMapping({"/list", "", "/"})
    public String list(
            Model model,
            @ModelAttribute(value = "filterDTO", binding = false) NotificationFilterDTO filterDTO
    ) {
        if (filterDTO == null) {
            filterDTO = new NotificationFilterDTO();
        }
        filterDTO.setUsername(SecurityUtil.getCurrentUsername());
        PaginationUtil.setupPagination(
                model,
                filterDTO,
                notificationService,
                metadataExtractor,
                NotificationFilterDTO::new,
                NotificationDTO.class,
                Arrays.asList("title", "content", "isRead", "type","createdAt", "updatedAt")
        );
        return "admin/notification/list";
    }

    @PostMapping({"/list", "", "/"})
    public String list(
            @ModelAttribute(value = "filterDTO") NotificationFilterDTO filterDTO,
            Model model
    ) {
        return list(model, filterDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> delete(@PathVariable Long id) {
        var notification = notificationService.findByIdAndUsername(id, SecurityUtil.getCurrentUsername());
        if (notification == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy thông báo");
        }
        notificationService.delete(id);
        return ResponseEntity.ok("Đã xóa thông báo");
    }

    @DeleteMapping("/delete-all")
    @ResponseBody
    public ResponseEntity<String> deleteAll() {
        notificationService.deleteAll(SecurityUtil.getCurrentUsername());
        return ResponseEntity.ok("Đã xóa tất cả thông báo");
    }

    @PostMapping("/mark-all-as-read")
    @ResponseBody
    public ResponseEntity<String> markAllAsRead() {
        notificationService.markAllAsRead(SecurityUtil.getCurrentUsername());
        return ResponseEntity.ok("Đã đánh dấu tất cả thông báo là đã đọc");
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        var notification = notificationService.findByIdAndUsername(id, SecurityUtil.getCurrentUsername());
        if (notification == null) {
            return "redirect:/admin/notification";
        }
        notificationService.markAsRead(id);
        model.addAttribute("entity", notification);
        return "admin/notification/view";
    }


}
