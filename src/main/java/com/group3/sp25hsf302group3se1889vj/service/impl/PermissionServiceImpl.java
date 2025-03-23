package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.PermissionDTO;
import com.group3.sp25hsf302group3se1889vj.repository.PermissionRepository;
import com.group3.sp25hsf302group3se1889vj.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<PermissionDTO> findAll() {
        return permissionRepository.findAll().stream().map((element) -> modelMapper.map(element, PermissionDTO.class)).collect(Collectors.toList());
    }
}
