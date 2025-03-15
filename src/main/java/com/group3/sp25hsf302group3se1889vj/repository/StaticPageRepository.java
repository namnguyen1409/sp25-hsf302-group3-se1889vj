package com.group3.sp25hsf302group3se1889vj.repository;

import com.group3.sp25hsf302group3se1889vj.entity.StaticPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaticPageRepository extends JpaRepository<StaticPage, Long>, JpaSpecificationExecutor<StaticPage> {
}
