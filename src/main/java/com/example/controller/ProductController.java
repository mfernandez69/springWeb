package com.example.controller;

import com.example.models.Product;
import com.example.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    /* La url es por GET : http://localhost:8080/products  */
    @GetMapping
    public String findAll(Model model) {
        List<Product> products = this.repository.findAll();
        model.addAttribute("products", products);
        return "product-list";
    }
    /*
    GET http://localhost:8080/products/new
     */
    @GetMapping("/new")
    public String getForm(Model model){
        model.addAttribute("product", new Product());
        return "product-form";
    }

    /*
    POST http://localhost:8080/products
     */
    @PostMapping("/new")
    public String save(@ModelAttribute("product") Product product){
        this.repository.save(product);
        return "redirect:/products";
    }
    @GetMapping("/{id}/view")
    public String verProducto(@PathVariable Long id, Model model) {
        Product product = this.repository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("product", product);
        return "product-view";
    }
    @GetMapping("/{id}/edit")
    public String editarProducto(@PathVariable Long id, Model model) {
        Product product = this.repository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("product", product);
        return "product-edit";
    }
    @PostMapping("/{id}/edit")
    public String saveFromEdit(@ModelAttribute("product") Product product){
        this.repository.save(product);
        return "redirect:/products";
    }
}
