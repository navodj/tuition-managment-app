package com.example.tuitioninfoapp.services;

import android.util.Log;

import com.example.tuitioninfoapp.models.Notification;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class NotificationService {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void sendNotification(String userId, String message) {
        String notificationId = UUID.randomUUID().toString();

        Notification notification = new Notification(
                notificationId,
                userId,
                message,
                Timestamp.now(),
                false
        );

        db.collection("notifications")
                .document(notificationId)
                .set(notification)
                .addOnSuccessListener(aVoid -> Log.d("NotificationService", "Notification sent"))
                .addOnFailureListener(e -> Log.e("NotificationService", "Failed to send notification", e));
    }

    public void sendNotificationToUser(String studentId, String title, String message) {
    }
}