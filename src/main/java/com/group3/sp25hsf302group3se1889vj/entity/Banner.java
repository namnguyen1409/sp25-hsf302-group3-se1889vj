package com.group3.sp25hsf302group3se1889vj.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "banners")
public class Banner extends BaseEntity {

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String title;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String url;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String image;

    @Column(columnDefinition = "NVARCHAR(500)")
    private String description;

}
