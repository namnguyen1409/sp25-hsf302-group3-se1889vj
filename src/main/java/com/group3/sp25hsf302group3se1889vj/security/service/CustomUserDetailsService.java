package com.group3.sp25hsf302group3se1889vj.security.service;


import com.group3.sp25hsf302group3se1889vj.entity.Token;
import com.group3.sp25hsf302group3se1889vj.entity.User;
import com.group3.sp25hsf302group3se1889vj.enums.TokenType;
import com.group3.sp25hsf302group3se1889vj.repository.TokenRepository;
import com.group3.sp25hsf302group3se1889vj.repository.UserRepository;
import com.group3.sp25hsf302group3se1889vj.security.userdetails.CustomUserDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        User managedUser = entityManager.find(User.class, user.get().getId());
        return new CustomUserDetails(managedUser);
    }

    public UserDetails loadUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        User managedUser = entityManager.find(User.class, user.get().getId());
        return new CustomUserDetails(managedUser);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public CustomUserDetails loadUserByRefreshToken(String refreshToken) {
        Optional<Token> token = tokenRepository.findByTokenAndType(refreshToken, TokenType.REFRESH_TOKEN);
        if (token.isEmpty()) {
            throw new UsernameNotFoundException("Token not found");
        }
        Optional<User> user = userRepository.findByEmail(token.get().getEmail());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        User managedUser = entityManager.find(User.class, user.get().getId());
        return new CustomUserDetails(managedUser);
    }


}
