package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.enums.OrderStatus;
import com.group3.sp25hsf302group3se1889vj.enums.RoleType;
import com.group3.sp25hsf302group3se1889vj.repository.OrderRepository;
import com.group3.sp25hsf302group3se1889vj.repository.ProductRepository;
import com.group3.sp25hsf302group3se1889vj.repository.UserRepository;
import com.group3.sp25hsf302group3se1889vj.service.OrderService;
import com.group3.sp25hsf302group3se1889vj.service.ProductService;
import com.group3.sp25hsf302group3se1889vj.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.stream.Collectors;

@PreAuthorize("hasAnyRole('OWNER', 'STAFF')")
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class HomeController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @RequestMapping("/home")
    public String home() {
        return "admin/home";
    }

    @RequestMapping("/dashboard")
    public String dashboard(
            Model model
    ) {
        Long totalCustomers = userRepository.countByRole(RoleType.CUSTOMER);
        model.addAttribute("totalCustomers", totalCustomers);
        Long totalStaffs = userRepository.countByRole(RoleType.STAFF);
        model.addAttribute("totalStaffs", totalStaffs);
        Long totalMales = userRepository.countByGender(Boolean.TRUE);
        model.addAttribute("totalMales", totalMales);
        Long totalFemales = userRepository.countByGender(Boolean.FALSE);
        model.addAttribute("totalFemales", totalFemales);
        Long totalActiveUsers = userRepository.countByIsActive(Boolean.TRUE);
        model.addAttribute("totalActiveUsers", totalActiveUsers);
        Long totalInactiveUsers = userRepository.countByIsActive(Boolean.FALSE);
        model.addAttribute("totalInactiveUsers", totalInactiveUsers);

        var countByMonth = userRepository.countNewUsersByMonth().stream()
                .map(objects -> Map.of(
                        "year", objects[0],
                        "month", objects[1],
                        "count", objects[2]
                )).toList();
        model.addAttribute("countByMonth", countByMonth);

        Long totalProducts = productRepository.count();
        model.addAttribute("totalProducts", totalProducts);
        var countProductsByCategory = productRepository.countProductsByCategory().stream()
                .filter(objects -> objects[0] != null && objects[1] != null)
                .collect(Collectors.toMap(
                        objects -> (String) objects[0],
                        objects -> ((Number) objects[1]).longValue()
                ));
        model.addAttribute("countProductsByCategory", countProductsByCategory);

        var countProductsByBrand = productRepository.countProductsByBrand().stream()
                .filter(objects -> objects[0] != null && objects[1] != null)
                .collect(Collectors.toMap(
                        objects -> (String) objects[0],
                        objects -> ((Number) objects[1]).longValue()
                ));
        model.addAttribute("countProductsByBrand", countProductsByBrand);

        var countProductsByStatus = productRepository.countProductsByStatus().stream()
                .filter(objects -> objects[0] != null && objects[1] != null)
                .collect(Collectors.toMap(
                        objects -> (Boolean) objects[0] ? "Đang bán" : "Ngừng bán",
                        objects -> ((Number) objects[1]).longValue()
                ));
        model.addAttribute("countProductsByStatus", countProductsByStatus);

        Map<OrderStatus, String> statusLabels = Map.of(
                OrderStatus.PENDING_PAYMENT, "Chờ thanh toán",
                OrderStatus.PAYMENT_SUCCESS, "Thanh toán thành công",
                OrderStatus.CONFIRMED, "Đã xác nhận",
                OrderStatus.SHIPPED, "Đang giao",
                OrderStatus.DELIVERED, "Đã giao hàng",
                OrderStatus.CANCELLED, "Đã hủy",
                OrderStatus.REFUNDED, "Hoàn tiền"
        );

        var countOrdersByStatus = orderRepository.countOrdersByStatus().stream()
                .filter(objects -> objects[0] != null && objects[1] != null)
                .collect(Collectors.toMap(
                        objects -> statusLabels.get(OrderStatus.valueOf(objects[0].toString())), // Đổi sang tiếng Việt
                        objects -> ((Number) objects[1]).longValue()
                ));
        model.addAttribute("countOrdersByStatus", countOrdersByStatus);

        var totalRevenueByDay = orderRepository.totalRevenueByDay().stream()
                .map(objects -> Map.of(
                        "year", objects[0],
                        "month", objects[1],
                        "day", objects[2],
                        "total", objects[3]
                )).toList();

        model.addAttribute("totalRevenueByDay", totalRevenueByDay);

        var countOrdersByMonth = orderRepository.countOrdersByMonth().stream()
                .map(objects -> Map.of(
                        "year", objects[0],
                        "month", objects[1],
                        "count", objects[2]
                )).toList();
        model.addAttribute("countOrdersByMonth", countOrdersByMonth);


        return "admin/dashboard";
    }
}
