package com.group3.sp25hsf302group3se1889vj.entity;

import com.group3.sp25hsf302group3se1889vj.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "customer_address_id")
    private CustomerAddress customerAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "NVARCHAR(20)")
    private OrderStatus status;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Column(name = "final_price")
    private BigDecimal finalPrice;

}
