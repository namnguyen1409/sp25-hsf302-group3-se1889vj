package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.NotificationDTO;
import com.group3.sp25hsf302group3se1889vj.dto.OrderDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.NotificationFilterDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.OrderFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.NotificationService;
import com.group3.sp25hsf302group3se1889vj.util.MetadataExtractor;
import com.group3.sp25hsf302group3se1889vj.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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
        if(filterDTO == null) {
            filterDTO = new NotificationFilterDTO();
        }
        Sort sortDirection = "asc".equalsIgnoreCase(filterDTO.getDirection())
                ? Sort.by(filterDTO.getOrderBy()).ascending()
                : Sort.by(filterDTO.getOrderBy()).descending();

        List<String> fields = Arrays.asList("title", "content", "isRead", "type","createdAt", "updatedAt");
        model.addAttribute("fields", fields);
        model.addAttribute("fieldTitles", metadataExtractor.getFieldTitles(NotificationDTO.class, fields));
        model.addAttribute("fieldClasses", metadataExtractor.getFieldClasses(NotificationDTO.class, fields));

        filterDTO.setUsername(SecurityUtil.getCurrentUsername());

        Pageable pageable = PageRequest.of(filterDTO.getPage() - 1, filterDTO.getSize(), sortDirection);

        Page<NotificationDTO> page = notificationService.findAll(filterDTO, pageable);
        model.addAttribute("pages", page);
        model.addAttribute("filterDTO", filterDTO);
        return "admin/notification/list";
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

    @DeleteMapping("/deleteAll")
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
