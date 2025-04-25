package com.example.webservice.controllers;

import com.example.webservice.models.User;
import com.example.webservice.models.enums.RatingType;
import com.example.webservice.services.RatingService;
import com.example.webservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;
    private final UserService userService;

    @PostMapping("/rate/{sellerId}")
    public ResponseEntity<RatingService.RatingStats> rateSeller(
            @PathVariable Long sellerId,
            @RequestParam RatingType type,
            Principal principal) {

        User user = userService.getUserByPrincipal(principal);
        User seller = userService.getUserById(sellerId);

        if (user.getId().equals(sellerId)) {
            return ResponseEntity.badRequest().build();
        }

        RatingService.RatingStats stats = ratingService.rateUser(user, seller, type);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/{sellerId}")
    public ResponseEntity<RatingService.RatingStats> getStats(@PathVariable Long sellerId) {
        RatingService.RatingStats stats = ratingService.getRatingStats(sellerId);
        return ResponseEntity.ok(stats);
    }
}
