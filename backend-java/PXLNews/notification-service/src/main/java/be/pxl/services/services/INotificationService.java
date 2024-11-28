package be.pxl.services.services;

import be.pxl.services.Domain.NotificationRequest;

public interface INotificationService {
    void sendEmail(NotificationRequest notification);
}
