package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.util.FieldMetadata;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAddressDTO extends BaseDTO {
    private boolean isDefault;
    @NotBlank(message = "Họ và tên không được để trống")
    @FieldMetadata(title = "Họ và tên")
    private String fullName;
    @NotBlank(message = "Địa chỉ không được để trống")
    @FieldMetadata(title = "Địa chỉ")
    private String address;
    @NotBlank(message = "Số điện thoại không được để trống")
    @FieldMetadata(title = "Số điện thoại")
    private String phone;
}
