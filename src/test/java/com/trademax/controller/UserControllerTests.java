package com.trademax.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trademax.model.User;
import com.trademax.model.Wallet;
import com.trademax.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @Test
    void testRegisterSuccess() throws Exception {
        User u = new User("1", "Joel", "j@a.com", "PAN", new Wallet(0), Collections.emptyList());
        when(userService.register(any())).thenReturn(u);

        mvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(u)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Joel"));
    }

    @Test
    void testGetSuccess() throws Exception {
        User u = new User("10", "Alex", "a@b.com", "PANX", new Wallet(100), Collections.emptyList());
        when(userService.get("10")).thenReturn(u);

        mvc.perform(get("/api/users/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("a@b.com"));
    }
}
