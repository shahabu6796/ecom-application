package com.app.ecom.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@SuppressWarnings("unused")
public class ProductController
{
    private final ProductService productService;
    @PostMapping("/addProduct")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest)
    {
        return new ResponseEntity<>(productService.createProduct(productRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest)
    {
        return productService.updateProduct(id,productRequest)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }
    @GetMapping("/findAll")
    public ResponseEntity<List<ProductResponse>> getAllProducts()
    {
        return ResponseEntity.ok(productService.getAllProducts());
    }
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword)
    {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id)
    {
        Boolean isDeleted = productService.deleteProduct(id);
        return isDeleted ? ResponseEntity.noContent().build():ResponseEntity.notFound().build();
    }
}
