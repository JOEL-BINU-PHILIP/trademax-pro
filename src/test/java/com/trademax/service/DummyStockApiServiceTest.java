package com.trademax.service;

import com.trademax.exception.StockNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DummyStockApiServiceTest {

    private DummyStockApiService service;

    @BeforeEach
    void setup() {
        service = new DummyStockApiService();
        service.init(); // load predefined stocks
    }

    @Test
    void getStock_shouldReturnAStock() {
        var s = service.getStock("TCS");

        assertNotNull(s);
        assertEquals("TCS", s.getTicker());
        assertTrue(s.getPrice() > 0);
    }

    @Test
    void getStock_shouldThrowIfNotFound() {
        assertThrows(StockNotFoundException.class,
                () -> service.getStock("INVALID"));
    }

    @Test
    void listStocks_shouldReturnAllStocks() {
        List<DummyStockApiService.StockInfo> list = service.listStocks();

        assertEquals(5, list.size());
        assertTrue(list.stream().anyMatch(s -> s.getTicker().equals("TCS")));
        assertTrue(list.stream().allMatch(s -> s.getPrice() > 0));
    }

    @Test
    void getStock_shouldGenerateDifferentPricesOverTime() {
        var s1 = service.getStock("TCS");
        var s2 = service.getStock("TCS");

        // Since price fluctuates randomly, at least sometimes they differ
        assertNotEquals(s1.getPrice(), s2.getPrice());
    }
}
