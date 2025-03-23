package com.group3.sp25hsf302group3se1889vj.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_images")
public class ProductImage extends BaseEntity {

    @EqualsAndHashCode.Include
    private int position;

    @EqualsAndHashCode.Include
    private String url;

    @EqualsAndHashCode.Include
    @Column(nullable = true, columnDefinition = "NVARCHAR(50)")
    private String alt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

}
