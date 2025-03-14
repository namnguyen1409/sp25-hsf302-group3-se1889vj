package com.group3.sp25hsf302group3se1889vj.config;

import com.group3.sp25hsf302group3se1889vj.entity.User;
import com.group3.sp25hsf302group3se1889vj.enums.RoleType;
import com.group3.sp25hsf302group3se1889vj.repository.UserRepository;
import com.group3.sp25hsf302group3se1889vj.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    @Value("${owner.username}")
    private String ownerUsername;

    @Value("${owner.password}")
    private String ownerPassword;

    @Value("${owner.firstname}")
    private String ownerFirstname;

    @Value("${owner.lastname}")
    private String ownerLastname;

    @Value("${owner.email}")
    private String ownerEmail;

    @Value("${owner.phone}")
    private String ownerPhone;

    @Value("${owner.gender}")
    private Boolean ownerGender;

    @Value("${owner.address}")
    private String ownerAddress;

    @Value("${owner.birthday}")
    private String ownerBirthday;

    @Value("${owner.avatar}")
    private String ownerAvatar;


    private final UserService userService;

    @PostConstruct
    public void init() {
        if(!userService.isUsernameExist(ownerUsername)) {
            User owner = new User();
            owner.setUsername(ownerUsername);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            owner.setPassword(passwordEncoder.encode(ownerPassword));
            owner.setFirstName(ownerFirstname);
            owner.setLastName(ownerLastname);
            owner.setEmail(ownerEmail);
            owner.setPhone(ownerPhone);
            owner.setGender(ownerGender);
            owner.setAddress(ownerAddress);
            owner.setBirthday(LocalDate.parse(ownerBirthday));
            owner.setRole(RoleType.OWNER);
            owner.setAvatar(ownerAvatar);
            owner.setActive(true);
            userRepository.save(owner);
            log.info("Owner {} created", ownerUsername);
        } else {
            log.info("Username {} already exist", ownerUsername);
        }
    }

}
