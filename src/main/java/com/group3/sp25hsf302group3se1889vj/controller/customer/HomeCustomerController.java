package com.group3.sp25hsf302group3se1889vj.controller.customer;

import com.group3.sp25hsf302group3se1889vj.dto.*;
import com.group3.sp25hsf302group3se1889vj.dto.filter.BannerFilterDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.CustomerProductSearchDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.OrderFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.User;
import com.group3.sp25hsf302group3se1889vj.enums.OrderStatus;
import com.group3.sp25hsf302group3se1889vj.service.*;
import com.group3.sp25hsf302group3se1889vj.util.FlashMessageUtil;
import com.group3.sp25hsf302group3se1889vj.util.MetadataExtractor;
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
import java.util.Arrays;
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
    private final MetadataExtractor metadataExtractor;
    private final CategoryService categoryService;
    private final BrandService brandService;

    @GetMapping({"/", ""})
    public String home(
            Model model
    ) {
        List<BannerDTO> banners = bannerService.findAll(new BannerFilterDTO(), PageRequest.of(0, 5)).getContent();
        List<CustomerProductDTO> newProducts = productService.getNewProducts(10);
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


    @GetMapping("/category/{id}")
    public String category(
            Model model,
            @PathVariable("id") Long id,
            @ModelAttribute(value = "filterDTO", binding = false) CustomerProductSearchDTO filterDTO
    ) {
        if (filterDTO == null) filterDTO = new CustomerProductSearchDTO();
        filterDTO.setCategoryId(id);
        String orderBy = filterDTO.getOrderBy() == null ? "createdAt" : filterDTO.getOrderBy();
        Sort sortDirection = "asc".equalsIgnoreCase(filterDTO.getDirection())
                ? Sort.by(orderBy).ascending()
                : Sort.by(orderBy).descending();

        Pageable pageable = PageRequest.of(filterDTO.getPage() - 1, filterDTO.getSize(), sortDirection);

        Page<CustomerProductDTO> products = productService.searchProducts(filterDTO, pageable);

        CategoryDTO category = categoryService.findById(id);
        model.addAttribute("category", category);
        model.addAttribute("products", products);
        model.addAttribute("filterDTO", filterDTO);
        return "customer/category";
    }

    @PostMapping("/category/{id}")
    public String category(
            @PathVariable("id") Long id,
            @ModelAttribute(value = "filterDTO") CustomerProductSearchDTO filterDTO,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("filterDTO", filterDTO);
        return "redirect:/category/" + id;
    }


    @GetMapping("/brand/{id}")
    public String brand(
            Model model,
            @PathVariable("id") Long id,
            @ModelAttribute(value = "filterDTO", binding = false) CustomerProductSearchDTO filterDTO
    ) {
        if (filterDTO == null) filterDTO = new CustomerProductSearchDTO();
        filterDTO.setBrandId(id);
        String orderBy = filterDTO.getOrderBy() == null ? "createdAt" : filterDTO.getOrderBy();
        Sort sortDirection = "asc".equalsIgnoreCase(filterDTO.getDirection())
                ? Sort.by(orderBy).ascending()
                : Sort.by(orderBy).descending();
        Pageable pageable = PageRequest.of(filterDTO.getPage() - 1, filterDTO.getSize(), sortDirection);

        Page<CustomerProductDTO> products = productService.searchProducts(filterDTO, pageable);

        BrandDTO brand = brandService.findById(id);

        model.addAttribute("brand", brand);
        model.addAttribute("products", products);
        model.addAttribute("filterDTO", filterDTO);
        return "customer/brand";
    }

    @PostMapping("/brand/{id}")
    public String brand(
            @PathVariable("id") Long id,
            @ModelAttribute(value = "filterDTO") CustomerProductSearchDTO filterDTO,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("filterDTO", filterDTO);
        return "redirect:/brand/" + id;
    }

    @GetMapping("/product/{id}")
    public String product(
            Model model,
            @PathVariable("id") Long id
    ) {
        if (!productService.isProductActive(id)) {
            return "redirect:/";
        }

        CustomerProductDTO product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "customer/product";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/cart/add")
    @ResponseBody
    public ResponseEntity<String> addToCart(
            @RequestBody ShoppingCartDTO shoppingCartDTO
    ) {
        log.info("shoppingCartDTO: {}", shoppingCartDTO);
        if (shoppingCartDTO.getQuantity() <= 0) {
            return ResponseEntity.badRequest().body("Số lượng phải lớn hơn 0");
        }
        shoppingCartDTO.setCreatedBy(SecurityUtil.getCurrentUsername());
        shoppingCartService.addToCart(shoppingCartDTO);
        return ResponseEntity.ok("OK");
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/cart/remove")
    @ResponseBody
    public ResponseEntity<String> removeFromCart(
            @RequestBody ShoppingCartDTO shoppingCartDTO
    ) {
        if (!shoppingCartService.isExistByCreatedByAndId(SecurityUtil.getCurrentUsername(), shoppingCartDTO.getId())) {
            return ResponseEntity.badRequest().body("Không tìm thấy sản phẩm trong giỏ hàng");
        }
        shoppingCartService.removeFromCart(shoppingCartDTO);
        return ResponseEntity.ok("OK");
    }


    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/cart/update")
    @ResponseBody
    public ResponseEntity<String> updateCart(
            @RequestBody ShoppingCartDTO shoppingCartDTO
    ) {
        if (!shoppingCartService.isExistByCreatedByAndId(SecurityUtil.getCurrentUsername(), shoppingCartDTO.getId())) {
            return ResponseEntity.badRequest().body("Không tìm thấy sản phẩm trong giỏ hàng");
        }
        if (shoppingCartDTO.getQuantity() <= 0) {
            return ResponseEntity.badRequest().body("Số lượng phải lớn hơn 0");
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
        if (addresses.isEmpty()) {
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


    @PreAuthorize("hasRole('CUSTOMER')")
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


    @PreAuthorize("hasRole('CUSTOMER')")
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
                log.error("Lỗi khi tạo URL thanh toán", e);
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

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/orders")
    public String list(
            Model model,
            @ModelAttribute(value = "filterDTO", binding = false) OrderFilterDTO filterDTO
    ) {
        if(filterDTO == null) {
            filterDTO = new OrderFilterDTO();
        }
        Sort sortDirection = "asc".equalsIgnoreCase(filterDTO.getDirection())
                ? Sort.by(filterDTO.getOrderBy()).ascending()
                : Sort.by(filterDTO.getOrderBy()).descending();

        List<String> fields = Arrays.asList("status", "totalPrice", "discountAmount", "finalPrice");
        model.addAttribute("fields", fields);
        model.addAttribute("fieldTitles", metadataExtractor.getFieldTitles(OrderDTO.class, fields));
        model.addAttribute("fieldClasses", metadataExtractor.getFieldClasses(OrderDTO.class, fields));

        Pageable pageable = PageRequest.of(filterDTO.getPage() - 1, filterDTO.getSize(), sortDirection);

        filterDTO.setCreatedBy(SecurityUtil.getCurrentUsername());
        Page<OrderDTO> pages = orderService.findAll(filterDTO, pageable);
        model.addAttribute("pages", pages);
        model.addAttribute("filterDTO", filterDTO);

        int n1 = pages.getNumber() * pages.getSize() + 1;
        int n2 = Math.min((pages.getNumber() + 1) * pages.getSize(), (int) pages.getTotalElements());

        model.addAttribute("n1", n1);
        model.addAttribute("n2", n2);
        model.addAttribute("total", pages.getTotalElements());
        return "customer/order/list";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/orders")
    public String list(
            @ModelAttribute(value = "filterDTO") OrderFilterDTO filterDTO,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("filterDTO", filterDTO);
        return "redirect:/orders";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/orders/view/{id}")
    public String detail(
            Model model,
            @PathVariable("id") Long id
    ) {
        OrderDTO order = orderService.findById(id);
        if (order == null || !order.getCreatedBy().equals(SecurityUtil.getCurrentUsername())) {
            return "redirect:/orders";
        }
        model.addAttribute("order", order);
        return "customer/order/view";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/orders/cancel/{id}")
    public String cancel(
            @PathVariable Long id,
            @RequestParam("reason") String reason
    ) {
        OrderDTO order = orderService.findById(id);
        if (order == null || !order.getCreatedBy().equals(SecurityUtil.getCurrentUsername())) {
            return "redirect:/orders";
        }
        orderService.cancelOrder(id, reason);
        return "redirect:/orders";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/orders/pay/{id}")
    public String pay(
            @PathVariable Long id
    ) {
        OrderDTO order = orderService.findById(id);
        if (order == null || !order.getCreatedBy().equals(SecurityUtil.getCurrentUsername())) {
            return "redirect:/orders";
        }
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            return "redirect:/orders";
        }
        String url;
        try {
            url = vnPayService.createPaymentUrl(order.getFinalPrice(), order.getId().toString());
        } catch (Exception e) {
            log.error("Lỗi khi tạo URL thanh toán", e);
            return "redirect:/orders";
        }
        return "redirect:" + url;
    }


    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/address")
    public String address(
            Model model
    ) {
        List<CustomerAddressDTO> addresses = customerAddressService.findAllByCreatedBy(SecurityUtil.getCurrentUsername());
        model.addAttribute("addresses", addresses);
        return "customer/address";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/address/add")
    public String addAddress(
            Model model
    ) {
        model.addAttribute("address", new CustomerAddressDTO());
        return "customer/address-add";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/address/add")
    public String addAddress(
            @ModelAttribute("address") CustomerAddressDTO address,
            RedirectAttributes redirectAttributes
    ) {
        address.setCreatedBy(SecurityUtil.getCurrentUsername());
        if (customerAddressService.isExistAddress(address)) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Địa chỉ đã tồn tại trong hệ thống", "error");
            return "redirect:/address";
        }
        customerAddressService.save(address);
        return "redirect:/address";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/address/edit/{id}")
    public String editAddress(
            Model model,
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes
    ) {
        CustomerAddressDTO address = customerAddressService.findById(id);
        if (address == null || !address.getCreatedBy().equals(SecurityUtil.getCurrentUsername())) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Không tìm thấy địa chỉ", "error");
            return "redirect:/address";
        }
        if (orderService.isExistByAddressId(id)) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Không thể chỉnh sửa địa chỉ đã được sử dụng trong đơn hàng", "error");
            return "redirect:/address";
        }
        model.addAttribute("address", address);
        return "customer/address-edit";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/address/edit")
    public String editAddress(
            @ModelAttribute("address") CustomerAddressDTO address,
            RedirectAttributes redirectAttributes
    ) {
        if (address.getId() == null) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Không tìm thấy địa chỉ", "error");
            return "redirect:/address";
        }
        CustomerAddressDTO oldAddress = customerAddressService.findById(address.getId());
        if (oldAddress == null || !oldAddress.getCreatedBy().equals(SecurityUtil.getCurrentUsername())) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Không tìm thấy địa chỉ", "error");
            return "redirect:/address";
        }
        if (orderService.isExistByAddressId(address.getId())) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Không thể chỉnh sửa địa chỉ đã được sử dụng trong đơn hàng", "error");
            return "redirect:/address";
        }
        address.setCreatedBy(SecurityUtil.getCurrentUsername());
        if (customerAddressService.isExistAddressAndIdNot(address, address.getId())) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Địa chỉ đã tồn tại trong hệ thống", "error");
            return "redirect:/address";
        }
        customerAddressService.update(address);
        return "redirect:/address";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/address/delete/{id}")
    public String deleteAddress(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes
    ) {
        CustomerAddressDTO address = customerAddressService.findById(id);
        if (address == null || !address.getCreatedBy().equals(SecurityUtil.getCurrentUsername())) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Không tìm thấy địa chỉ", "error");
            return "redirect:/address";
        }
        if (orderService.isExistByAddressId(id)) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Không thể xóa địa chỉ đã được sử dụng trong đơn hàng", "error");
            return "redirect:/address";
        }
        customerAddressService.delete(id);
        return "redirect:/address";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/address/set-default/{id}")
    public String setDefaultAddress(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes
    ) {
        CustomerAddressDTO address = customerAddressService.findById(id);
        if (address == null || !address.getCreatedBy().equals(SecurityUtil.getCurrentUsername())) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Không tìm thấy địa chỉ", "error");
            return "redirect:/address";
        }
        customerAddressService.setDefaultAddress(id, SecurityUtil.getCurrentUsername());
        return "redirect:/address";
    }

}
