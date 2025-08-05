package com.app.ecom.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.ecom.model.CartItem;
import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class CartController
{
    private final CartService cartService;
    @PostMapping("/add")
    public ResponseEntity<String> addCart(@RequestHeader("X-User-ID") String userId,@RequestBody CartItemRequest cartItemRequest)
    {
        boolean isCreated = cartService.addToCart(userId, cartItemRequest);
        if(!isCreated)
            return ResponseEntity.badRequest().body("Product is Out of stock or not found or user not found..");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Void> removeCart(@RequestHeader("X-User-ID") String userId,@PathVariable Long productId)
    {
        boolean isDeleted = cartService.deleteItemFromCart(userId,productId);
        return isDeleted ? ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }
    @GetMapping("/getProduct")
    public ResponseEntity<List<CartItem>> getCart(@RequestHeader("X-User-ID") String userId)
    {
       return ResponseEntity.ok(cartService.getCartItems(userId));
    }
}
