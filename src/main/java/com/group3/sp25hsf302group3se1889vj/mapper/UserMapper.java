package com.group3.sp25hsf302group3se1889vj.mapper;

import com.group3.sp25hsf302group3se1889vj.dto.UpdateProfileDTO;
import com.group3.sp25hsf302group3se1889vj.dto.UserDTO;
import com.group3.sp25hsf302group3se1889vj.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    ModelMapper modelMapper = new ModelMapper();

    public UserDTO mapToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public UpdateProfileDTO mapToUpdateProfileDTO(User user) {
        return modelMapper.map(user, UpdateProfileDTO.class);
    }

    public User mapToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
