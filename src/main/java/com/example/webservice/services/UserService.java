package com.example.webservice.services;

import com.example.webservice.models.Image;
import com.example.webservice.models.User;
import com.example.webservice.models.Product;
import com.example.webservice.models.enums.Role;
import com.example.webservice.repositories.ImageRepository;
import com.example.webservice.repositories.ProductRepository;
import com.example.webservice.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    public boolean createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return false;
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_ADMIN);

        log.info("Saving new User with email: {}", user.getEmail());
        userRepository.save(user);
        return true;
    }

    public List<User> list() {
        return userRepository.findAll();
    }

    public void banUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if (user.isActive()) {
                user.setActive(false);
                log.info("Ban user with id: {}; email: {}", user.getId(),user.getEmail());
            }else {
                user.setActive(true);
                log.info("Unban user with id: {}; email: {}", user.getId(),user.getEmail());
            }
        }
        userRepository.save(user);
    }


    public void changeUserRoles(User user, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) { return new User(); }
        return userRepository.findByEmail(principal.getName());
    }

    @Transactional
    public void deleteUser(Long userId) {
        // Load the user with all necessary relationships
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if this is the last admin
        if (user.isAdmin() && userRepository.countByRole(Role.ROLE_ADMIN) <= 1) {
            throw new IllegalStateException("Cannot delete the last admin");
        }

        // Explicitly load products to avoid LazyInitializationException
        List<Product> products = productRepository.findByUser(user);

        // Delete all products and their images
        for (Product product : products) {
            // Delete all images associated with the product
            imageRepository.deleteAll(product.getImages());
            // Delete the product
            productRepository.delete(product);
        }

        // Delete user's avatar if exists
        if (user.getAvatar() != null) {
            imageRepository.delete(user.getAvatar());
        }

        // Clear roles to avoid constraint violations
        user.getRoles().clear();
        userRepository.saveAndFlush(user); // Ensure changes are flushed

        // Finally delete the user
        userRepository.delete(user);
        userRepository.flush(); // Force immediate execution
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

}