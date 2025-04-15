package com.example.webservice.repositories;

import com.example.webservice.models.User;
import com.example.webservice.models.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    Long id(Long id);
    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :userId")
    void deleteById(@Param("userId") Long userId);

    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r = :role")
    long countByRole(@Param("role") Role role);
}
