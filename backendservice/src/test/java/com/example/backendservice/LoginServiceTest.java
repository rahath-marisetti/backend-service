package com.example.backendservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private LoginRepository loginRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private LoginService loginService;

    private Login existingLogin;

    @BeforeEach
    void setUp() {
        existingLogin = new Login();
        existingLogin.setEmail("user@example.com");
        existingLogin.setPassword("secret123");
        existingLogin.setLocked(false);
        existingLogin.setOtp(1234);
    }

    // --- newLogin ---

    @Test
    void newLogin_whenEmailAlreadyExists_returnsNull() {
        when(loginRepository.findById("user@example.com")).thenReturn(Optional.of(existingLogin));

        Login input = new Login();
        input.setEmail("user@example.com");

        Login result = loginService.newLogin(input);

        assertNull(result);
        verify(loginRepository, never()).save(any());
    }

    @Test
    void newLogin_whenEmailIsNew_savesAndReturnsLogin() {
        Login newLogin = new Login();
        newLogin.setEmail("new@example.com");
        newLogin.setPassword("pass");

        when(loginRepository.findById("new@example.com")).thenReturn(Optional.empty());
        when(loginRepository.save(newLogin)).thenReturn(newLogin);

        Login result = loginService.newLogin(newLogin);

        assertNotNull(result);
        assertEquals("new@example.com", result.getEmail());
        verify(loginRepository).save(newLogin);
    }

    // --- findbyemail ---

    @Test
    void findbyemail_withCorrectPasswordAndUnlocked_returnsPass() {
        when(loginRepository.findById("user@example.com")).thenReturn(Optional.of(existingLogin));

        Login input = new Login();
        input.setEmail("user@example.com");
        input.setPassword("secret123");

        assertEquals("pass", loginService.findbyemail(input));
    }

    @Test
    void findbyemail_withCorrectPasswordButLocked_returnsLockedMessage() {
        existingLogin.setLocked(true);
        when(loginRepository.findById("user@example.com")).thenReturn(Optional.of(existingLogin));

        Login input = new Login();
        input.setEmail("user@example.com");
        input.setPassword("secret123");

        assertEquals("Your account has been locked", loginService.findbyemail(input));
    }

    @Test
    void findbyemail_withWrongPassword_returnsInvalid() {
        when(loginRepository.findById("user@example.com")).thenReturn(Optional.of(existingLogin));

        Login input = new Login();
        input.setEmail("user@example.com");
        input.setPassword("wrongPassword");

        assertEquals("invalid", loginService.findbyemail(input));
    }

    @Test
    void findbyemail_withNonExistentEmail_returnsUsernameDoesNotExist() {
        when(loginRepository.findById("ghost@example.com")).thenReturn(Optional.empty());

        Login input = new Login();
        input.setEmail("ghost@example.com");
        input.setPassword("any");

        assertEquals("username doesn't exist", loginService.findbyemail(input));
    }

    // --- updatePassword ---

    @Test
    void updatePassword_whenUserExists_updatesAndReturnsSuccess() {
        when(loginRepository.findById("user@example.com")).thenReturn(Optional.of(existingLogin));
        when(loginRepository.save(existingLogin)).thenReturn(existingLogin);

        Login input = new Login();
        input.setEmail("user@example.com");
        input.setPassword("newPassword");

        String result = loginService.updatePassword(input);

        assertEquals("Password updated", result);
        assertEquals("newPassword", existingLogin.getPassword());
        verify(loginRepository).save(existingLogin);
    }

    @Test
    void updatePassword_whenUserDoesNotExist_returnsError() {
        when(loginRepository.findById("ghost@example.com")).thenReturn(Optional.empty());

        Login input = new Login();
        input.setEmail("ghost@example.com");
        input.setPassword("any");

        assertEquals("Username doesn't exist", loginService.updatePassword(input));
        verify(loginRepository, never()).save(any());
    }

    // --- generateOTP ---

    @Test
    void generateOTP_whenEmailExists_generatesOTPAndReturnsSuccess() {
        when(loginRepository.findById("user@example.com")).thenReturn(Optional.of(existingLogin));
        when(loginRepository.save(existingLogin)).thenReturn(existingLogin);

        String result = loginService.generateOTP("user@example.com");

        assertEquals("OTP generated successfully", result);
        // OTP should be a 4-digit number in range [1000, 9999]
        assertTrue(existingLogin.getOtp() >= 1000 && existingLogin.getOtp() <= 9999);
        verify(loginRepository).save(existingLogin);
    }

    @Test
    void generateOTP_whenEmailDoesNotExist_returnsInvalidEmail() {
        when(loginRepository.findById("ghost@example.com")).thenReturn(Optional.empty());

        String result = loginService.generateOTP("ghost@example.com");

        assertEquals("Invalid Email Address", result);
        verify(loginRepository, never()).save(any());
    }

    // --- validateOTP ---

    @Test
    void validateOTP_whenOTPMatches_returnsTrue() {
        when(loginRepository.findById("user@example.com")).thenReturn(Optional.of(existingLogin));

        Login input = new Login();
        input.setEmail("user@example.com");
        input.setOtp(1234);

        assertTrue(loginService.validateOTP(input));
    }

    @Test
    void validateOTP_whenOTPDoesNotMatch_returnsFalse() {
        when(loginRepository.findById("user@example.com")).thenReturn(Optional.of(existingLogin));

        Login input = new Login();
        input.setEmail("user@example.com");
        input.setOtp(9999);

        assertFalse(loginService.validateOTP(input));
    }

    @Test
    void validateOTP_whenEmailDoesNotExist_returnsFalse() {
        when(loginRepository.findById("ghost@example.com")).thenReturn(Optional.empty());

        Login input = new Login();
        input.setEmail("ghost@example.com");
        input.setOtp(1234);

        assertFalse(loginService.validateOTP(input));
    }
}
