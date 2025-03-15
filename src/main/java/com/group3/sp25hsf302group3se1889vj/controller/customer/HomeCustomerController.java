package com.group3.sp25hsf302group3se1889vj.controller.customer;

import com.group3.sp25hsf302group3se1889vj.dto.BannerDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.BannerFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.BannerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class HomeCustomerController {

    private final BannerService bannerService;

    @GetMapping({"/", ""})
    public String home(
            Model model
    ) {
        List<BannerDTO> banners = bannerService.findAll(new BannerFilterDTO(), PageRequest.of(0, 5)).getContent();

        model.addAttribute("banners", banners);

        return "customer/home";
    }



}
