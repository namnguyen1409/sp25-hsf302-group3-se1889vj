package com.group3.sp25hsf302group3se1889vj.config;

import com.group3.sp25hsf302group3se1889vj.entity.Category;
import com.group3.sp25hsf302group3se1889vj.entity.Permission;
import com.group3.sp25hsf302group3se1889vj.entity.User;
import com.group3.sp25hsf302group3se1889vj.enums.PermissionType;
import com.group3.sp25hsf302group3se1889vj.enums.RoleType;
import com.group3.sp25hsf302group3se1889vj.repository.CategoryRepository;
import com.group3.sp25hsf302group3se1889vj.repository.PermissionRepository;
import com.group3.sp25hsf302group3se1889vj.repository.UserRepository;
import com.group3.sp25hsf302group3se1889vj.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;


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
        initOwner();
        initPermissions();
        initCategories();
    }

    private void initOwner() {
        if (!userService.isUsernameExist(ownerUsername)) {
            User owner = new User();
            owner.setUsername(ownerUsername);
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
            log.info("Username {} already exists", ownerUsername);
        }
    }

    private void initPermissions() {
        for (PermissionType type : PermissionType.values()) {
            permissionRepository.findByName(type)
                    .orElseGet(() -> permissionRepository.save(Permission.builder().name(type).build()));
        }

        var owner = userRepository.findByUsername(ownerUsername).orElseThrow();
        for (PermissionType type : PermissionType.values()) {
            var permission = permissionRepository.findByName(type).orElseThrow();
            owner.getPermissions().add(permission);
        }
        userRepository.save(owner);
    }

    private void initCategories() {
        if (categoryRepository.count() < 10) {
            // tạo danh mục gốc
            // thời trang trẻ em
            Category babyFashion = new Category();
            babyFashion.setName("Thời trang trẻ em");
            babyFashion.setDescription("Danh mục thời trang trẻ em");
            categoryRepository.save(babyFashion);

            // danh mục con
            // áo trẻ em
            Category babyShirt = new Category();
            babyShirt.setName("Áo trẻ em");
            babyShirt.setDescription("Danh mục áo trẻ em");
            babyShirt.setParent(babyFashion);
            categoryRepository.save(babyShirt);

            // quần trẻ em
            Category babyPants = new Category();
            babyPants.setName("Quần trẻ em");
            babyPants.setDescription("Danh mục quần trẻ em");
            babyPants.setParent(babyFashion);
            categoryRepository.save(babyPants);

            // giày trẻ em
            Category babyShoes = new Category();
            babyShoes.setName("Giày trẻ em");
            babyShoes.setDescription("Danh mục giày trẻ em");
            babyShoes.setParent(babyFashion);
            categoryRepository.save(babyShoes);

            // phụ kiện trẻ em
            Category babyAccessories = new Category();
            babyAccessories.setName("Phụ kiện trẻ em");
            babyAccessories.setDescription("Danh mục phụ kiện trẻ em");
            babyAccessories.setParent(babyFashion);
            categoryRepository.save(babyAccessories);

            // thời trang nam
            Category menFashion = new Category();
            menFashion.setName("Thời trang nam");
            menFashion.setDescription("Danh mục thời trang nam");
            categoryRepository.save(menFashion);

            // áo nam
            Category menShirt = new Category();
            menShirt.setName("Áo nam");
            menShirt.setDescription("Danh mục áo nam");
            menShirt.setParent(menFashion);
            categoryRepository.save(menShirt);

            // quần nam
            Category menPants = new Category();
            menPants.setName("Quần nam");
            menPants.setDescription("Danh mục quần nam");
            menPants.setParent(menFashion);
            categoryRepository.save(menPants);

            // giày nam
            Category menShoes = new Category();
            menShoes.setName("Giày nam");
            menShoes.setDescription("Danh mục giày nam");
            menShoes.setParent(menFashion);
            categoryRepository.save(menShoes);

            // phụ kiện nam
            Category menAccessories = new Category();
            menAccessories.setName("Phụ kiện nam");
            menAccessories.setDescription("Danh mục phụ kiện nam");
            menAccessories.setParent(menFashion);
            categoryRepository.save(menAccessories);

            // thời trang nữ
            Category womenFashion = new Category();
            womenFashion.setName("Thời trang nữ");
            womenFashion.setDescription("Danh mục thời trang nữ");
            categoryRepository.save(womenFashion);

            // áo nữ
            Category womenShirt = new Category();
            womenShirt.setName("Áo nữ");
            womenShirt.setDescription("Danh mục áo nữ");
            womenShirt.setParent(womenFashion);
            categoryRepository.save(womenShirt);

            // quần nữ
            Category womenPants = new Category();
            womenPants.setName("Quần nữ");
            womenPants.setDescription("Danh mục quần nữ");
            womenPants.setParent(womenFashion);
            categoryRepository.save(womenPants);

            // giày nữ
            Category womenShoes = new Category();
            womenShoes.setName("Giày nữ");
            womenShoes.setDescription("Danh mục giày nữ");
            womenShoes.setParent(womenFashion);
            categoryRepository.save(womenShoes);

            // phụ kiện nữ
            Category womenAccessories = new Category();
            womenAccessories.setName("Phụ kiện nữ");
            womenAccessories.setDescription("Danh mục phụ kiện nữ");
            womenAccessories.setParent(womenFashion);
            categoryRepository.save(womenAccessories);


            log.info("Categories initialized successfully!");
        } else {
            log.info("Categories already exist, skipping initialization.");
        }
    }


}
