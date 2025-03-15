package com.group3.sp25hsf302group3se1889vj.dto.filter;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAddressFilterDTO extends BaseFilterDTO {
    private String fullName;
    private String address;
    private String phone;
}
