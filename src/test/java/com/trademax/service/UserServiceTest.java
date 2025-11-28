package com.trademax.service;

import com.trademax.exception.DuplicateFieldException;
import com.trademax.exception.UserNotFoundException;
import com.trademax.model.User;
import com.trademax.model.Wallet;
import com.trademax.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTests {

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // register success generate id

    @Test
    void register_shouldGenerateIdIfMissing() {
        User input = new User(null, "Joel", "joel@test.com", "ABCDE1234F", null, null);

        when(userRepo.findByEmail("joel@test.com")).thenReturn(Optional.empty());
        when(userRepo.findByPan("ABCDE1234F")).thenReturn(Optional.empty());
        when(userRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        User saved = userService.register(input);

        assertNotNull(saved.getId());
        assertEquals(36, saved.getId().length());
        assertNotNull(saved.getWallet());
        assertEquals(0.0, saved.getWallet().getBalance());
        assertNotNull(saved.getPortfolio());
        assertTrue(saved.getPortfolio().isEmpty());
    }

    // register success

    @Test
    void register_shouldKeepExistingId() {
        User input = new User("abc123", "Joel", "test@test.com", "PQRSX6789Z", null, null);

        when(userRepo.findByEmail("test@test.com")).thenReturn(Optional.empty());
        when(userRepo.findByPan("PQRSX6789Z")).thenReturn(Optional.empty());
        when(userRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        User saved = userService.register(input);

        assertEquals("abc123", saved.getId());
        assertNotNull(saved.getWallet());
        assertNotNull(saved.getPortfolio());
    }

    //duplicate email

    @Test
    void register_shouldThrowDuplicateEmail() {
        User input = new User(null, "Joel", "dup@test.com", "ABCDE1234F", null, null);

        when(userRepo.findByEmail("dup@test.com")).thenReturn(Optional.of(input));

        DuplicateFieldException ex = assertThrows(
                DuplicateFieldException.class,
                () -> userService.register(input)
        );

        assertEquals("Email already exists", ex.getMessage());
    }

    // duplicate pan

    @Test
    void register_shouldThrowDuplicatePan() {
        User input = new User(null, "Joel", "unique@test.com", "ABCDE1234F", null, null);

        when(userRepo.findByEmail("unique@test.com")).thenReturn(Optional.empty());
        when(userRepo.findByPan("ABCDE1234F")).thenReturn(Optional.of(input));

        DuplicateFieldException ex = assertThrows(
                DuplicateFieldException.class,
                () -> userService.register(input)
        );

        assertEquals("PAN already exists", ex.getMessage());
    }

    // get success

    @Test
    void get_shouldReturnUser() {
        User u = new User("id1", "Joel", "a@b.com", "ABCDE1234F", new Wallet(100), Collections.emptyList());

        when(userRepo.findById("id1")).thenReturn(Optional.of(u));

        User result = userService.get("id1");

        assertEquals("id1", result.getId());
        assertEquals("a@b.com", result.getEmail());
    }

    // Get user not found

    @Test
    void get_shouldThrowExceptionIfNotFound() {
        when(userRepo.findById("missing")).thenReturn(Optional.empty());

        UserNotFoundException ex =
                assertThrows(UserNotFoundException.class, () -> userService.get("missing"));

        assertEquals("UserId not found: missing", ex.getMessage());
    }

    // save method

    @Test
    void save_shouldCallRepositorySave() {
        User u = new User("1", "Joel", "a@b.com", "ABCDE1234F", new Wallet(0), Collections.emptyList());

        when(userRepo.save(u)).thenReturn(u);

        User saved = userService.save(u);

        assertEquals("1", saved.getId());
        verify(userRepo).save(u);
    }
}
