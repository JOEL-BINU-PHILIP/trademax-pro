package com.trademax.controller;

import com.trademax.model.PortfolioItem;
import com.trademax.model.User;
import com.trademax.model.Wallet;
import com.trademax.service.DummyStockApiService;
import com.trademax.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PortfolioController.class)
public class PortfolioControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private DummyStockApiService stockService;

    @Test
    void testViewPortfolio() throws Exception {
        PortfolioItem item = new PortfolioItem("TCS", 2, 3000);
        User user = new User("u1", "Joel", "j@a.com", "PAN", new Wallet(1000), List.of(item));

        when(userService.get("u1")).thenReturn(user);
        when(stockService.getStock("TCS"))
                .thenReturn(new DummyStockApiService.StockInfo("TCS", "Tata", 3500));

        mvc.perform(get("/api/portfolio/u1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ticker").value("TCS"))
                .andExpect(jsonPath("$[0].currentValue").value(7000.0))
                .andExpect(jsonPath("$[0].plPercentRounded").value(16.67));
    }

    @Test
    void testPortfolioEmpty() throws Exception {
        User user = new User("u2", "A", "B", "PAN", new Wallet(0), List.of());
        when(userService.get("u2")).thenReturn(user);

        mvc.perform(get("/api/portfolio/u2"))
                .andExpect(status().isOk())
                .andExpect(content().string("No holdings"));
    }
}
