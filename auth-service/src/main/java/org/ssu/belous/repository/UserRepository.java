package org.ssu.belous.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ssu.belous.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}
