package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.CategoryDTO;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("/templates/admin/customer-address")
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

        List<String> fields = Arrays.asList("fullName", "address", "phone", "createdAt", "createdBy","updatedAt", "updatedBy");
        model.addAttribute("fields", fields);
        model.addAttribute("fieldTitles", metadataExtractor.getFieldTitles(CustomerAddressDTO.class, fields));
        model.addAttribute("fieldClasses", metadataExtractor.getFieldClasses(CustomerAddressDTO.class, fields));

        Pageable pageable = PageRequest.of(filterDTO.getPage() - 1, filterDTO.getSize(), sortDirection);

        Page<CustomerAddressDTO> pages = customerAddressService.searchCustomerAddresses(filterDTO, pageable);
        model.addAttribute("pages", pages);
        model.addAttribute("filterDTO", filterDTO);
        return "admin/customer-address/list";
    }

    @PostMapping({"/list", "", "/"})
    public String list(
            @ModelAttribute(value = "filterDTO") CustomerAddressFilterDTO filterDTO,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("filterDTO", filterDTO);
        return "redirect:/templates/admin/customer-address/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("customerAddressDTO", new CustomerAddressDTO());
        return "admin/customer-address/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("customerAddressDTO") @Validated CustomerAddressDTO customerAddressDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if(customerAddressService.existsCustomerAddressByPhone(customerAddressDTO.getPhone())) {
            bindingResult.rejectValue("phone", "customerAddressDTO.phone.exists", "Số điện thoại đã tồn tại");
        }
        if(bindingResult.hasErrors()) {
            return "admin/customer-address/add";
        }
        customerAddressService.save(customerAddressDTO);
        return "redirect:/templates/admin/customer-address/list";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model,
                       @PathVariable("id") Long id) {
        CustomerAddressDTO customerAddressDTO = customerAddressService.findById(id);
        model.addAttribute("customerAddressDTO", customerAddressDTO);
        return "admin/customer-address/edit";
    }

    @PostMapping("/edit")
    public String edit(
            @ModelAttribute("customerAddressDTO") @Validated CustomerAddressDTO customerAddressDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if(customerAddressService.existsCustomerAddressByPhoneAndIdNot(customerAddressDTO.getPhone(), customerAddressDTO.getId())) {
            bindingResult.rejectValue("phone", "customerAddressDTO.phone.exists", "Số điện thoại đã tồn tại");
        }
        if(bindingResult.hasErrors()) {
            return "admin/customer-address/edit";
        }
        CustomerAddressDTO customerAddress = customerAddressService.findById(customerAddressDTO.getId());
        customerAddress.setFullName(customerAddressDTO.getFullName());
        customerAddress.setAddress(customerAddressDTO.getAddress());
        customerAddress.setPhone(customerAddressDTO.getPhone());
        customerAddressService.save(customerAddress);
        return "redirect:/templates/admin/customer-address/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        CustomerAddressDTO customerAddressDTO = customerAddressService.findById(id);
        customerAddressDTO.setDeleted(true);
        customerAddressService.save(customerAddressDTO);
        return "redirect:/templates/admin/customer-address/list";
    }
}
