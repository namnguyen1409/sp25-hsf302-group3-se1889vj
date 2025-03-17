package com.group3.sp25hsf302group3se1889vj.dto.filter;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProductSearchDTO extends BaseFilterDTO{
    private String query;
    private Long categoryId;
    private Long brandId;
    private Long priceFrom;
    private Long priceTo;
}
