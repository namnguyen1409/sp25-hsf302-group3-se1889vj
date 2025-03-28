package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.util.FieldMetadata;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerDTO extends BaseDTO {
    @FieldMetadata(title = "Tiêu đề")
    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(min = 1, max = 255, message = "Tiêu đề phải từ 1 đến 255 ký tự")
    private String title;

    @FieldMetadata(title = "Liên kết", cssClass = "link")
    @NotBlank(message = "Liên kết không được để trống")
    @Size(min = 1, max = 255, message = "Liên kết phải từ 1 đến 255 ký tự")
    @URL(message = "Liên kết không hợp lệ")
    private String url;

    @FieldMetadata(title = "Hình ảnh", cssClass = "image")
    @NotBlank(message = "Hình ảnh không được để trống")
    private String image;

    @FieldMetadata(title = "Mô tả")
    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String description;
}
