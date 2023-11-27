package com.chess.business.implementations;

import com.chess.business.services.AuthService;
import com.chess.integration.repositories.UserRepository;
import com.chess.model.AuthDto;
import com.chess.model.AuthResponseDto;

import com.chess.model.Role;
import com.chess.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public JwtServiceImpl jwtServiceImpl;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDto login(AuthDto authDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getName(), authDto.getPassword()));
        UserDetails user = userRepository.findByName(authDto.getName()).orElseThrow();
        return new AuthResponseDto(jwtServiceImpl.getToken(user));
    }

    @Override
    public AuthResponseDto register(AuthDto authDto) {
        User user = new User();
        user.setName(authDto.getName());
        user.setPassword(passwordEncoder.encode(authDto.getPassword()));
        user.setRole(Role.USER); //TODO Informarme de como crear admins y para que quiero hacerlo

        userRepository.save(user);
        return new AuthResponseDto(jwtServiceImpl.getToken(user));
    }
}
