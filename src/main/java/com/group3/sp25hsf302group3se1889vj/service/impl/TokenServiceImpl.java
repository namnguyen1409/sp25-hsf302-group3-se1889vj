package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.TokenDTO;
import com.group3.sp25hsf302group3se1889vj.enums.TokenType;
import com.group3.sp25hsf302group3se1889vj.repository.TokenRepository;
import com.group3.sp25hsf302group3se1889vj.service.TokenService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final ModelMapper modelMapper;

    @Override
    public void deleteTokenByToken(String token) {
        tokenRepository.deleteByToken(token);
    }

    @Override
    public TokenDTO findByEmailAndType(String email, TokenType tokenType) {
        return modelMapper.map(tokenRepository.findByEmailAndType(email, tokenType), TokenDTO.class);
    }

    @Override
    public TokenDTO findByTokenAndType(String token, TokenType tokenType) {
        return modelMapper.map(tokenRepository.findByTokenAndType(token, tokenType), TokenDTO.class);
    }
}
