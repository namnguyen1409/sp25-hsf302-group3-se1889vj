package com.group3.sp25hsf302group3se1889vj.repository;

import com.group3.sp25hsf302group3se1889vj.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    boolean existsByCustomerAddressId(Long id);

    @Query("SELECT o.status, COUNT(o) FROM Order o GROUP BY o.status")
    List<Object[]> countOrdersByStatus();

    @Query("SELECT YEAR(o.createdAt), MONTH(o.createdAt), SUM(o.finalPrice) " +
            "FROM Order o " +
            "WHERE o.status = 'DELIVERED' " +
            "GROUP BY YEAR(o.createdAt), MONTH(o.createdAt) " +
            "ORDER BY YEAR(o.createdAt), MONTH(o.createdAt)")
    List<Object[]> totalRevenueByMonth();

    @Query("SELECT YEAR(o.createdAt), MONTH(o.createdAt), COUNT(o) FROM Order o GROUP BY YEAR(o.createdAt), MONTH(o.createdAt) ORDER BY YEAR(o.createdAt), MONTH(o.createdAt)")
    List<Object[]> countOrdersByMonth();

    @Query("SELECT YEAR(o.createdAt), MONTH(o.createdAt), DAY(o.createdAt), SUM(o.finalPrice) " +
            "FROM Order o " +
            "WHERE o.status = 'DELIVERED' " +
            "GROUP BY YEAR(o.createdAt), MONTH(o.createdAt), DAY(o.createdAt) " +
            "ORDER BY YEAR(o.createdAt), MONTH(o.createdAt), DAY(o.createdAt)")
    List<Object[]> totalRevenueByDay();

}
