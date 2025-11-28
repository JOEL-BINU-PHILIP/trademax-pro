package com.trademax.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trademax.model.User;
import com.trademax.model.Wallet;
import com.trademax.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper mapper;

    //Succesful Register

    @Test
    void testRegisterSuccess() throws Exception {
        User u = new User(
                "1",
                "Joel",
                "j@a.com",
                "ABCDE1234F",                
                new Wallet(0),
                Collections.emptyList()
        );

        when(userService.register(any())).thenReturn(u);

        mvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(u)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Joel"))
                .andExpect(jsonPath("$.email").value("j@a.com"))
                .andExpect(jsonPath("$.pan").value("ABCDE1234F"));
    }

    //Get Succesful registration test

    @Test
    void testGetSuccess() throws Exception {
        User u = new User(
                "10",
                "Alex",
                "a@b.com",
                "PQRSX6789Z",               // VALID PAN
                new Wallet(100),
                Collections.emptyList()
        );

        when(userService.get("10")).thenReturn(u);

        mvc.perform(get("/api/users/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("a@b.com"))
                .andExpect(jsonPath("$.pan").value("PQRSX6789Z"));
    }

    // Invalid PAN Test

    @Test
    void testRegisterInvalidPan() throws Exception {

        User u = new User(
                null,
                "Joel",
                "j@a.com",
                "INVALIDPAN",               //INVALID PAN
                new Wallet(0),
                Collections.emptyList()
        );

        mvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(u)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid PAN format. Example: ABCDE1234F"));
    }

    // Invalid Email Test

    @Test
    void testRegisterInvalidEmail() throws Exception {

        User u = new User(
                null,
                "Joel",
                "invalid-email",            //INVALID EMAIL
                "ABCDE1234F",              // VALID PAN
                new Wallet(0),
                Collections.emptyList()
        );

        mvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(u)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid email format"));
    }

    // Duplicate Email Test

    @Test
    void testRegisterDuplicateEmail() throws Exception {

        User u = new User(
                null,
                "Joel",
                "j@a.com",                 // DUPLICATE EMAIL
                "ABCDE1234F",
                new Wallet(0),
                Collections.emptyList()
        );

        when(userService.register(any()))
                .thenThrow(new DuplicateKeyException("E11000 duplicate key error dup key: { email: \"j@a.com\" }"));

        mvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(u)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already exists"));
    }

    // Duplicate PAN Test
    @Test
    void testRegisterDuplicatePan() throws Exception {

        User u = new User(
                null,
                "Joel",
                "j@a.com",
                "ABCDE1234F",              // DUPLICATE PAN
                new Wallet(0),
                Collections.emptyList()
        );

        when(userService.register(any()))
                .thenThrow(new DuplicateKeyException("E11000 duplicate key error dup key: { pan: \"ABCDE1234F\" }"));

        mvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(u)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("PAN already exists"));
    }
}
