package com.trademax.controller;

import com.trademax.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
public class WalletControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WalletService svc;

    @Test
    void testAddMoney() throws Exception {
        when(svc.addMoney("u1", 500)).thenReturn(1500.0);

        mvc.perform(post("/api/wallet/add?userId=u1&amount=500"))
                .andExpect(status().isOk())
                .andExpect(content().string("1500.0"));
    }

    @Test
    void testWithdrawMoney() throws Exception {
        when(svc.withdrawMoney("u5", 200)).thenReturn(300.0);

        mvc.perform(post("/api/wallet/withdraw?userId=u5&amount=200"))
                .andExpect(status().isOk())
                .andExpect(content().string("300.0"));
    }
}
