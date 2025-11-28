package com.trademax.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trademax.dto.BuyRequest;
import com.trademax.dto.SellRequest;
import com.trademax.exception.InsufficientFundsException;
import com.trademax.exception.InvalidQuantityException;
import com.trademax.service.TradeService;

import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(TradeController.class)
class TradeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TradeService tradeService;

    // ------------------ BUY TESTS ------------------

    @Test
    void testBuyStockSuccess() throws Exception {
        BuyRequest req = new BuyRequest();
        req.setUserId("u1");
        req.setTicker("TCS");
        req.setQuantity(5);

        mockMvc.perform(post("/api/trade/buy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock bought successfully"));

        verify(tradeService).buy("u1", "TCS", 5);
    }

    @Test
    void testBuyStockInvalidQuantity() throws Exception {
        BuyRequest req = new BuyRequest();
        req.setUserId("u1");
        req.setTicker("TCS");
        req.setQuantity(0);

        doThrow(new InvalidQuantityException("Quantity must be > 0"))
                .when(tradeService).buy("u1", "TCS", 0);

        mockMvc.perform(post("/api/trade/buy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Quantity must be > 0"));
    }

    @Test
    void testBuyStockInsufficientFunds() throws Exception {
        BuyRequest req = new BuyRequest();
        req.setUserId("u1");
        req.setTicker("INFY");
        req.setQuantity(3);

        doThrow(new InsufficientFundsException("Need 3000, available 1000"))
                .when(tradeService).buy("u1", "INFY", 3);

        mockMvc.perform(post("/api/trade/buy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Need 3000, available 1000"));
    }

    // ------------------ SELL TESTS ------------------

    @Test
    void testSellStockSuccess() throws Exception {
        SellRequest req = new SellRequest();
        req.setUserId("u1");
        req.setTicker("TCS");
        req.setQuantity(2);

        mockMvc.perform(post("/api/trade/sell")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock sold successfully"));

        verify(tradeService).sell("u1", "TCS", 2);
    }

    @Test
    void testSellStockInvalidQuantity() throws Exception {
        SellRequest req = new SellRequest();
        req.setUserId("u1");
        req.setTicker("TCS");
        req.setQuantity(0);

        doThrow(new InvalidQuantityException("Quantity must be > 0"))
                .when(tradeService).sell("u1", "TCS", 0);

        mockMvc.perform(post("/api/trade/sell")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Quantity must be > 0"));
    }
}
