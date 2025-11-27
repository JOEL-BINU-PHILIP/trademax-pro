package com.trademax.controller;

import com.trademax.service.DummyStockApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {
    private final DummyStockApiService svc;
    public StockController(DummyStockApiService svc){this.svc=svc;}

    @GetMapping
    public ResponseEntity<List<DummyStockApiService.StockInfo>> all(){ 
        return ResponseEntity.ok(svc.listStocks());
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<DummyStockApiService.StockInfo> byTicker(@PathVariable String ticker){
        return ResponseEntity.ok(svc.getStock(ticker));
    }
}
