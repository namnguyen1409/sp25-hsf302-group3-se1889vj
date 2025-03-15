package com.group3.sp25hsf302group3se1889vj.dto.filter;

import com.group3.sp25hsf302group3se1889vj.enums.RoleType;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFilterDTO extends BaseFilterDTO {
    // TODO: Add fields here
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Boolean gender;
    private LocalDate birthday;
    private String address;
    private String avatar;
    private RoleType role;
}
