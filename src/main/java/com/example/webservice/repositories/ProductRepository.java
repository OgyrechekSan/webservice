package com.example.webservice.repositories;

import com.example.webservice.models.Product;
import com.example.webservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTitle(String title);
    List<Product> findByUser(User user);
}
