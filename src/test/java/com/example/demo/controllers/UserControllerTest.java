package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "passwordEncoder", passwordEncoder);
    }

    @Test
    public void findByIdNotFoundTest(){
        ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void findByIdFoundTest(){
        User user = fakeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        ResponseEntity<User> response = userController.findById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
    }

    @Test
    public void findByUsernameNotFoundTest(){
        ResponseEntity<User> response = userController.findByUserName("Username");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void findByUsernameFoundTest(){
        User user = fakeUser();
        when(userRepository.findByUsername("Username")).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName("Username");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
    }

    @Test
    public void createUserShortPasswordTest(){
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("Username");
        userRequest.setPassword("Pass");
        userRequest.setConfirmPassword("Pass");

        ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void createUserHappyPath() throws Exception{
        when(passwordEncoder.encode("Password")).thenReturn("HashedPassword");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("Username");
        userRequest.setPassword("Password");
        userRequest.setConfirmPassword("Password");

        ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals("HashedPassword", u.getPassword());
    }

    private Cart emptyCart(){
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(null);
        cart.setItems(new ArrayList<Item>());
        cart.setTotal(BigDecimal.valueOf(0.0));
        return cart;
    }

    private User fakeUser(){
        User user = new User();
        user.setId(1);
        user.setUsername("Username");
        user.setSalt("");
        user.setPassword("Password");
        user.setCart(emptyCart());

        return user;
    }
}
