package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.TokenDTO;
import com.group3.sp25hsf302group3se1889vj.enums.TokenType;

public interface TokenService {
    void deleteTokenByToken(String token);

    TokenDTO findByEmailAndType(String email, TokenType tokenType);

    TokenDTO findByTokenAndType(String token, TokenType tokenType);
}
