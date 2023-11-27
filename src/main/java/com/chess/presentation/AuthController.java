package com.chess.presentation;

import com.chess.business.implementations.AuthServiceImpl;
import com.chess.model.AuthDto;
import com.chess.model.AuthResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:4200"}) //CORS
public class AuthController {

    @Autowired
    public AuthServiceImpl authServiceImpl;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthDto authDto) {
        return ResponseEntity.ok(authServiceImpl.login(authDto));
    }

    @PostMapping("register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody AuthDto authDto) {
        return ResponseEntity.ok(authServiceImpl.register(authDto));
    }
}
