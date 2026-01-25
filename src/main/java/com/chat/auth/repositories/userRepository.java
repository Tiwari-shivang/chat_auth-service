package com.chat.auth.repositories;

import com.chat.auth.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface userRepository extends JpaRepository<Users, UUID> {
    Users findByEmail(String email);
}
