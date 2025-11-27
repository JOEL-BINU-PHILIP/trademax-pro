package com.trademax.service;

import com.trademax.exception.UserNotFoundException;
import com.trademax.model.User;
import com.trademax.model.Wallet;
import com.trademax.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldGenerateIdIfMissing() {
        User input = new User(null, "Joel", "joel@test.com", "PAN123", null, null);

        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        User saved = userService.register(input);

        assertNotNull(saved.getId());
        assertEquals(36, saved.getId().length()); // UUID
        assertEquals(0.0, saved.getWallet().getBalance());
        assertTrue(saved.getPortfolio().isEmpty());

        verify(userRepository).save(any());
    }

    @Test
    void register_shouldKeepExistingId() {
        User input = new User("abc123", "Joel", "abc@test.com", "PAN123", null, null);

        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        User saved = userService.register(input);

        assertEquals("abc123", saved.getId());
        assertNotNull(saved.getWallet());
    }

    @Test
    void get_shouldReturnUser() {
        User u = new User("id1", "Joel", "test", "PAN", new Wallet(100), null);

        when(userRepository.findById("id1")).thenReturn(Optional.of(u));

        User result = userService.get("id1");

        assertEquals("id1", result.getId());
        verify(userRepository).findById("id1");
    }

    @Test
    void get_shouldThrowExceptionIfNotFound() {
        when(userRepository.findById("x")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.get("x"));
    }
}
