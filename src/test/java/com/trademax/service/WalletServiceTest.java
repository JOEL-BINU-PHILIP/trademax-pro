package com.trademax.service;

import com.trademax.exception.InsufficientFundsException;
import com.trademax.exception.UserNotFoundException;
import com.trademax.model.User;
import com.trademax.model.Wallet;
import com.trademax.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WalletService walletService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addMoney_shouldIncreaseBalance() {
        User u = new User("id1", "Joel", "email", "PAN", new Wallet(100.0), null);

        when(userRepository.findById("id1")).thenReturn(Optional.of(u));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        double newBalance = walletService.addMoney("id1", 50.0);

        assertEquals(150.0, newBalance);
        verify(userRepository).save(u);
    }

    @Test
    void addMoney_shouldThrowIfUserMissing() {
        when(userRepository.findById("x")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> walletService.addMoney("x", 100));
    }

    @Test
    void withdrawMoney_shouldReduceBalance() {
        User u = new User("id1", "Joel", "email", "PAN", new Wallet(200.0), null);

        when(userRepository.findById("id1")).thenReturn(Optional.of(u));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        double newBalance = walletService.withdrawMoney("id1", 50.0);

        assertEquals(150.0, newBalance);
        verify(userRepository).save(u);
    }

    @Test
    void withdrawMoney_shouldFailIfInsufficientFunds() {
        User u = new User("id2", "Joel", "email", "PAN", new Wallet(20.0), null);

        when(userRepository.findById("id2")).thenReturn(Optional.of(u));

        assertThrows(InsufficientFundsException.class,
                () -> walletService.withdrawMoney("id2", 50.0));
    }

    @Test
    void withdrawMoney_shouldThrowIfUserMissing() {
        when(userRepository.findById("nope")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> walletService.withdrawMoney("nope", 10));
    }
}
