package com.chess.business.services;

import com.chess.model.AuthDto;
import com.chess.model.AuthResponseDto;

public interface AuthService {
    public AuthResponseDto login(AuthDto authDto);
    public AuthResponseDto register(AuthDto authDto);
}
