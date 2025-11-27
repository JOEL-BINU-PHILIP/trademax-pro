package com.trademax.controller;

import com.trademax.model.PortfolioItem;
import com.trademax.model.User;
import com.trademax.service.DummyStockApiService;
import com.trademax.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {
    private final UserService userSvc;
    private final DummyStockApiService stockSvc;

    public PortfolioController(UserService userSvc, DummyStockApiService stockSvc){
        this.userSvc = userSvc; 
        this.stockSvc = stockSvc;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> view(@PathVariable String userId) {
        User u = userSvc.get(userId);
        List<PortfolioItem> pf = u.getPortfolio();
        if (pf == null || pf.isEmpty()) return ResponseEntity.ok("No holdings");

        var details = pf.stream().map(item -> {
            var stock = stockSvc.getStock(item.getTicker());
            double market = stock.getPrice();
            double invested = item.getAvgBuyPrice() * item.getQuantity();
            double current = market * item.getQuantity();
            double plPercent = invested == 0 ? 0.0 : ((current - invested) / invested) * 100.0;

            return new Object() {
                public String ticker = item.getTicker();
                public int quantity = item.getQuantity();
                public double avgBuyPrice = item.getAvgBuyPrice();
                public double marketPrice = market;
                public double investedValue = invested;
                public double currentValue = current;
                public double plPercentRounded = Math.round(plPercent * 100.0) / 100.0;
            };
        }).collect(Collectors.toList());

        return ResponseEntity.ok(details);
    }
}
