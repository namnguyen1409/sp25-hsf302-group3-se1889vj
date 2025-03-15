package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.CustomerAddressDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.CustomerAddressFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.CustomerAddressService;
import com.group3.sp25hsf302group3se1889vj.util.MetadataExtractor;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("/customer-address")
@AllArgsConstructor
public class CustomerAddressController {
    private final CustomerAddressService customerAddressService;
    private final MetadataExtractor metadataExtractor;

    @GetMapping({"/list", "", "/"})
    public String list(
            Model model,
            @ModelAttribute(value = "filterDTO", binding = false) CustomerAddressFilterDTO filterDTO
    ) {
        if(filterDTO == null) {
            filterDTO = new CustomerAddressFilterDTO();
        }
        Sort sortDirection = "asc".equalsIgnoreCase(filterDTO.getDirection())
                ? Sort.by(filterDTO.getOrderBy()).ascending()
                : Sort.by(filterDTO.getOrderBy()).descending();

        List<String> fields = Arrays.asList("firstname", "lastname", "district", "ward", "updatedAt", "address", "phone");
        model.addAttribute("fields", fields);
        model.addAttribute("fieldTitles", metadataExtractor.getFieldTitles(CustomerAddressDTO.class, fields));
        model.addAttribute("fieldClasses", metadataExtractor.getFieldClasses(CustomerAddressDTO.class, fields));

        Pageable pageable = PageRequest.of(filterDTO.getPage() - 1, filterDTO.getSize(), sortDirection);

        Page<CustomerAddressDTO> pages = customerAddressService.searchCustomerAddresses(filterDTO, pageable);
        model.addAttribute("pages", pages);
        model.addAttribute("filterDTO", filterDTO);
        return "admin/category/list";
    }

    @PostMapping({"/list", "", "/"})
    public String list(
            @ModelAttribute(value = "filterDTO") CustomerAddressFilterDTO filterDTO,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("filterDTO", filterDTO);
        return "redirect:/admin/category/list";
    }
}
