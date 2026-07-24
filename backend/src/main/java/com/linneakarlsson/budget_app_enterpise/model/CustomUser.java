package com.linneakarlsson.budget_app_enterpise.model;

import com.linneakarlsson.budget_app_enterpise.dto.CustomUserResponseDTO;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class CustomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

    public CustomUser() {}

    public CustomUser(String email, String password, String role, List<Transaction> transactions) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.transactions = transactions;
    }

    public Long getId() { return id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public CustomUserResponseDTO toDTO() {
        return new CustomUserResponseDTO(id, email, role);
    }
}

