package com.example.webservice.repositories;

import com.example.webservice.models.Rating;
import com.example.webservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByUserAndSeller(User user, User seller);

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.seller.id = :sellerId AND r.type = 'LIKE'")
    long countLikesBySellerId(@Param("sellerId") Long sellerId);

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.seller.id = :sellerId AND r.type = 'DISLIKE'")
    long countDislikesBySellerId(@Param("sellerId") Long sellerId);
}
