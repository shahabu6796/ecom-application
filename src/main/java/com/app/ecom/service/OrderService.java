package com.app.ecom.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.ecom.dto.OrderItemDTO;
import com.app.ecom.dto.OrderResponse;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Order;
import com.app.ecom.model.OrderItem;
import com.app.ecom.model.OrderStatus;
import com.app.ecom.model.User;
import com.app.ecom.repository.OrderRepository;
import com.app.ecom.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService
{
    private final CartService cartService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public Optional<OrderResponse> createOrder(String userId)
    {
        List<CartItem> cartItems = cartService.getCartItems(userId);
        if (cartItems.isEmpty())
        {
            return Optional.empty();
        }
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
        if (userOptional.isEmpty())
        {
            return Optional.empty();
        }
        User user = userOptional.get();
        BigDecimal totalPrice = cartItems.stream().map(CartItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalPrice);
        order.setStatus(OrderStatus.CONFIRMED);

        List<OrderItem> orderItems = cartItems.stream().map(item -> new OrderItem(null, item.getProduct(), item.getQuantity(), item.getPrice(), item.getOrder())).collect(Collectors.toList());
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        // validate for user
        // calculate total price
        // create order
        // create the cart
        cartService.clearCart(userId);
        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order savedOrder)
    {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(savedOrder.getId());
        orderResponse.setTotalAmount(savedOrder.getTotalAmount());
        orderResponse.setStatus(savedOrder.getStatus());
        orderResponse.setCreatedAt(savedOrder.getCreatedAt());

        orderResponse.setItems(savedOrder.getItems().stream().map(orderItem -> new OrderItemDTO(
                orderItem.getId(),
                                                                                                orderItem.getProduct().getId(),
                                                                                                orderItem.getQuantity(),
                                                                                                orderItem.getPrice(),
                                                                                                orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))).toList());

        return orderResponse;
    }

}
