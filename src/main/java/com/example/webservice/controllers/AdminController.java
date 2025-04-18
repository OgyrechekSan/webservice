package com.example.webservice.controllers;

import com.example.webservice.models.User;
import com.example.webservice.models.enums.Role;
import com.example.webservice.repositories.UserRepository;
import com.example.webservice.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    private final UserService userService;

    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        model.addAttribute("users",userService.list());
        model.addAttribute("user",userService.getUserByPrincipal(principal));
        return "admin";
    }

    @PostMapping("/admin/user/ban/{id}")
    public String userBan(@PathVariable("id") Long id){
        userService.banUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user/edit/{user}")
    public String userEdit(@PathVariable ("user") User user, Model model, Principal principal) {
        model.addAttribute("targetUser", user);
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("roles", Role.values());
        return "user-edit";
    }

    @PostMapping("/admin/user/edit")
    public String userEdit(@RequestParam("userId") User user, @RequestParam Map<String, String> form) {
        userService.changeUserRoles(user, form);
        return "redirect:/admin";
    }

    @PostMapping("/admin/user/delete/{id}")
    public String adminDeleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Trying to delete user with id: {}", id);
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            log.error("Error deleting user", e);
            redirectAttributes.addFlashAttribute("error", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/admin";
    }
}
