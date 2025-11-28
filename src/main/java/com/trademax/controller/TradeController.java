package com.trademax.controller;

import com.trademax.dto.BuyRequest;
import com.trademax.dto.SellRequest;
import com.trademax.service.TradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trade")
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @PostMapping("/buy")
    public ResponseEntity<String> buyStock(@RequestBody BuyRequest request) {
        tradeService.buy(request.getUserId(), request.getTicker(), request.getQuantity());
        return ResponseEntity.ok("Stock bought successfully");
    }

    @PostMapping("/sell")
    public ResponseEntity<String> sellStock(@RequestBody SellRequest request) {
        tradeService.sell(request.getUserId(), request.getTicker(), request.getQuantity());
        return ResponseEntity.ok("Stock sold successfully");
    }
}
