package com.example.roomscheduler.web;

import com.example.roomscheduler.model.User;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.NonNull;


@Getter
@ToString(of = "user")
public class AuthUser extends org.springframework.security.core.userdetails.User {

    private final User user;

    public AuthUser(@NonNull User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.user = user;
    }
}