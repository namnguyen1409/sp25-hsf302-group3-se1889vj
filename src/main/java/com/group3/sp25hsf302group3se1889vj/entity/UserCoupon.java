package com.group3.sp25hsf302group3se1889vj.entity;

import jakarta.persistence.*;
import lombok.*;



@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_coupons")
public class UserCoupon extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
