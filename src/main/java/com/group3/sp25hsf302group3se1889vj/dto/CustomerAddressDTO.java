package com.group3.sp25hsf302group3se1889vj.dto;

import jakarta.persistence.Column;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAddressDTO extends BaseDTO {
    // TODO: Add fields here
    private String firstname;
    private String lastname;
    private String district;
    private String ward;
    private String address;
    private String phone;
}
