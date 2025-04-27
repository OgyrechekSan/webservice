package com.example.webservice.controllers;

import com.example.webservice.models.Product;
import com.example.webservice.models.User;
import com.example.webservice.repositories.ProductRepository;
import com.example.webservice.repositories.UserRepository;
import com.example.webservice.services.ProductService;
import com.example.webservice.services.RatingService;
import com.example.webservice.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ProductRepository productRepository;
    private final RatingService ratingService;

    @GetMapping("/login")
    public String login(Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "login";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/registration")
    public String registration(Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(User user, Model model) {
        if (!userService.createUser(user)) {
            model.addAttribute("errorMessage",
                    "Пользователь с email: " + user.getEmail() + " уже существует");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/user/{id}")
    public String userInfo(@PathVariable Long id, Principal principal, Model model) {
        User user = userService.getUserById(id);
        User userByPrincipal = userService.getUserByPrincipal(principal);
        List<Product> products = productRepository.findByUser(user);
        RatingService.RatingStats ratingStats = ratingService.getRatingStats(id);

        model.addAttribute("user", user);
        model.addAttribute("userByPrincipal", userByPrincipal);
        model.addAttribute("products", products);
        model.addAttribute("ratingStats", ratingStats);
        model.addAttribute("sellerId", id);

        return "user-info";
    }


    @PostMapping("/user/delete/{id}")
    public String selfDeleteUser(
            @PathVariable Long id,
            Principal principal,
            HttpServletRequest request,
            HttpServletResponse response) {

        User currentUser = userService.getUserByPrincipal(principal);
        if (!currentUser.getId().equals(id)) {
            throw new AccessDeniedException("Вы можете удалить только свой аккаунт");
        }

        try {
            userService.deleteUser(id);
            // Явный выход и редирект
            new SecurityContextLogoutHandler().logout(request, response, null);
            return "redirect:/login?message=" +
                    URLEncoder.encode("Ваш аккаунт успешно удалён", StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "redirect:/profile?error=" + URLEncoder.encode("Ошибка при удалении аккаунта: "
                    + e.getMessage(), StandardCharsets.UTF_8);
        }
    }
}