package com.chess.business.services;

import com.chess.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> load(String name);
    User save(User user);
}
