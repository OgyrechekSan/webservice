package com.example.webservice.services;

import com.example.webservice.models.Rating;
import com.example.webservice.models.User;
import com.example.webservice.models.enums.RatingType;
import com.example.webservice.repositories.RatingRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RatingService {
    private final RatingRepository ratingRepository;

    public RatingStats rateUser(User user, User seller, RatingType type) {
        Optional<Rating> existingRating = ratingRepository.findByUserAndSeller(user, seller);

        if (existingRating.isPresent()) {
            Rating rating = existingRating.get();
            if (rating.getType() == type) {
                ratingRepository.delete(rating);
            } else {
                rating.setType(type);
                ratingRepository.save(rating);
            }
        } else {
            Rating newRating = new Rating();
            newRating.setUser(user);
            newRating.setSeller(seller);
            newRating.setType(type);
            ratingRepository.save(newRating);
        }

        return getRatingStats(seller.getId());
    }

    public RatingStats getRatingStats(Long sellerId) {
        long likes = ratingRepository.countLikesBySellerId(sellerId);
        long dislikes = ratingRepository.countDislikesBySellerId(sellerId);
        return new RatingStats(likes, dislikes);
    }

    @Data
    @AllArgsConstructor
    public static class RatingStats {
        private long likes;
        private long dislikes;
    }
}
