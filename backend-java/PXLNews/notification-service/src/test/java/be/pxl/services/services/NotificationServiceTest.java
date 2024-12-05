package be.pxl.services.services;

import be.pxl.services.domain.NotificationRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class NotificationServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private NotificationService notificationService;

    public NotificationServiceTest() {
        openMocks(this);
    }

    @Test
    public void sendEmailShouldSendCorrectEmail() {
        // Arrange
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setTo("recipient@example.com");
        notificationRequest.setSubject("Test Subject");
        notificationRequest.setBody("This is a test email.");

        ArgumentCaptor<SimpleMailMessage> mailCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // Act
        notificationService.sendEmail(notificationRequest);

        // Assert
        verify(javaMailSender).send(mailCaptor.capture());
        SimpleMailMessage sentMessage = mailCaptor.getValue();

        assertEquals("rhanipieters@gmail.com", sentMessage.getFrom());
        assertEquals("recipient@example.com", sentMessage.getTo()[0]);
        assertEquals("Test Subject", sentMessage.getSubject());
        assertEquals("This is a test email.", sentMessage.getText());
    }

    @Test
    public void sendEmailShouldLogMessage() {
        // Arrange
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setTo("recipient@example.com");
        notificationRequest.setSubject("Log Test");
        notificationRequest.setBody("Check log functionality.");

        ArgumentCaptor<SimpleMailMessage> mailCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // Act
        notificationService.sendEmail(notificationRequest);

        // Assert
        verify(javaMailSender).send(mailCaptor.capture());
        SimpleMailMessage sentMessage = mailCaptor.getValue();

        assertEquals("rhanipieters@gmail.com", sentMessage.getFrom());
        assertEquals("recipient@example.com", sentMessage.getTo()[0]);
        assertEquals("Log Test", sentMessage.getSubject());
        assertEquals("Check log functionality.", sentMessage.getText());
    }

}
