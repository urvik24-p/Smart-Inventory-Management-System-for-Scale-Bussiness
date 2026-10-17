package com.inventory.smartinventory.repository;

import com.inventory.smartinventory.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for the users table.
 * findByUsername is used by Spring Security to load users during login.
 */
@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {

    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);
}
