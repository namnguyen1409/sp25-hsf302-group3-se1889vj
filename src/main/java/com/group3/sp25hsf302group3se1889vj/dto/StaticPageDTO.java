package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.util.FieldMetadata;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaticPageDTO extends BaseDTO {
    @FieldMetadata(title = "Tiêu đề")
    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(min = 1, max = 255, message = "Tiêu đề phải từ 1 đến 255 ký tự")
    private String title;

    @FieldMetadata(title = "Liên kết", cssClass = "link")
    @NotBlank(message = "Liên kết không được để trống")
    @Size(min = 1, max = 255, message = "Liên kết phải từ 1 đến 255 ký tự")
    private String slug;

    @FieldMetadata(title = "Nội dung", cssClass = "html")
    @NotBlank(message = "Nội dung không được để trống")
    @Size(min = 1, message = "Nội dung không được dưới 1 kí tự")
    private String content;

}
