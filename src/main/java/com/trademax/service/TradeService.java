package com.trademax.service;

import com.trademax.exception.*;
import com.trademax.model.PortfolioItem;
import com.trademax.model.User;
import com.trademax.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TradeService {
    private final DummyStockApiService stockApi;
    private final UserRepository userRepo;

    public TradeService(DummyStockApiService stockApi, UserRepository userRepo) {
        this.stockApi = stockApi;
        this.userRepo = userRepo;
    }

    public synchronized void buy(String userId, String ticker, int qty) {
        if (qty <= 0) throw new InvalidQuantityException("Quantity must be > 0");

        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        DummyStockApiService.StockInfo stock = stockApi.getStock(ticker);

        double total = stock.getPrice() * qty;
        double bal = user.getWallet().getBalance();

        if (bal < total) throw new InsufficientFundsException("Need " + total + ", available " + bal);

        user.getWallet().setBalance(bal - total);

        List<PortfolioItem> pf = user.getPortfolio() != null ? new ArrayList<>(user.getPortfolio()) : new ArrayList<>();
        PortfolioItem existing = pf.stream()
                .filter(p -> p.getTicker().equalsIgnoreCase(stock.getTicker()))
                .findFirst()
                .orElse(null);

        if (existing == null) {
            pf.add(new PortfolioItem(stock.getTicker(), qty, stock.getPrice()));
        } else {
            int newQty = existing.getQuantity() + qty;
            double newAvg = ((existing.getAvgBuyPrice() * existing.getQuantity()) + (stock.getPrice() * qty)) / newQty;
            existing.setQuantity(newQty);
            existing.setAvgBuyPrice(Math.round(newAvg * 100.0) / 100.0);
        }

        user.setPortfolio(pf);
        userRepo.save(user);
    }

    public synchronized void sell(String userId, String ticker, int qty) {
        if (qty <= 0) throw new InvalidQuantityException("Quantity must be > 0");

        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        List<PortfolioItem> pf = user.getPortfolio();
        if (pf == null) throw new InsufficientHoldingsException("No holdings");

        PortfolioItem item = pf.stream()
                .filter(p -> p.getTicker().equalsIgnoreCase(ticker))
                .findFirst()
                .orElse(null);

        if (item == null) throw new InsufficientHoldingsException("Ticker not in portfolio: " + ticker);
        if (item.getQuantity() < qty) throw new InsufficientHoldingsException("Not enough quantity. Owned: " + item.getQuantity());

        DummyStockApiService.StockInfo stock = stockApi.getStock(ticker);

        double earnings = stock.getPrice() * qty;
        user.getWallet().setBalance(user.getWallet().getBalance() + earnings);

        int remaining = item.getQuantity() - qty;
        if (remaining == 0) pf.remove(item);
        else item.setQuantity(remaining);

        user.setPortfolio(pf);
        userRepo.save(user);
    }
}
