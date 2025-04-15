package com.example.webservice.repositories;

import com.example.webservice.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("SELECT i FROM Image i WHERE i.product.id = :productId")
    List<Image> findAllByProductId(@Param("productId") Long productId);
}