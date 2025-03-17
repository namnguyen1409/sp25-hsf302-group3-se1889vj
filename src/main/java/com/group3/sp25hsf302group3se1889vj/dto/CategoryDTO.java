package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.util.FieldMetadata;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO extends BaseDTO {
    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(max = 50, message = "Tên danh mục không được vượt quá 50 ký tự")
    @FieldMetadata(title = "Tên danh mục")
    private String name;

    @FieldMetadata(title = "Ảnh")
    private String image;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    @FieldMetadata(title = "Mô tả", cssClass = "description")
    private String description;
    private Long parentId;
    @FieldMetadata(title = "Danh mục cha")
    private String parentName;
    private Set<CategoryDTO> subCategories;

}
