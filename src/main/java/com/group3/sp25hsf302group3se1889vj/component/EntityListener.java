package com.group3.sp25hsf302group3se1889vj.component;

import com.group3.sp25hsf302group3se1889vj.entity.BaseEntity;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EntityListener {

    private final AuditorAware<String> auditorAware;

    @PreUpdate
    public void preUpdate(BaseEntity entity) {
        if (entity.isDeleted()) {
            entity.setDeletedBy(auditorAware.getCurrentAuditor().orElse("system"));
            entity.setDeletedAt(LocalDateTime.now());
        }
    }

}
