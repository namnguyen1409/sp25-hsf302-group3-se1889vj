package com.group3.sp25hsf302group3se1889vj.dto.filter;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAddressFilterDTO extends BaseFilterDTO {
    private String firstname;
    private String lastname;
    private String district;
    private String ward;
    private String address;
    private String phone;
}
