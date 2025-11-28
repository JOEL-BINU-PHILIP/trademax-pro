package com.trademax.controller;

import com.trademax.service.DummyStockApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StockController.class)
public class StockControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DummyStockApiService stockService;

    @Test
    void testGetAllStocks() throws Exception {
        DummyStockApiService.StockInfo s1 =
                new DummyStockApiService.StockInfo("TCS", "Tata", 5000);

        when(stockService.listStocks()).thenReturn(List.of(s1));

        mvc.perform(get("/api/stocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ticker").value("TCS"));
    }

    @Test
    void testGetStockByTicker() throws Exception {
        DummyStockApiService.StockInfo s =
                new DummyStockApiService.StockInfo("INFY", "Infosys", 1400);

        when(stockService.getStock("INFY")).thenReturn(s);

        mvc.perform(get("/api/stocks/INFY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.company").value("Infosys"));
    }
}
