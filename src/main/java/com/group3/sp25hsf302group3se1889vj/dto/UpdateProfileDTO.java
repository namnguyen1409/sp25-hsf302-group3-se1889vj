package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.validation.annotation.PhoneUnique;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDTO {
    private Long id;

    @NotBlank(message = "Họ không được để trống")
    @Size(min = 1, max = 50, message = "Họ phải từ 1 đến 50 ký tự")
    @Pattern(regexp = "^[^!@#$%^&*()_+=\\[\\]{}|,;:'\"<>?/\\\\~`]*$", message = "Họ không được chứa ký tự đặc biệt")
    private String firstName;

    @NotBlank(message = "Tên không được để trống")
    @Size(min = 1, max = 50, message = "Tên phải từ 1 đến 50 ký tự")
    @Pattern(regexp = "^[^!@#$%^&*()_+=\\[\\]{}|,;:'\"<>?/\\\\~`]*$", message = "Tên không được chứa ký tự đặc biệt")
    private String lastName;

    @NotNull(message = "Giới tính không được để trống")
    private Boolean gender;

    @NotNull(message = "Ngày sinh không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 255, message = "Địa chỉ tối đa 255 ký tự")
    private String address;

    @NotBlank(message = "Ảnh đại diện không được để trống")
    @Size(max = 255, message = "Ảnh đại diện tối đa 255 ký tự")
    private String avatar;
}
