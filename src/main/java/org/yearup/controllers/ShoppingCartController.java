package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController
{
    private ShoppingCartService shoppingCartService;
    private UserService userService;


    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ShoppingCart> getCart(Principal principal)
    {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        ShoppingCart currentCart = shoppingCartService.getByUserId(userId);
        return ResponseEntity.ok(currentCart);
    }

    @PostMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCart addToCart(@PathVariable int productId, Principal principal){
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        shoppingCartService.addToCart(userId, productId);

        return shoppingCartService.getByUserId(userId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCart deleteCart(Principal principal)
    {   String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        shoppingCartService.clearCart(userId);
        return new ShoppingCart();
    }

    @PutMapping("/products/{productId}")
     public ShoppingCart updateCartItem(@PathVariable int productId,
                                        @RequestBody ShoppingCartItem item,
                                        Principal principal) {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        return shoppingCartService.updateCartItem(userId, productId, item.getQuantity());
    }

}
