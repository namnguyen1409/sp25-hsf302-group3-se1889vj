package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.util.FieldMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class BaseDTO {

    @FieldMetadata(title = "ID")
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @FieldMetadata(title = "Ngày tạo", cssClass = "dateTime")
    private LocalDateTime createdAt;
    @FieldMetadata(title = "Người tạo")
    private String createdBy;
    @FieldMetadata(title = "Ngày cập nhật", cssClass = "dateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    @FieldMetadata(title = "Người cập nhật")
    private String updatedBy;
    @FieldMetadata(title = "Ngày xóa", cssClass = "dateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deletedAt;
    @FieldMetadata(title = "Người xóa")
    private String deletedBy;
    private boolean isDeleted;
}
