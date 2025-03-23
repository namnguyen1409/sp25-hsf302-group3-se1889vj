package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.*;
import com.group3.sp25hsf302group3se1889vj.entity.Token;
import com.group3.sp25hsf302group3se1889vj.entity.User;
import com.group3.sp25hsf302group3se1889vj.enums.RoleType;
import com.group3.sp25hsf302group3se1889vj.enums.TokenType;
import com.group3.sp25hsf302group3se1889vj.exception.Http400;
import com.group3.sp25hsf302group3se1889vj.exception.Http404;
import com.group3.sp25hsf302group3se1889vj.exception.Http500;
import com.group3.sp25hsf302group3se1889vj.repository.CustomerAddressRepository;
import com.group3.sp25hsf302group3se1889vj.repository.PermissionRepository;
import com.group3.sp25hsf302group3se1889vj.repository.TokenRepository;
import com.group3.sp25hsf302group3se1889vj.dto.filter.UserFilterDTO;
import com.group3.sp25hsf302group3se1889vj.mapper.UserMapper;
import com.group3.sp25hsf302group3se1889vj.repository.UserRepository;
import com.group3.sp25hsf302group3se1889vj.service.EmailService;
import com.group3.sp25hsf302group3se1889vj.service.UserService;
import com.group3.sp25hsf302group3se1889vj.util.EncryptionUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.group3.sp25hsf302group3se1889vj.specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EncryptionUtil encryptionUtil;
    private final EmailService emailService;
    

    private final CustomerAddressRepository customerAddressRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final UserMapper userMapper;
    private final PermissionRepository permissionRepository;

    @Override
    public boolean existsByEmail(String value) {
        return userRepository.existsByEmail(value);
    }

    @Override
    public boolean existsByPhone(String value) {
        return userRepository.existsByPhone(value);
    }

    @Override
    public boolean isUsernameExist(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public void registerCustomer(RegisterCustomerDTO registerCustomerDTO) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        User user = new User();
        user.setUsername(registerCustomerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerCustomerDTO.getPassword()));
        user.setFirstName(registerCustomerDTO.getFirstName());
        user.setLastName(registerCustomerDTO.getLastName());
        user.setEmail(registerCustomerDTO.getEmail());
        user.setPhone(registerCustomerDTO.getPhone());
        user.setGender(registerCustomerDTO.getGender());
        user.setBirthday(registerCustomerDTO.getBirthday());
        user.setAddress(registerCustomerDTO.getAddress());
        if (registerCustomerDTO.getGender()) {
            user.setAvatar("images/male.png");
        } else {
            user.setAvatar("images/female.png");
        }
        user.setRole(RoleType.CUSTOMER);
        user = userRepository.save(user);

        Token token = new Token();
        token.setEmail(registerCustomerDTO.getEmail());
        var uuid = UUID.randomUUID().toString();
        token.setToken(encryptionUtil.encrypt(uuid));
        token.setType(TokenType.VERIFY_EMAIL_TOKEN);

        tokenRepository.save(token);

        emailService.sendHTMLMail(
                registerCustomerDTO.getEmail(),
                "Kích hoạt tài khoản",
                """
                                    <!DOCTYPE html>
                                    <html>
                                    <head>
                                        <meta charset="UTF-8">
                                        <title>Kích hoạt tài khoản</title>
                                        <style>
                                            body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }
                                            .container { max-width: 600px; background-color: #ffffff; padding: 20px; border-radius: 8px; 
                                                        box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); text-align: center; margin: auto; }
                                            h2 { color: #007bff; }
                                            p { font-size: 16px; color: #333; }
                                            .button { display: inline-block; background-color: #007bff; color: #ffffff; padding: 12px 24px; 
                                                      text-decoration: none; border-radius: 5px; font-size: 16px; margin-top: 20px; }
                                            .footer { margin-top: 20px; font-size: 14px; color: #777; }
                                        </style>
                                    </head>
                                    <body>
                        
                        <div class="container">
                            <h2>Chào mừng bạn đến với OCOPS!</h2>
                            <p>Xin chào <strong>%s</strong>,</p>
                            <p>Cảm ơn bạn đã đăng ký tài khoản. Vui lòng nhấn vào nút bên dưới để kích hoạt tài khoản của bạn.</p>
                        
                            <a class="button" href="http://localhost:8080/verify-email?token=%s">Kích Hoạt Tài Khoản</a>
                        
                            <p>Nếu bạn không đăng ký tài khoản, vui lòng bỏ qua email này.</p>
                        
                            <div class="footer">
                                <p>Trân trọng,<br>Đội ngũ hỗ trợ OCOPS</p>
                            </div>
                        </div>
                        
                        </body>
                        </html>
                        """.formatted(registerCustomerDTO.getUsername(), uuid)
        );


    }

    @Transactional
    @Override
    public void verifyEmail(String token) {
        log.info("Verifying email with token: {}", token);

        String encryptedToken;
        try {
            encryptedToken = encryptionUtil.encrypt(token);
        } catch (Exception e) {
            log.warn("Token not found or invalid");
            throw new Http500("Lỗi mã hóa token");
        }


        // Tìm token trong DB
        Token verifyToken = tokenRepository.findByTokenAndType(encryptedToken, TokenType.VERIFY_EMAIL_TOKEN)
                .orElseThrow(() -> {
                    log.warn("Token not found or invalid");
                    return new Http404("Token không hợp lệ");
                });

        // Tìm user theo email
        User user = userRepository.findByEmail(verifyToken.getEmail())
                .orElseThrow(() -> {
                    log.warn("User not found for email: {}", verifyToken.getEmail());
                    return new Http404("Không tìm thấy người dùng");
                });

        user.setActive(true);
        userRepository.save(user);
        tokenRepository.delete(verifyToken);
        log.info("Email verified successfully for user: {}", user.getEmail());

    }

    public Page<UserDTO> findAll(UserFilterDTO filter, Pageable pageable) {
        Specification<User> specification = UserSpecification.filterUser(filter);
        return userRepository.findAll(specification, pageable).map(userMapper::mapToUserDTO);
    }

    @Override
    public UserDTO getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return userMapper.mapToUserDTO(user.get());
    }

    @Override
    public void updateProfile(UpdateProfileDTO updateProfileDTO) {
        User user = userRepository.findById(updateProfileDTO.getId())
                .orElseThrow(() -> new Http404("Không tìm thấy người dùng"));

        user.setFirstName(updateProfileDTO.getFirstName());
        user.setLastName(updateProfileDTO.getLastName());
        user.setGender(updateProfileDTO.getGender());
        user.setBirthday(updateProfileDTO.getBirthday());
        user.setAddress(updateProfileDTO.getAddress());
        user.setAvatar(updateProfileDTO.getAvatar());
        userRepository.save(user);
    }

    @Override
    public boolean checkPassword(ChangePasswordDTO changePasswordDTO) {
        User user = userRepository.findById(changePasswordDTO.getId())
                .orElseThrow(() -> new Http404("Không tìm thấy người dùng"));
        return passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword());
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        User user = userRepository.findById(changePasswordDTO.getId())
                .orElseThrow(() -> new Http404("Không tìm thấy người dùng"));
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void inviteStaff(String email) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // tìm token trong DB đã có chưa
        Token token = tokenRepository.findByEmailAndType(email, TokenType.INVITE_TOKEN);
        var uuid = UUID.randomUUID().toString();
        if (token == null) {
            token = new Token();
            token.setEmail(email);
            token.setType(TokenType.INVITE_TOKEN);
            token.setCreatedAt(LocalDateTime.now());
        } else if (token.getUpdatedAt().plusDays(1).isAfter(LocalDateTime.now())) {
            throw new Http400("Email vẫn còn hiệu lực");
        }
        token.setUpdatedAt(LocalDateTime.now());
        token.setToken(encryptionUtil.encrypt(uuid));
        tokenRepository.save(token);

        // gửi email
        emailService.sendHTMLMail(
                email,
                "Mời tham gia OCOPS",
                """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta charset="UTF-8">
                            <title>Mời tham gia OCOPS</title>
                            <style>
                                body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }
                                .container { max-width: 600px; background-color: #ffffff; padding: 20px; border-radius: 8px; 
                                            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); text-align: center; margin: auto; }
                                h2 { color: #007bff; }
                                p { font-size: 16px; color: #333; }
                                .button { display: inline-block; background-color: #007bff; color: #ffffff; padding: 12px 24px; 
                                          text-decoration: none; border-radius: 5px; font-size: 16px; margin-top: 20px; }
                                .footer { margin-top: 20px; font-size: 14px; color: #777; }
                            </style>
                        </head>
                        <body>
                        
                        <div class="container">
                            <h2>Mời tham gia OCOPS</h2>
                            <p>Bạn đã được mời tham gia hệ thống OCOPS. Vui lòng nhấn vào nút bên dưới để đăng ký tài khoản.</p>
                        
                            <a class="button" href="http://localhost:8080/register-staff?token=%s">Đăng Ký</a>
                        
                            <p>Nếu bạn không muốn tham gia, vui lòng bỏ qua email này.</p>
                        
                            <div class="footer">
                                <p>Trân trọng,<br>Đội ngũ hỗ trợ OCOPS</p>
                            </div>
                        </div>
                        
                        </body>
                        </html>
                        """.formatted(uuid)
        );
    }

    @Override
    public void registerStaff(RegisterStaffDTO registerStaffDTO) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        var token = tokenRepository.findByTokenAndType(encryptionUtil.encrypt(registerStaffDTO.getToken()), TokenType.INVITE_TOKEN).orElseThrow(() -> {
            log.info("Token not found or invalid");
            return new Http400("Token không hợp lệ");
        });
        if (userRepository.existsByEmail(token.getEmail())) {
            log.info("Email already exists");
            throw new Http400("Email đã tồn tại");
        }
        if (token.getUpdatedAt().plusDays(1).isBefore(LocalDateTime.now())) {
            log.info("Token expired");
            throw new Http400("Token đã hết hạn");
        }
        User user = new User();
        user.setUsername(registerStaffDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerStaffDTO.getPassword()));
        user.setFirstName(registerStaffDTO.getFirstName());
        user.setLastName(registerStaffDTO.getLastName());
        user.setEmail(token.getEmail());
        user.setPhone(registerStaffDTO.getPhone());
        user.setGender(registerStaffDTO.getGender());
        user.setBirthday(registerStaffDTO.getBirthday());
        user.setAddress(registerStaffDTO.getAddress());
        user.setRole(RoleType.STAFF);
        user.setActive(true);
        if (registerStaffDTO.getGender()) {
            user.setAvatar("images/male.png");
        } else {
            user.setAvatar("images/female.png");
        }
        userRepository.save(user);
        tokenRepository.delete(token);
    }

    @Override
    public void changePermissions(Long userId, List<Long> permissionIds) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new Http404("Không tìm thấy người dùng");
        }
        // xóa tất cả quyền cũ
        user.getPermissions().clear();
        // thêm quyền mới
        permissionIds.forEach(permissionId -> {
            var permission = permissionRepository.findById(permissionId).orElse(null);
            user.getPermissions().add(permission);
        });
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void lockUser(Long id, String reason) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new Http404("Không tìm thấy người dùng");
        }
        if (user.getRole() == RoleType.OWNER) {
            throw new Http400("Không thể khóa chủ cửa hàng");
        }
        user.setLocked(true);
        user.setLockReason(reason);
        userRepository.save(user);
    }

    @Override
    public void unlockUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new Http404("Không tìm thấy người dùng");
        }
        user.setLocked(false);
        user.setLockReason(null);
        userRepository.save(user);
    }

    @Override
    public void forgotPassword(String email) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Token token = tokenRepository.findByEmailAndType(email, TokenType.RESET_PASSWORD_TOKEN);
        var uuid = UUID.randomUUID().toString();
        if (token == null) {
            token = new Token();
            token.setEmail(email);
            token.setType(TokenType.RESET_PASSWORD_TOKEN);
            token.setCreatedAt(LocalDateTime.now());
        } else if (token.getUpdatedAt().plusDays(1).isAfter(LocalDateTime.now())) {
            throw new Http400("Email vẫn còn hiệu lực");
        }
        token.setUpdatedAt(LocalDateTime.now());
        token.setToken(encryptionUtil.encrypt(uuid));
        tokenRepository.save(token);


        // gửi email
        emailService.sendHTMLMail(
                email,
                "Đặt lại mật khẩu",
                """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta charset="UTF-8">
                            <title>Đặt lại mật khẩu</title>
                            <style>
                                body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }
                                .container { max-width: 600px; background-color: #ffffff; padding: 20px; border-radius: 8px; 
                                            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); text-align: center; margin: auto; }
                                h2 { color: #007bff; }
                                p { font-size: 16px; color: #333; }
                                .button { display: inline-block; background-color: #007bff; color: #ffffff; padding: 12px 24px; 
                                          text-decoration: none; border-radius: 5px; font-size: 16px; margin-top: 20px; }
                                .footer { margin-top: 20px; font-size: 14px; color: #777; }
                            </style>
                        </head>
                        <body>
                        
                        <div class="container">
                            <h2>
                                Đặt lại mật khẩu
                            </h2>
                            <p>
                                Bạn đã yêu cầu đặt lại mật khẩu. Vui lòng nhấn vào nút bên dưới để đặt lại mật khẩu của bạn.
                            </p>
                        
                            <a class="button" href="http://localhost:8080/reset-password?token=%s">Đăng Ký</a>
                        
                            <p>
                                Nếu bạn không muốn thay đổi mật khẩu, vui lòng bỏ qua email này.
                            </p>
                        
                            <div class="footer">
                                <p>Trân trọng,<br>Đội ngũ hỗ trợ OCOPS</p>
                            </div>
                        </div>
                        
                        </body>
                        </html>
                        """.formatted(uuid)
        );

    }


    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Token token = tokenRepository.findByTokenAndType(encryptionUtil.encrypt(resetPasswordDTO.getToken()), TokenType.RESET_PASSWORD_TOKEN).orElseThrow(() -> {
            return new Http400("Token không hợp lệ");
        });
        if (token.getUpdatedAt().plusDays(1).isBefore(LocalDateTime.now())) {
            throw new Http400("Token đã hết hạn");
        }
        User user = userRepository.findByEmail(token.getEmail()).orElse(null);
        if (user == null) {
            throw new Http404("Không tìm thấy người dùng");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
        userRepository.save(user);
        tokenRepository.delete(token);
    }





}
