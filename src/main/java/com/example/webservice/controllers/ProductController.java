package com.example.webservice.controllers;

import com.example.webservice.models.Product;
import com.example.webservice.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor//убираем конструктор
public class ProductController {//отвечает за приёмы http запросов
    private final ProductService productService;//final чтобы spring при создании бина сразу его инджектит

    @GetMapping("/")//при переходе на локальный хост будет вызываться данный метод
    public String products(@RequestParam(name = "title", required = false) String title, Model model){//для передачи параметров в шаблонизатор
        model.addAttribute("products", productService.listProducts(title));
        return "products";//возвращение представления products(в template валяется)
    }

    @GetMapping("/product/{id}")  // Изменили на GET для просмотра информации
    public String productInfo(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/";
        }
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        return "product-info";
    }


    @PostMapping("/product/create")
    public String CreateProduct(@RequestParam("file1") MultipartFile file1,@RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3,Product product) throws IOException {
        productService.saveProduct(product, file1, file2, file3);
        return "redirect:/";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return "redirect:/";
    }
}
