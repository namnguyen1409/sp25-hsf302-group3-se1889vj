package com.group3.sp25hsf302group3se1889vj.util;

import com.group3.sp25hsf302group3se1889vj.dto.filter.BaseFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.PagingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import java.util.List;
import java.util.function.Supplier;

public class PaginationUtil {

    public static <T, F extends BaseFilterDTO> void setupPagination(Model model,
                                                                    F filterDTO,
                                                                    PagingService<T, F> pagingService,
                                                                    MetadataExtractor metadataExtractor,
                                                                    Supplier<F> filterSupplier,
                                                                    Class<T> dtoClass,
                                                                    List<String> fields

    ) {
        if (filterDTO == null) {
            filterDTO = filterSupplier.get();
        }
        if (filterDTO.getOrderBy() == null) {
            filterDTO.setOrderBy("id");
        }
        if (filterDTO.getDirection() == null) {
            filterDTO.setDirection("asc");
        }
        Sort sortDirection = "asc".equalsIgnoreCase(filterDTO.getDirection())
                ? Sort.by(filterDTO.getOrderBy()).ascending()
                : Sort.by(filterDTO.getOrderBy()).descending();
        model.addAttribute("fields", fields);
        model.addAttribute("fieldTitles", metadataExtractor.getFieldTitles(dtoClass, fields));
        model.addAttribute("fieldClasses", metadataExtractor.getFieldClasses(dtoClass, fields));

        Pageable pageable = PageRequest.of(filterDTO.getPage() - 1, filterDTO.getSize(), sortDirection);
        Page<T> pages = pagingService.findAll(filterDTO, pageable);

        model.addAttribute("pages", pages);
        model.addAttribute("filterDTO", filterDTO);

        int n1 = pages.getNumber() * pages.getSize() + 1;
        if (pages.getTotalElements() == 0) {
            n1 = 0;
        }
        int n2 = Math.min((pages.getNumber() + 1) * pages.getSize(), (int) pages.getTotalElements());

        model.addAttribute("n1", n1);
        model.addAttribute("n2", n2);
        model.addAttribute("total", pages.getTotalElements());
    }

}
