package com.example.springrolejwt.service;

import com.example.springrolejwt.model.Role;

public interface RoleService {
    Role findByName(String name);
}
