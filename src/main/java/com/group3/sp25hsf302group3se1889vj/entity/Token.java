package com.group3.sp25hsf302group3se1889vj.entity;

import com.group3.sp25hsf302group3se1889vj.enums.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tokens")
public class Token extends BaseEntity {

    @Column(name="email", nullable = true, columnDefinition = "nvarchar(100)")
    private String email;

    @Column(name = "token", nullable = false, columnDefinition = "nvarchar(255)")
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "nvarchar(50)")
    private TokenType type;

}
