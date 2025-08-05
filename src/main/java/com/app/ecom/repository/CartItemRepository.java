package com.app.ecom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ecom.model.CartItem;
import com.app.ecom.model.Product;
import com.app.ecom.model.User;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long>
{
    Optional<CartItem> findByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

    List<CartItem> findByUser(User user);

    void deleteByUser(User user);
}
