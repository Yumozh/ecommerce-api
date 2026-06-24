package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.*;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final ShoppingCartService shoppingCartService;
    private final ProfileService profileService;

    public OrderService(OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository,
                        ShoppingCartService shoppingCartService, ProfileService profileService) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.shoppingCartService = shoppingCartService;
        this.profileService = profileService;
    }

    public Order checkout (int userId){
        ShoppingCart currentCart = shoppingCartService.getByUserId(userId);
        Profile profile = profileService.getProfileById(userId);

        Order order = new Order();
        order.setUserId(userId);
        order.setDate(LocalDate.now());
        order.setShippingAmount(BigDecimal.ZERO);
        order.setAddress(profile.getAddress());
        order.setCity(profile.getCity());
        order.setState(profile.getState());
        order.setZip(profile.getZip());
        Order savedOrder = orderRepository.save(order);

//        Profile existing = profileRepository.findById(userId).orElseThrow();
//        existing.setFirstName(profile.getFirstName());
//        existing.setLastName(profile.getLastName());
//        existing.setPhone(profile.getPhone());
//        existing.setEmail(profile.getEmail());
//        existing.setAddress(profile.getAddress());
//        existing.setCity(profile.getCity());
//        existing.setState(profile.getState());
//        existing.setZip(profile.getZip());
//        return profileRepository.save(existing);

        for (ShoppingCartItem cartItem : currentCart.getItems().values())
        {
            OrderLineItem lineItem = new OrderLineItem();
            lineItem.setOrderId(savedOrder.getOrderId());
            lineItem.setProductId(cartItem.getProductId());
            lineItem.setSalesPrice(BigDecimal.valueOf(cartItem.getProduct().getPrice()));
            lineItem.setQuantity(cartItem.getQuantity());
            lineItem.setDiscount(cartItem.getDiscountPercent());
            orderLineItemRepository.save(lineItem);
        }
        shoppingCartService.clearCart(userId);

        return savedOrder;
    }
}
