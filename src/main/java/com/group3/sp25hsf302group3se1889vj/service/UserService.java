package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.RegisterCustomerDTO;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.group3.sp25hsf302group3se1889vj.dto.UpdateProfileDTO;
import com.group3.sp25hsf302group3se1889vj.dto.UserDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.UserFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    boolean existsByEmail(String value);

    boolean existsByPhone(String value);

    boolean isUsernameExist(String username);

    void registerCustomer(RegisterCustomerDTO registerCustomerDTO) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;

    void verifyEmail(String token) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
    Page<UserDTO> searchUsers(UserFilterDTO filter, Pageable pageable);

    UserDTO getUserById(Long id);

    void updateProfile(UpdateProfileDTO updateProfileDTO);
}
