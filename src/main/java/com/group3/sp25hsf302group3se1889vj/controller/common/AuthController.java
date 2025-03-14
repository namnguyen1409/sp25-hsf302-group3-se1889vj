package com.group3.sp25hsf302group3se1889vj.controller.common;

import com.group3.sp25hsf302group3se1889vj.dto.LoginDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Token;
import com.group3.sp25hsf302group3se1889vj.entity.User;
import com.group3.sp25hsf302group3se1889vj.enums.TokenType;
import com.group3.sp25hsf302group3se1889vj.repository.TokenRepository;
import com.group3.sp25hsf302group3se1889vj.repository.UserRepository;
import com.group3.sp25hsf302group3se1889vj.security.service.RecaptchaService;
import com.group3.sp25hsf302group3se1889vj.security.token.JwtTokenProvider;
import com.group3.sp25hsf302group3se1889vj.security.token.RefreshTokenProvider;
import com.group3.sp25hsf302group3se1889vj.security.userdetails.CustomUserDetails;
import com.group3.sp25hsf302group3se1889vj.service.StorageService;
import com.group3.sp25hsf302group3se1889vj.service.TokenService;
import com.group3.sp25hsf302group3se1889vj.service.UserService;
import com.group3.sp25hsf302group3se1889vj.util.CookieUtil;
import com.group3.sp25hsf302group3se1889vj.util.EncryptionUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
@Data
@Transactional
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final RecaptchaService recaptchaService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final EncryptionUtil encryptionUtil;
    private final CookieUtil cookieUtil;
    private final StorageService storageService;
    private final UserService userService;
    private final TokenRepository tokenRepository;

    @Value("${app.refresh.expiration}")
    private Long refreshTokenExpiration;

    @Value("${app.jwt.expiration}")
    private Long jwtExpiration;

    private User getUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetails.getUser();
    }

    @GetMapping("/login")
    public String login(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof CustomUserDetails) {
            return "redirect:/";
        }
        model.addAttribute("title", "Login");
        model.addAttribute("loginDTO", new LoginDTO());

        return "auth/login";
    }


    @PostMapping("/login")
    public String login(
            @ModelAttribute("loginDTO") @Validated LoginDTO loginDTO,
            BindingResult bindingResult,
            @RequestParam("g-recaptcha-response") String recaptchaResponse
    ) {
        if (recaptchaService.notVerifyRecaptcha(recaptchaResponse)) {
            bindingResult.rejectValue("recaptchaResponse", "error.recaptcha", "Vui lòng xác minh bạn không phải là robot");
            return "auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }
        Optional<User> userOptional = userRepository.findByUsername(loginDTO.getUsername());
        if (userOptional.isEmpty() || !passwordEncoder.matches(loginDTO.getPassword(), userOptional.get().getPassword())) {
            bindingResult.rejectValue("password", "error.login", "Mật khẩu không đúng");
            return "auth/login";
        }
        var user = userOptional.get();
        var jwt = jwtTokenProvider.generateToken(new CustomUserDetails(user));
        if (Boolean.TRUE.equals(loginDTO.getRemember())) {
            var refreshToken = refreshTokenProvider.generateRefreshToken(UUID.randomUUID().toString());
            Token tokenEntity = new Token();
            tokenEntity.setType(TokenType.REFRESH_TOKEN);
            tokenEntity.setToken(refreshTokenProvider.getKeyFromRefreshToken(refreshToken));
            tokenEntity.setEmail(user.getEmail());
            tokenRepository.save(tokenEntity);
            cookieUtil.addCookie("jwtToken", jwt, Integer.parseInt(jwtExpiration / 1000L + ""), "/", true, false);
            cookieUtil.addCookie("refreshToken", refreshToken, Integer.parseInt(refreshTokenExpiration / 1000L + ""), "/", true, false);
        } else {
            cookieUtil.addCookie("jwtToken", jwt, Integer.parseInt(jwtExpiration / 1000L + ""), "/", true, false);
        }

        return "redirect:admin/home";
    }



}
