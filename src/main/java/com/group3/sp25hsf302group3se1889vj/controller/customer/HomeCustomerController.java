package com.group3.sp25hsf302group3se1889vj.controller.customer;

import com.group3.sp25hsf302group3se1889vj.dto.*;
import com.group3.sp25hsf302group3se1889vj.dto.filter.BannerFilterDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.CustomerProductSearchDTO;
import com.group3.sp25hsf302group3se1889vj.entity.User;
import com.group3.sp25hsf302group3se1889vj.service.*;
import com.group3.sp25hsf302group3se1889vj.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@AllArgsConstructor
public class HomeCustomerController {

    private final BannerService bannerService;
    private final StaticPageService staticPageService;
    private final ProductService productService;
    private final ShoppingCartService shoppingCartService;
    private final CouponService couponService;
    private final UserCouponService userCouponService;
    private final CustomerAddressService customerAddressService;
    private final OrderService orderService;
    private final VNPayService vnPayService;

    @GetMapping({"/", ""})
    public String home(
            Model model
    ) {
        List<BannerDTO> banners = bannerService.findAll(new BannerFilterDTO(), PageRequest.of(0, 5)).getContent();
        List<CustomerProductDTO> newProducts = productService.getNewProducts(10);
        log.info("newProducts: {}", newProducts.stream().count());
        model.addAttribute("newProducts", newProducts);

        model.addAttribute("banners", banners);
        return "customer/home";
    }

    @GetMapping("/pages/{slug}")
    public String page(
            Model model,
            @PathVariable("slug") String slug
    ) {
        StaticPageDTO page = staticPageService.findBySlug(slug);
        model.addAttribute("page", page);
        return "customer/page";
    }

    @GetMapping("/search")
    public String search(
            Model model,
            @ModelAttribute(value = "filterDTO", binding = false) CustomerProductSearchDTO filterDTO
    ) {
        if (filterDTO == null) filterDTO = new CustomerProductSearchDTO();
        String orderBy = filterDTO.getOrderBy() == null ? "createdAt" : filterDTO.getOrderBy();
        Sort sortDirection = "asc".equalsIgnoreCase(filterDTO.getDirection())
                ? Sort.by(orderBy).ascending()
                : Sort.by(orderBy).descending();

        Pageable pageable = PageRequest.of(filterDTO.getPage() - 1, filterDTO.getSize(), sortDirection);

        Page<CustomerProductDTO> products = productService.searchProducts(filterDTO, pageable);

        log.info("products: {}", products.getContent().size());

        model.addAttribute("products", products);
        model.addAttribute("filterDTO", filterDTO);
        return "customer/search";
    }

    @PostMapping("/search")
    public String search(
            @ModelAttribute(value = "filterDTO") CustomerProductSearchDTO filterDTO,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("filterDTO", filterDTO);
        return "redirect:/search";
    }

    @GetMapping("/product/{id}")
    public String product(
            Model model,
            @PathVariable("id") Long id
    ) {
        if(productService.isProductActive(id) == false) {
            return "redirect:/";
        }

        CustomerProductDTO product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "customer/product";
    }

    @PostMapping("/cart/add")
    @ResponseBody
    public ResponseEntity<String> addToCart(
            @RequestBody ShoppingCartDTO shoppingCartDTO
    ) {
        log.info("shoppingCartDTO: {}", shoppingCartDTO);
        shoppingCartDTO.setCreatedBy(SecurityUtil.getCurrentUsername());
        shoppingCartService.addToCart(shoppingCartDTO);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/cart/remove")
    @ResponseBody
    public ResponseEntity<String> removeFromCart(
            @RequestBody ShoppingCartDTO shoppingCartDTO
    ) {
        if(!shoppingCartService.isExistByCreatedByAndId(SecurityUtil.getCurrentUsername(), shoppingCartDTO.getId())) {
            return ResponseEntity.badRequest().body("Không tìm thấy sản phẩm trong giỏ hàng");
        }
        shoppingCartService.removeFromCart(shoppingCartDTO);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/cart/update")
    @ResponseBody
    public ResponseEntity<String> updateCart(
            @RequestBody ShoppingCartDTO shoppingCartDTO
    ) {
        if(!shoppingCartService.isExistByCreatedByAndId(SecurityUtil.getCurrentUsername(), shoppingCartDTO.getId())) {
            return ResponseEntity.badRequest().body("Không tìm thấy sản phẩm trong giỏ hàng");
        }
        shoppingCartService.updateCart(shoppingCartDTO);
        return ResponseEntity.ok("OK");
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/cart")
    public String cart(
            Model model
    ) {
        List<ShoppingCartDTO> carts = shoppingCartService.getAllCartByCreatedBy(SecurityUtil.getCurrentUsername());
        List<CustomerAddressDTO> addresses = customerAddressService.findAllByCreatedBy(SecurityUtil.getCurrentUsername());
        if(addresses.size() == 0) {
            User user = SecurityUtil.getUser();
            CustomerAddressDTO defaultAddress = new CustomerAddressDTO();
            defaultAddress.setCreatedBy(user.getUsername());
            defaultAddress.setDefault(true);
            defaultAddress.setFullName(user.getFirstName() + " " + user.getLastName());
            defaultAddress.setPhone(user.getPhone());
            defaultAddress.setAddress(user.getAddress());
            customerAddressService.save(defaultAddress);
            addresses = customerAddressService.findAllByCreatedBy(SecurityUtil.getCurrentUsername());
        }
        model.addAttribute("carts", carts);
        model.addAttribute("addresses", addresses);
        return "customer/cart";
    }

    @GetMapping("/account")
    public String account(
            Model model
    ) {
        return "customer/account";
    }


    @GetMapping("/coupon/{code}")
    @ResponseBody
    public ResponseEntity<CouponDTO> getCoupon(
            @PathVariable("code") String code
    ) {
        if (code == null || code.isBlank()) {
            return ResponseEntity.badRequest().body(null);
        }
        CouponDTO coupon = couponService.findByCode(code);

        if (coupon.isDeleted()) {
            return ResponseEntity.badRequest().body(null);
        }
        if (coupon.getMaxUsage() == 0) {
            return ResponseEntity.badRequest().body(null);
        }
        if (coupon.getStartDate() != null && coupon.getStartDate().isAfter(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(null);
        }
        if (coupon.getEndDate() != null && coupon.getEndDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(null);
        }
        if (coupon.getMaxUsagePerUser() > 0) {
            int count = userCouponService.countByCouponCodeAndCreatedBy(code, SecurityUtil.getCurrentUsername());
            if (count >= coupon.getMaxUsagePerUser()) {
                return ResponseEntity.badRequest().body(null);
            }
        }
        return ResponseEntity.ok(coupon);
    }


    @PostMapping("/order")
    @ResponseBody
    public ResponseEntity<?> order(@RequestBody OrderDTO orderDTO) {
        try {
            orderDTO.setCreatedBy(SecurityUtil.getCurrentUsername());
            Long orderId = orderService.order(orderDTO);

            if (orderId == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Không thể tạo đơn hàng, vui lòng thử lại sau"));
            }

            var order = orderService.findById(orderId);
            if (order == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Đơn hàng không tồn tại"));
            }

            String url;
            try {
                url = vnPayService.createPaymentUrl(order.getFinalPrice(), order.getId().toString());
            } catch (Exception e) {
                log.error( "Lỗi khi tạo URL thanh toán", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Lỗi khi tạo URL thanh toán"));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Đặt hàng thành công");
            response.put("orderId", orderId);
            response.put("url", url);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi, vui lòng thử lại sau"));
        }
    }


}
