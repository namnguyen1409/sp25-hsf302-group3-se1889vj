package com.group3.sp25hsf302group3se1889vj.repository;


import com.group3.sp25hsf302group3se1889vj.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {
    int countByUserIdAndIsReadFalse(Long id);

    Optional<Notification> findByIdAndUserUsername(Long id, String currentUsername);

    void deleteAllByUserUsername(String currentUsername);

    List<Notification> findAllByUserUsername(String currentUsername);
}
