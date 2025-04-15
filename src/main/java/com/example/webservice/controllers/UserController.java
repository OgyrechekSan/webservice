package com.example.webservice.controllers;

import com.example.webservice.models.User;
import com.example.webservice.repositories.UserRepository;
import com.example.webservice.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

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

    @GetMapping("/user/{user}")
    public String userInfo(@PathVariable("user") User user, Model model, Principal principal) {
        model.addAttribute("user", user);
        model.addAttribute("userByPrincipal", userService.getUserByPrincipal(principal));
        model.addAttribute("products",user.getProducts());
        return "user-info";
    }

    @PostMapping("/user/delete/{id}")
    public String deleteUser(
            @PathVariable Long id,
            Principal principal,
            HttpServletRequest request) throws Exception {

        try {
            User currentUser = userService.getUserByPrincipal(principal);
            User userToDelete = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

            // Проверка прав
            if (!currentUser.isAdmin() && !currentUser.getId().equals(userToDelete.getId())) {
                return "redirect:/users?error=" +
                        URLEncoder.encode("Нет прав для удаления", StandardCharsets.UTF_8);
            }

            // Удаление пользователя
            userService.deleteUser(id);

            // Если пользователь удаляет сам себя - logout
            if (currentUser.getId().equals(id)) {
                new SecurityContextLogoutHandler().logout(request, null, null);
                return "redirect:/login?message=" +
                        URLEncoder.encode("Ваш аккаунт удалён", StandardCharsets.UTF_8);
            }

            return "redirect:/admin/users?success=" +
                    URLEncoder.encode("Пользователь удалён", StandardCharsets.UTF_8);

        } catch (IllegalStateException e) {
            return "redirect:/admin/users?error=" +
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "redirect:/admin/users?error=" +
                    URLEncoder.encode("Ошибка сервера", StandardCharsets.UTF_8);
        }
    }
}