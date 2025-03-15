package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.util.FieldMetadata;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

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
    private String url;

    @FieldMetadata(title = "Hình ảnh", cssClass = "image")
    @NotBlank(message = "Hình ảnh không được để trống")
    private String image;
}
