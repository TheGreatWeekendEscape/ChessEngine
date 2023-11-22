package com.chess.business.services;

import com.chess.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    public String getToken(UserDetails user);
}
