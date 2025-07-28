package com.hrsys.hr_backend.service;

import com.hrsys.hr_backend.dao.UserDAO;
import com.hrsys.hr_backend.dto.JwtResponse;
import com.hrsys.hr_backend.dto.LoginRequest;
import com.hrsys.hr_backend.dto.RegisterRequest;
import com.hrsys.hr_backend.entity.User;
import com.hrsys.hr_backend.entity.Role;
import com.hrsys.hr_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String register(RegisterRequest registerRequest) {
        if (userDAO.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        if (userDAO.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        User user = new User(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword())
        );
        user.setRole(Role.USER);

        userDAO.save(user);
        return "User registered successfully!";
    }

    public JwtResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword()
                    )
            );

            User user = userDAO.findByUsernameOrEmail(loginRequest.getUsernameOrEmail())
                    .orElseThrow(() -> new RuntimeException("User not found!"));

            String token = jwtUtil.generateToken(user.getUsername());

            return new JwtResponse(
                    token,
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().name()
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials!");
        }
    }

    public String promoteUser(String username, Role role) {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        user.setRole(role);
        userDAO.save(user);

        return "User promoted to " + role.name() + " successfully!";
    }
}