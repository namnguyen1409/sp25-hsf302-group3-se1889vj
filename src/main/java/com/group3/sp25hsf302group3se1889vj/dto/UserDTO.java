package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.entity.Permission;
import com.group3.sp25hsf302group3se1889vj.enums.RoleType;
import com.group3.sp25hsf302group3se1889vj.util.FieldMetadata;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends BaseDTO {
    // TODO: Add fields here
    @FieldMetadata(title = "Tên đăng nhập")
    private String username;
    @FieldMetadata(title = "Mật khẩu")
    private String password;
    @FieldMetadata(title = "Họ")
    private String firstName;
    @FieldMetadata(title = "Tên")
    private String lastName;
    @FieldMetadata(title = "Email")
    private String email;
    @FieldMetadata(title = "Số điện thoại")
    private String phone;
    @FieldMetadata(title = "Giới tính")
    private Boolean gender;
    @FieldMetadata(title = "Ngày sinh", cssClass = "date")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthday;
    @FieldMetadata(title = "Địa chỉ")
    private String address;
    @FieldMetadata(title = "Ảnh đại diện" , cssClass = "image")
    private String avatar;
    @FieldMetadata(title = "Vai trò")
    private RoleType role;
    @FieldMetadata(title = "Quyền")
    private Set<Permission> permissions = new HashSet<>();
    private boolean isLocked = false;
    private String lockReason;
}
