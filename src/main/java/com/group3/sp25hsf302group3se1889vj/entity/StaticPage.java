package com.group3.sp25hsf302group3se1889vj.entity;

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
@Table(name = "staticpages")
public class StaticPage extends BaseEntity {

    @Column(nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    private String title;

    @Column(nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    private String slug;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String content;

}
