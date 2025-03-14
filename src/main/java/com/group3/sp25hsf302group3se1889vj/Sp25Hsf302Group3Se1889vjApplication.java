package com.group3.sp25hsf302group3se1889vj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@EntityScan(basePackages = "com.group3.sp25hsf302group3se1889vj.entity")
public class Sp25Hsf302Group3Se1889vjApplication {

    public static void main(String[] args) {
        SpringApplication.run(Sp25Hsf302Group3Se1889vjApplication.class, args);
    }

}
