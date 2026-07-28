package com.linneakarlsson.budget_app_enterpise.controller;

import com.linneakarlsson.budget_app_enterpise.dto.CustomUserPatchDTO;
import com.linneakarlsson.budget_app_enterpise.dto.CustomUserRequestDTO;
import com.linneakarlsson.budget_app_enterpise.dto.CustomUserResponseDTO;
import com.linneakarlsson.budget_app_enterpise.model.customUser.CustomUser;
import com.linneakarlsson.budget_app_enterpise.service.AuthenticationService;
import com.linneakarlsson.budget_app_enterpise.service.CustomUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class CustomUserController {

    private final CustomUserService customUserService;
    private final AuthenticationService authenticationService;

    public CustomUserController(CustomUserService customUserService, AuthenticationService authenticationService) {
        this.customUserService = customUserService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<CustomUserResponseDTO> createUser(@RequestBody @Valid CustomUserRequestDTO dto) {
        CustomUser savedUser = customUserService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser.toDTO());
    }

    @PatchMapping("/me")
    public ResponseEntity<CustomUserResponseDTO> updateUser(
            @RequestBody @Valid CustomUserPatchDTO dto,
            @RequestHeader String email,
            @RequestHeader String password) {
        CustomUser user = authenticationService.authenticateOrThrow(email, password);
        CustomUser updatedUser = customUserService.updateUser(dto, user.getId());
        return ResponseEntity.ok(updatedUser.toDTO());
    }

    // TODO - autentication with JWT - remove email and password from headers

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create")
    public ResponseEntity<CustomUserResponseDTO> createAdmin(
            @RequestBody @Valid CustomUserRequestDTO dto,
            @RequestHeader String email,
            @RequestHeader String password) {
        CustomUser user = authenticationService.authenticateOrThrow(email, password);
        CustomUser savedAdmin = customUserService.createAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmin.toDTO());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(
            @PathVariable UUID id,
            @RequestHeader String email,
            @RequestHeader String password) {
        CustomUser user = authenticationService.authenticateOrThrow(email, password);
        customUserService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User with id " + id + " deleted successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<CustomUserResponseDTO>> getAllUsers(
            @RequestHeader String email,
            @RequestHeader String password) {
        CustomUser user = authenticationService.authenticateOrThrow(email, password);
        List<CustomUserResponseDTO> users = customUserService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }
}

