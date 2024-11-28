package be.pxl.services.services;

import be.pxl.services.Domain.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements INotificationService {
    private final JavaMailSender javaMailSender;
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public NotificationService(JavaMailSender mailSender) {
        this.javaMailSender = mailSender;
    }

    @RabbitListener(queues = "sendEmail")
    @Override
    public void sendEmail(NotificationRequest notification) {
        log.info("sending email...");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("rhanipieters@gmail.com");
        message.setTo(notification.getTo());
        message.setSubject(notification.getSubject());
        message.setText(notification.getBody());
        javaMailSender.send(message);
    }
}
