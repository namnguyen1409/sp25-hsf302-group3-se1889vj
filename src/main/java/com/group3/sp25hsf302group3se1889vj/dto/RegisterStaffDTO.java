package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.validation.annotation.FieldsEqual;
import com.group3.sp25hsf302group3se1889vj.validation.annotation.PasswordStrength;
import com.group3.sp25hsf302group3se1889vj.validation.annotation.PhoneUnique;
import com.group3.sp25hsf302group3se1889vj.validation.annotation.UsernameUnique;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldsEqual(field1 = "password", field2 = "confirmPassword", message = "Mật khẩu không khớp")
public class RegisterStaffDTO {
    @UsernameUnique(message = "Tên đăng nhập đã tồn tại")
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 6, max = 50, message = "Tên đăng nhập phải từ 6 đến 50 ký tự")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @PasswordStrength(message = "Mật khẩu không đủ mạnh")
    private String password;
    private String confirmPassword;

    @NotBlank(message = "Họ không được để trống")
    @Size(min = 1, max = 50, message = "Họ phải từ 1 đến 50 ký tự")
    @Pattern(regexp = "^[^!@#$%^&*()_+=\\[\\]{}|,;:'\"<>?/\\\\~`]*$", message = "Họ không được chứa ký tự đặc biệt")
    private String firstName;

    @NotBlank(message = "Tên không được để trống")
    @Size(min = 1, max = 50, message = "Tên phải từ 1 đến 50 ký tự")
    @Pattern(regexp = "^[^!@#$%^&*()_+=\\[\\]{}|,;:'\"<>?/\\\\~`]*$", message = "Tên không được chứa ký tự đặc biệt")
    private String lastName;

    @Pattern(regexp = "^(\\+84|84|0)(3|5|7|8|9|1[2|6|8|9])[0-9]{8}$", message = "Số điện thoại không hợp lệ")
    @PhoneUnique(message = "Số điện thoại đã tồn tại")
    private String phone;

    @NotNull(message = "Giới tính không được để trống")
    private Boolean gender;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate birthday;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 255, message = "Địa chỉ tối đa 255 ký tự")
    private String address;

    private String recaptchaResponse;
    private String token;

}
