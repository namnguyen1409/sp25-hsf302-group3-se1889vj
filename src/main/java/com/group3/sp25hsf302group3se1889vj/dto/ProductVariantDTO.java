package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.util.FieldMetadata;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantDTO extends BaseDTO {

    @FieldMetadata(title = "Product Id")
    private Long productId;

    @FieldMetadata(title = "Kích thước")
    @NotBlank(message = "Kích thước không được để trống")
    @Size(max = 50, message = "Kích thước không được quá 50 ký tự")
    private String size;

    @FieldMetadata(title = "Màu sắc")
    @NotBlank(message = "Màu sắc không được để trống")
    @Size(max = 50, message = "Màu sắc không được quá 50 ký tự")
    private String color;

    @FieldMetadata(title = "Tồn kho")
    @NotNull(message = "Tồn kho không được để trống")
    @Positive(message = "Tồn kho phải lớn hơn 0")
    private Integer quantity;

}
