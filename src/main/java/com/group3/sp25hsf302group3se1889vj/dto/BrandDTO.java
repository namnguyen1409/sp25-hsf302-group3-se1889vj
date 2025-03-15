package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.util.FieldMetadata;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandDTO extends BaseDTO {
    @NotBlank(message = "Tên thương hiệu không được để trống")
    @Size(max = 255, message = "Tên thương hiệu không được vượt quá 255 ký tự")
    @FieldMetadata(title = "Tên thương hiệu")
    private String name;

    @Size(max = 255, message = "Logo không được vượt quá 255 ký tự")
    @FieldMetadata(title = "Logo", cssClass = "image")
    private String logo;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    @FieldMetadata(title = "Mô tả", cssClass = "description")
    private String description;
}
