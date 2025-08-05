package com.project.ecommerce.dto;

import com.project.ecommerce.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class SignupRequest {

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 8)
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Name is Required")
    private String name;

    @NotBlank(message = "Role is required")
    private Role role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
