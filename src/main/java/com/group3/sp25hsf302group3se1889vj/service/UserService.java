package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.group3.sp25hsf302group3se1889vj.dto.filter.UserFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService extends PagingService<UserDTO, UserFilterDTO>{
    boolean existsByEmail(String value);

    boolean existsByPhone(String value);

    boolean isUsernameExist(String username);

    void registerCustomer(RegisterCustomerDTO registerCustomerDTO) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;

    void verifyEmail(String token) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;

    UserDTO getUserById(Long id);

    void updateProfile(UpdateProfileDTO updateProfileDTO);

    boolean checkPassword(ChangePasswordDTO changePasswordDTO);

    void changePassword(ChangePasswordDTO changePasswordDTO);

    void inviteStaff(String email) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;

    void registerStaff(RegisterStaffDTO registerStaffDTO) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;

    void changePermissions(Long userId, List<Long> permissionIds);

    void lockUser(Long id, String reason);

    void unlockUser(Long id);

    void forgotPassword(String email) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;

    void resetPassword(ResetPasswordDTO resetPasswordDTO) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
}
