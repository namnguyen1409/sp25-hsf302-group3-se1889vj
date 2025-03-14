package com.group3.sp25hsf302group3se1889vj.entity;

import com.group3.sp25hsf302group3se1889vj.component.EntityListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class, EntityListener.class})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SQLRestriction("is_deleted = false")
@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false, columnDefinition = "nvarchar(50)")
    private String createdBy; // lấy username của người tạo

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by", columnDefinition = "nvarchar(50)")
    private String updatedBy; // lấy username của người cập nhật

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by", columnDefinition = "nvarchar(50)")
    private String deletedBy; // lấy username của người xóa

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    public void softDelete() {
        this.isDeleted = true;
    }

}
