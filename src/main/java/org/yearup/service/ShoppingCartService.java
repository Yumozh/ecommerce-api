package org.yearup.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yearup.models.*;
import org.yearup.repository.ShoppingCartRepository;

import java.util.List;

@Service
public class ShoppingCartService
{
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId)
    {
        // load the user's cart rows, look up each product, and build the ShoppingCart
        ShoppingCart shoppingCart = new ShoppingCart();
        List<CartItem> cartItemList = shoppingCartRepository.findByUserId(userId);

        for(CartItem item : cartItemList){
            Product product = productService.getById(item.getProductId());
            ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
            shoppingCartItem.setProduct(product);
            shoppingCartItem.setQuantity(item.getQuantity());
            shoppingCart.add(shoppingCartItem);
        }
        return shoppingCart;
    }

//    public void addToCart{}
    public void addToCart(int userId, int productId){
        CartItem itemExist = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        if(itemExist == null){
            CartItem newItem = new CartItem();
            newItem.setUserId(userId);
            newItem.setProductId(productId);
            newItem.setQuantity(1);
            shoppingCartRepository.save(newItem);
        }
        else
        {
            itemExist.setQuantity(itemExist.getQuantity() + 1);
            shoppingCartRepository.save(itemExist);
        }
    }
    @Transactional
    public void clearCart(int userId){
        shoppingCartRepository.deleteByUserId(userId);
    }

    @Transactional
    public ShoppingCart updateCartItem(int userId, int productId, int quantity)
    {
        CartItem cartItem = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        if(cartItem != null){
            cartItem.setQuantity(quantity);
            shoppingCartRepository.save(cartItem);
        }
        return getByUserId(userId);
    }
}
