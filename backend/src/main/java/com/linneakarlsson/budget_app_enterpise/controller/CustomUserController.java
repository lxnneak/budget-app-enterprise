package com.linneakarlsson.budget_app_enterpise.controller;

import com.linneakarlsson.budget_app_enterpise.dto.CustomUserPatchDTO;
import com.linneakarlsson.budget_app_enterpise.dto.CustomUserRequestDTO;
import com.linneakarlsson.budget_app_enterpise.dto.CustomUserResponseDTO;
import com.linneakarlsson.budget_app_enterpise.model.customUser.CustomUser;
import com.linneakarlsson.budget_app_enterpise.model.customUser.CustomUserDetails;
import com.linneakarlsson.budget_app_enterpise.service.CustomUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class CustomUserController {

    private final CustomUserService customUserService;

    public CustomUserController(CustomUserService customUserService) {
        this.customUserService = customUserService;
    }

    @PatchMapping("/me")
    public ResponseEntity<CustomUserResponseDTO> updateUser(
            @Valid @RequestBody CustomUserPatchDTO dto,
            Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        CustomUser updatedUser = customUserService.updateUser(dto, userDetails.getCustomUser().getId());
        return ResponseEntity.ok(updatedUser.toDTO());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/create")
    public ResponseEntity<CustomUserResponseDTO> createAdmin(@Valid @RequestBody CustomUserRequestDTO dto) {

        CustomUser savedAdmin = customUserService.createAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmin.toDTO());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable UUID id) {

        customUserService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User with id " + id + " deleted successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<CustomUserResponseDTO>> getAllUsers() {

        List<CustomUserResponseDTO> users = customUserService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }
}

