package com.example.webservice.controllers;


import com.example.webservice.models.Rating;
import com.example.webservice.models.User;
import com.example.webservice.models.enums.RatingType;
import com.example.webservice.services.RatingService;
import com.example.webservice.services.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RatingWebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final RatingService ratingService;
    private final UserService userService;

    @MessageMapping("/rate")
    public void handleRating(RatingMessage message) {
        try {
            log.info("Received rating: {}", message);
            User user = userService.getUserById(message.getUserId());
            User seller = userService.getUserById(message.getSellerId());

            if(user.getId().equals(seller.getId())) {
                log.warn("User cannot rate themselves");
                return;
            }

            RatingService.RatingStats stats = ratingService.rateUser(user, seller, message.getType());
            messagingTemplate.convertAndSend("/topic/ratings/" + message.getSellerId(), stats);
            log.info("Sent update to /topic/ratings/{}: {}", message.getSellerId(), stats);
        } catch (Exception e) {
            log.error("Rating error", e);
        }

    }

    @Data
    public static class RatingMessage {
        private Long userId;
        private Long sellerId;
        private RatingType type;
    }
}
