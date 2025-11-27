package com.trademax.controller;

import com.trademax.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    private final WalletService svc;
    public WalletController(WalletService svc){this.svc=svc;}

    @PostMapping("/add")
    public ResponseEntity<Double> add(
            @RequestParam String userId,
            @RequestParam double amount){
        return ResponseEntity.ok(svc.addMoney(userId, amount));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Double> withdraw(
            @RequestParam String userId,
            @RequestParam double amount){
        return ResponseEntity.ok(svc.withdrawMoney(userId, amount));
    }
}
