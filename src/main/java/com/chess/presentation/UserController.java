package com.chess.presentation;

import com.chess.business.implementations.UserServiceImpl;
import com.chess.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

//https://www.javaguides.net/2021/10/login-and-registration-rest-api-using-spring-boot-spring-security-hibernate-mysql-database.html
//Estoy siguiendo esta guia un poco para montarlo all

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImpl userServiceImpl;


    /**
     * @PostMapping("/signin")
     *     public ResponseEntity<String> authenticateUser(@RequestBody User loginDto){
     *         Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
     *                 loginDto.getUsernameOrEmail(), loginDto.getPassword()));
     *
     *         SecurityContextHolder.getContext().setAuthentication(authentication);
     *         return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
     *     }
     *
     *     @PostMapping("/signup")
     *     public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){
     *
     *         // add check for username exists in a DB
     *         if(userRepository.existsByUsername(signUpDto.getUsername())){
     *             return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
     *         }
     *
     *         // add check for email exists in DB
     *         if(userRepository.existsByEmail(signUpDto.getEmail())){
     *             return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
     *         }
     *
     *         // create user object
     *         User user = new User();
     *         user.setName(signUpDto.getName());
     *         user.setUsername(signUpDto.getUsername());
     *         user.setEmail(signUpDto.getEmail());
     *         user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
     *
     *         Role roles = roleRepository.findByName("ROLE_ADMIN").get();
     *         user.setRoles(Collections.singleton(roles));
     *
     *         userRepository.save(user);
     *
     *         return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
     *
     *     }
     */

}
