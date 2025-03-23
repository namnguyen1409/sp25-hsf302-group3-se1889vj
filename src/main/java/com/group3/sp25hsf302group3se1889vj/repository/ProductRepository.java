package com.group3.sp25hsf302group3se1889vj.repository;

import com.group3.sp25hsf302group3se1889vj.dto.ProductDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    boolean existsByCategoryId(Long id);

    List<Product> findTop10ByIsActiveTrueOrderByCreatedAtDesc();

    Optional<Object> findByIdAndIsActiveTrue(Long id);

    @Modifying
    @Query(value = "UPDATE Product p SET p.quantity = p.quantity - ?2, p.updatedAt = CURRENT_TIMESTAMP WHERE p.id = ?1")
    void subtractQuantity(Long productId, int quantity);

    @Modifying
    @Query(value = "UPDATE Product p SET p.quantity = p.quantity + ?2, p.updatedAt = CURRENT_TIMESTAMP WHERE p.id = ?1")
    void addQuantity(Long productId, int quantity);

    boolean existsByBrandId(Long id);

    @Query("SELECT c.name, COUNT(p) FROM Product p JOIN p.category c GROUP BY c.id, c.name")
    List<Object[]> countProductsByCategory();

    @Query("SELECT b.name, COUNT(p) FROM Product p JOIN p.brand b GROUP BY b.name")
    List<Object[]> countProductsByBrand();

    @Query("SELECT p.isActive, COUNT(p) FROM Product p GROUP BY p.isActive")
    List<Object[]> countProductsByStatus();
}
