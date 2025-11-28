package com.trademax.service;

import com.trademax.exception.DuplicateFieldException;
import com.trademax.exception.UserNotFoundException;
import com.trademax.model.User;
import com.trademax.model.Wallet;
import com.trademax.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) { this.userRepo = userRepo; }

    public User register(User u) {
        if (u.getId() == null || u.getId().isEmpty())
            u.setId(UUID.randomUUID().toString());

        if (u.getWallet() == null)
            u.setWallet(new Wallet(0.0));

        if (u.getPortfolio() == null)
            u.setPortfolio(Collections.emptyList());
        
        if (userRepo.findByEmail(u.getEmail()).isPresent()) {
            throw new DuplicateFieldException("Email already exists");
        }

        if (userRepo.findByPan(u.getPan()).isPresent()) {
            throw new DuplicateFieldException("PAN already exists");
        }
        return userRepo.save(u);
    }

    public User get(String userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("UserId not found: " + userId));
    }

    public User save(User u) { return userRepo.save(u); }
    
}
