package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.util.FieldMetadata;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO extends BaseDTO {
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 80, message = "Tên sản phẩm không được vượt quá 80 ký tự")
    @FieldMetadata(title = "Tên sản phẩm")
    private String name;
    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    @FieldMetadata(title = "Mô tả", cssClass = "description")
    private String description;
    @FieldMetadata(title = "Ảnh")
    private String thumbnail;
    @NotBlank(message = "Thương hiệu không được để trống")
    @FieldMetadata(title = "Thương hiệu")
    private String brandName;
    @NotBlank(message = "Danh mục không được để trống")
    @FieldMetadata(title = "Danh mục")
    private String categoryName;
    @FieldMetadata(title = "Giá gốc")
    private BigDecimal priceOrigin;
    @FieldMetadata(title = "Giá bán")
    private BigDecimal priceSale;
}
