package com.group3.sp25hsf302group3se1889vj.dto.filter;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerFilterDTO extends BaseFilterDTO {
    private String title;
    private String url;
    private String image;
}
