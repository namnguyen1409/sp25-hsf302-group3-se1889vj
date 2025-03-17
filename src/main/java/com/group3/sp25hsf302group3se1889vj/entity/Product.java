package com.group3.sp25hsf302group3se1889vj.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity {

    @EqualsAndHashCode.Include
    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String name;

    @EqualsAndHashCode.Include
    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @EqualsAndHashCode.Include
    @Column(nullable = false, columnDefinition = "BIT")
    private boolean isActive = true;

    @EqualsAndHashCode.Include
    private String thumbnail;

    @ManyToOne
    private Brand brand;

    @ManyToOne
    private Category category;

    private BigDecimal priceOrigin;

    private BigDecimal priceSale;

    private int quantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProductImage> images = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProductVariant> variants = new HashSet<>();


}
