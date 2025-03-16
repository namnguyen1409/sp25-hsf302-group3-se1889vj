package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.entity.Brand;
import com.group3.sp25hsf302group3se1889vj.entity.Category;
import com.group3.sp25hsf302group3se1889vj.util.FieldMetadata;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

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
    @FieldMetadata(title = "Mô tả", cssClass = "html")
    private String description;

    @FieldMetadata(title = "Ảnh", cssClass = "image")
    private String thumbnail;

    @NotNull(message = "Thương hiệu không được để trống")
    @FieldMetadata(title = "Thương hiệu")
    private Long brandId;

    @NotNull(message = "Danh mục không được để trống")
    @FieldMetadata(title = "Danh mục")
    private Long categoryId;

    @NotNull(message = "Giá gốc không được để trống")
    @Min(value = 0, message = "Giá gốc phải lớn hơn hoặc bằng 0")
    @FieldMetadata(title = "Giá gốc", cssClass = "currency")
    private BigDecimal priceOrigin;

    @NotNull(message = "Giá bán không được để trống")
    @Min(value = 0, message = "Giá bán phải lớn hơn hoặc bằng 0")
    @FieldMetadata(title = "Giá bán", cssClass = "currency")
    private BigDecimal priceSale;

    // Thêm các trường để hiển thị
    @FieldMetadata(title = "Thương hiệu")
    private String brandName;

    @FieldMetadata(title = "Danh mục")
    private String categoryName;

    @FieldMetadata(title = "Tồn kho")
    private Integer quantity;
}
