package com.linneakarlsson.budget_app_enterpise.service;

import com.linneakarlsson.budget_app_enterpise.dto.CustomUserPatchDTO;
import com.linneakarlsson.budget_app_enterpise.dto.CustomUserRequestDTO;
import com.linneakarlsson.budget_app_enterpise.dto.CustomUserResponseDTO;
import com.linneakarlsson.budget_app_enterpise.model.CustomUser;
import com.linneakarlsson.budget_app_enterpise.model.Role;
import com.linneakarlsson.budget_app_enterpise.repository.CustomUserRepository;
import com.linneakarlsson.budget_app_enterpise.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CustomUserService {

    private final CustomUserRepository customUserRepository;

    public CustomUserService(CustomUserRepository customUserRepository) {
        this.customUserRepository = customUserRepository;
    }

    public CustomUser createUser(CustomUserRequestDTO dto) {
        if (customUserRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email already exists");
        }
        CustomUser user = new CustomUser();
        user.setEmail(dto.email());
        user.setPassword(PasswordUtil.hashPassword(dto.password()));
        user.setRole(Role.USER);
        return customUserRepository.save(user);
    }

    public CustomUser createAdmin(CustomUserRequestDTO dto) {
        if (customUserRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email already exists");
        }
        CustomUser user = new CustomUser();
        user.setEmail(dto.email());
        user.setPassword(PasswordUtil.hashPassword(dto.password()));
        user.setRole(Role.ADMIN);
        return customUserRepository.save(user);
    }

    public CustomUser updateUser(CustomUserPatchDTO dto, Long userId) {
        CustomUser user = customUserRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        if (dto.email() != null) {
            if (customUserRepository.existsByEmail(dto.email())) {
                throw new IllegalArgumentException("Email already exists");
            }
            user.setEmail(dto.email());
        }
        if (dto.password() != null) {
            user.setPassword(PasswordUtil.hashPassword(dto.password()));
        }
        return customUserRepository.save(user);
    }

    public void deleteUser(Long userId) {
        if (!customUserRepository.existsById(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        customUserRepository.deleteById(userId);
    }

    public List<CustomUserResponseDTO> getAllUsers() {
        return customUserRepository.findAll().stream()
                .map(user -> new CustomUserResponseDTO(user.getId(), user.getEmail(), user.getRole()))
                .toList();
    }
}

