package com.chess.presentation;

import com.chess.business.implementations.AuthServiceImpl;
import com.chess.business.services.AuthService;
import com.chess.model.AuthDto;
import com.chess.model.AuthResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    public AuthServiceImpl authServiceImpl;

    //No se por que coño esto no funciona. error: "Bad credentials" (Comprobar UserRepository.java)
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthDto authDto) {
        return ResponseEntity.ok(authServiceImpl.login(authDto));
    }

    @PostMapping("register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody AuthDto authDto) {
        return ResponseEntity.ok(authServiceImpl.register(authDto));
    }
}
