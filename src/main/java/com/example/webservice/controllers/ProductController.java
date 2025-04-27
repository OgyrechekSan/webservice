package com.example.webservice.controllers;

import com.example.webservice.models.Product;
import com.example.webservice.models.User;
import com.example.webservice.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor//убираем конструктор
public class ProductController {//отвечает за приёмы http запросов
    private final ProductService productService;//final чтобы spring при создании бина сразу его инджектит

    @GetMapping("/")//при переходе на локальный хост будет вызываться данный метод
    public String products(@RequestParam(name = "searchWord", required = false) String title,
                           Principal principal,Model model){//для передачи параметров в шаблонизатор
        model.addAttribute("products", productService.listProducts(title));
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("searchWord", title);
        return "products";//возвращение представления products(в template валяется)
    }

    @GetMapping("/product/{id}")
    public String productInfo(@PathVariable Long id, Model model, Principal principal) {
        Product product = productService.getProductById(id);
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        model.addAttribute("authorProduct", product.getUser());
        return "product-info";
    }


    @PostMapping("/product/create")
    public String CreateProduct(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3, Product product, Principal principal)
            throws IOException {
        productService.saveProduct(principal,product, file1, file2, file3);
        return "redirect:/my/products";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Principal principal, Model model) {
        try {
            User user = productService.getUserByPrincipal(principal);
            productService.deleteProduct(user, id);
            return "redirect:/my/products?deleteSuccess=true";
        } catch (AccessDeniedException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/my/products?error=accessDenied";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при удалении товара");
            return "redirect:/my/products?error=deleteFailed";
        }
    }

    @GetMapping("/my/products")
    public String userProducts(Model model, Principal principal) {
        User user = productService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("products", user.getProducts());
        return "my-products";
    }
}