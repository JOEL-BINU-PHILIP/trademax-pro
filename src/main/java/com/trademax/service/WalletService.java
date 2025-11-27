package com.trademax.service;

import com.trademax.exception.InsufficientFundsException;
import com.trademax.exception.UserNotFoundException;
import com.trademax.model.User;
import com.trademax.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    private final UserRepository userRepo;

    public WalletService(UserRepository userRepo){ this.userRepo = userRepo; }

    public double addMoney(String userId, double amount) {
        User u = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        u.getWallet().setBalance(u.getWallet().getBalance() + amount);
        userRepo.save(u);
        return u.getWallet().getBalance();
    }

    public double withdrawMoney(String userId, double amount) {
        User u = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        double bal = u.getWallet().getBalance();

        if (bal < amount) throw new InsufficientFundsException("Insufficient balance");

        u.getWallet().setBalance(bal - amount);
        userRepo.save(u);
        return u.getWallet().getBalance();
    }
}
