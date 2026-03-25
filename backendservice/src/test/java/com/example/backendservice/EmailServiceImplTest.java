package com.example.backendservice;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    private EmailDetails details;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "sender", "noreply@example.com");

        details = new EmailDetails();
        details.setRecipient("recipient@example.com");
        details.setMsgBody("Test message body");
        details.setSubject("Test Subject");
    }

    // --- sendSimpleMail ---

    @Test
    void sendSimpleMail_success_returnsSuccessMessage() {
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        String result = emailService.sendSimpleMail(details);

        assertEquals("Mail Sent Successfully...", result);
        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendSimpleMail_whenExceptionThrown_returnsErrorMessage() {
        doThrow(new RuntimeException("SMTP error")).when(javaMailSender).send(any(SimpleMailMessage.class));

        String result = emailService.sendSimpleMail(details);

        assertEquals("Error while Sending Mail", result);
    }

    // --- sendMailWithAttachment ---

    @Test
    void sendMailWithAttachment_whenAttachmentFileDoesNotExist_returnsErrorMessage() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        details.setAttachment("/nonexistent/path/file.txt");

        // MimeMessageHelper will throw when trying to add a non-existent file
        String result = emailService.sendMailWithAttachment(details);

        assertEquals("Error while sending mail!!!", result);
    }
}
