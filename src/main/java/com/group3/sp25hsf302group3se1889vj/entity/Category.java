package com.group3.sp25hsf302group3se1889vj.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class Category extends BaseEntity {

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true, columnDefinition = "NVARCHAR(50)")
    private String name;

    @EqualsAndHashCode.Include
    @Column(columnDefinition = "NVARCHAR(1000)")
    private String description;

    @EqualsAndHashCode.Include
    @Column(columnDefinition = "NVARCHAR(255)")
    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private Set<Category> subCategories;

}
