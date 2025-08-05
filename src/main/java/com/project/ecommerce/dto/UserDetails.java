package com.project.ecommerce.dto;

public class UserDetails {
    int id;
    String role;

    public UserDetails(int id, String role) {
        this.id = id;
        this.role = role;
    }
    public int getId() {
        return id;
    }
    public String getRole() {
        return role;
    }
}
