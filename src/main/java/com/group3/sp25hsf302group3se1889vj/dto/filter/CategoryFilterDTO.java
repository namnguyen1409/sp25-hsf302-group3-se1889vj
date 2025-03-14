package com.group3.sp25hsf302group3se1889vj.dto.filter;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryFilterDTO extends BaseFilterDTO {
    private String name;
    private String description;
    private String parentName;
}
