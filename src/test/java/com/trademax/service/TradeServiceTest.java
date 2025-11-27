package com.trademax.service;

import com.trademax.exception.*;
import com.trademax.model.PortfolioItem;
import com.trademax.model.User;
import com.trademax.model.Wallet;
import com.trademax.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TradeServiceTest {

    @Mock
    private DummyStockApiService stockApi;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TradeService tradeService;

    private User user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User(
                "u1",
                "Joel",
                "x@test.com",
                "PAN",
                new Wallet(10000.0),
                new ArrayList<>()
        );
    }

    // ---------- BUY TESTS ----------

    @Test
    void buy_shouldAddNewStockIfNotPresent() {
        DummyStockApiService.StockInfo stock = new DummyStockApiService.StockInfo("TCS", "TCS", 100.0);

        when(userRepository.findById("u1")).thenReturn(Optional.of(user));
        when(stockApi.getStock("TCS")).thenReturn(stock);

        tradeService.buy("u1", "TCS", 10);

        assertEquals(1, user.getPortfolio().size());
        PortfolioItem item = user.getPortfolio().get(0);

        assertEquals("TCS", item.getTicker());
        assertEquals(10, item.getQuantity());
        assertEquals(100.0, item.getAvgBuyPrice());

        assertEquals(10000 - 1000, user.getWallet().getBalance());
        verify(userRepository).save(any());
    }

    @Test
    void buy_shouldMergeIfStockExists() {
        user.getPortfolio().add(new PortfolioItem("TCS", 10, 100.0));

        DummyStockApiService.StockInfo stock = new DummyStockApiService.StockInfo("TCS", "TCS", 200.0);

        when(userRepository.findById("u1")).thenReturn(Optional.of(user));
        when(stockApi.getStock("TCS")).thenReturn(stock);

        tradeService.buy("u1", "TCS", 10);

        PortfolioItem item = user.getPortfolio().get(0);

        assertEquals(20, item.getQuantity());
        assertEquals(150.0, item.getAvgBuyPrice()); // recalculated avg

        verify(userRepository).save(any());
    }

    @Test
    void buy_shouldFailIfInvalidQty() {
        assertThrows(InvalidQuantityException.class,
                () -> tradeService.buy("u1", "TCS", 0));
    }

    @Test
    void buy_shouldFailIfUserNotFound() {
        when(userRepository.findById("bad")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> tradeService.buy("bad", "TCS", 5));
    }

    @Test
    void buy_shouldFailIfInsufficientBalance() {
        user.getWallet().setBalance(50);
        DummyStockApiService.StockInfo stock = new DummyStockApiService.StockInfo("TCS", "TCS", 100);

        when(userRepository.findById("u1")).thenReturn(Optional.of(user));
        when(stockApi.getStock("TCS")).thenReturn(stock);

        assertThrows(InsufficientFundsException.class,
                () -> tradeService.buy("u1", "TCS", 1));
    }

    // ---------- SELL TESTS ----------

    @Test
    void sell_shouldRemoveStockCompletelyIfExactQty() {
        user.getPortfolio().add(new PortfolioItem("TCS", 5, 100));

        DummyStockApiService.StockInfo stock = new DummyStockApiService.StockInfo("TCS", "TCS", 200);

        when(userRepository.findById("u1")).thenReturn(Optional.of(user));
        when(stockApi.getStock("TCS")).thenReturn(stock);

        tradeService.sell("u1", "TCS", 5);

        assertTrue(user.getPortfolio().isEmpty());
        assertEquals(10000 + (200 * 5), user.getWallet().getBalance());
    }

    @Test
    void sell_shouldReduceQtyIfPartial() {
        user.getPortfolio().add(new PortfolioItem("TCS", 10, 100));

        DummyStockApiService.StockInfo stock = new DummyStockApiService.StockInfo("TCS", "TCS", 200);

        when(userRepository.findById("u1")).thenReturn(Optional.of(user));
        when(stockApi.getStock("TCS")).thenReturn(stock);

        tradeService.sell("u1", "TCS", 3);

        PortfolioItem item = user.getPortfolio().get(0);

        assertEquals(7, item.getQuantity());
        assertEquals(10000 + 600, user.getWallet().getBalance());
    }

    @Test
    void sell_shouldFailIfInvalidQty() {
        assertThrows(InvalidQuantityException.class,
                () -> tradeService.sell("u1", "TCS", 0));
    }

    @Test
    void sell_shouldFailIfUserNotFound() {
        when(userRepository.findById("bad")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> tradeService.sell("bad", "TCS", 2));
    }

    @Test
    void sell_shouldFailIfPortfolioEmpty() {
        when(userRepository.findById("u1")).thenReturn(Optional.of(user));

        assertThrows(InsufficientHoldingsException.class,
                () -> tradeService.sell("u1", "TCS", 1));
    }

    @Test
    void sell_shouldFailIfTickerMissingInPortfolio() {
        user.getPortfolio().add(new PortfolioItem("INFY", 10, 100));

        when(userRepository.findById("u1")).thenReturn(Optional.of(user));

        assertThrows(InsufficientHoldingsException.class,
                () -> tradeService.sell("u1", "TCS", 1));
    }

    @Test
    void sell_shouldFailIfQuantityTooHigh() {
        user.getPortfolio().add(new PortfolioItem("TCS", 2, 100));

        when(userRepository.findById("u1")).thenReturn(Optional.of(user));

        assertThrows(InsufficientHoldingsException.class,
                () -> tradeService.sell("u1", "TCS", 5));
    }
}
