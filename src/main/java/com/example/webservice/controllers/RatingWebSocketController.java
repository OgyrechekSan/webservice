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
public class RatingWebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final RatingService ratingService;
    private final UserService userService;

    @MessageMapping("/rate")
    public void handleRating(RatingMessage message) {
        User user = userService.getUserById(message.getUserId());
        User seller = userService.getUserById(message.getSellerId());

        RatingService.RatingStats stats = ratingService.rateUser(user, seller, message.getType());
        messagingTemplate.convertAndSend("/topic/ratings/" + message.getSellerId(), stats);
    }

    @Data
    public static class RatingMessage {
        private Long userId;
        private Long sellerId;
        private RatingType type;
    }
}
