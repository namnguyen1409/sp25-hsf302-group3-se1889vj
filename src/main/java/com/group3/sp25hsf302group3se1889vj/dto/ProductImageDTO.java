package com.group3.sp25hsf302group3se1889vj.dto;

import com.group3.sp25hsf302group3se1889vj.entity.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDTO extends BaseDTO {
    private int position;
    private String url;
    private String alt;
    private Long productId;
}
