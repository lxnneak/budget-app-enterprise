package com.linneakarlsson.budget_app_enterpise.security;

import com.linneakarlsson.budget_app_enterpise.dto.CustomUserLoginDTO;
import com.linneakarlsson.budget_app_enterpise.dto.CustomUserRequestDTO;
import com.linneakarlsson.budget_app_enterpise.dto.CustomUserResponseDTO;
import com.linneakarlsson.budget_app_enterpise.model.customUser.CustomUser;
import com.linneakarlsson.budget_app_enterpise.model.customUser.CustomUserDetails;
import com.linneakarlsson.budget_app_enterpise.service.CustomUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final CustomUserService customUserService;

    @Autowired
    public AuthController(JwtUtils jwtUtils, AuthenticationManager authenticationManager, CustomUserService customUserService) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.customUserService = customUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<CustomUserResponseDTO> createUser(@Valid @RequestBody CustomUserRequestDTO dto) {
        logger.info("Registering new user with email: {}", dto.email());
        CustomUser savedUser = customUserService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser.toDTO());
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @Valid @RequestBody CustomUserLoginDTO loginDTO,
            HttpServletResponse response
    ) {
        logger.debug("Attempting authentication for user: {}", loginDTO.email());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.email(),
                        loginDTO.password()
                )
        );

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtUtils.generateJwtToken(customUserDetails);

        Cookie cookie = new Cookie("authToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // TODO - true if prod
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        logger.info("Authentication successful for user: {}", loginDTO.email());

        return ResponseEntity.ok(Map.of(
                "username", loginDTO.email(),
                "authorities", customUserDetails.getAuthorities(),
                "token", token
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        Cookie cookie = new Cookie("authToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // TODO - true if prod
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message", "Logged out successfully!"));
    }
}