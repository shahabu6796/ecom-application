package com.app.ecom.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.ecom.model.CartItem;
import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.model.Product;
import com.app.ecom.model.User;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService
{
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public boolean addToCart(String userId, CartItemRequest cartItemRequest)
    {
        // validate product exits
        Optional<Product> product = productRepository.findById(cartItemRequest.getProductId());
        if (product.isEmpty())
        {
            return false;
        }
        // validate the requested quantity is available for this particular product
        if (product.get().getQuantity() < cartItemRequest.getQuantity())
        {
            return false;
        }

        // validate for the user by provided userId

        Optional<User> user = userRepository.findById(Long.valueOf(userId));
        if (user.isEmpty())
        {
            return false;
        }

        Optional<CartItem> exitingCartItem = cartItemRepository.findByUserAndProduct(user.get(), product.get());
        if (exitingCartItem.isPresent())
        {
            exitingCartItem.get().setQuantity(exitingCartItem.get().getQuantity() +  cartItemRequest.getQuantity());
            // Did this because price may vary for the same item, e.g sale is going on
            BigDecimal price = product.get().getPrice().multiply(BigDecimal.valueOf(cartItemRequest.getQuantity()));
            exitingCartItem.get().setPrice(exitingCartItem.get().getPrice().add(price));
            cartItemRepository.save(exitingCartItem.get());
        }
        else
        {
            CartItem cartItem = new CartItem();
           // cartItem.setOrder(user.get().get);
            cartItem.setProduct(product.get());
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setPrice(product.get().getPrice());
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean deleteItemFromCart(String userId, Long productId)
    {
        // validate product exits
        Optional<Product> productOptional = productRepository.findById(productId);
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
        if(productOptional.isPresent() && userOptional.isPresent())
        {
            cartItemRepository.deleteByUserAndProduct(userOptional.get(),productOptional.get());
            return true;
        }
        return false;
    }

    public List<CartItem> getCartItems(String userId)
    {
        return userRepository.findById(Long.valueOf(userId))
                .map(cartItemRepository::findByUser)
                .orElseGet(List::of);
    }

    public void clearCart(String userId)
    {
        userRepository.findById(Long.valueOf(userId)).ifPresent(
                    cartItemRepository::deleteByUser);
    }
}
