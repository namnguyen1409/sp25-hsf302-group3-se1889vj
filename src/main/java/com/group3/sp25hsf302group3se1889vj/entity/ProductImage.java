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
@Table(name = "product_images")
public class ProductImage extends BaseEntity {

    private int position;

    private String url;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
