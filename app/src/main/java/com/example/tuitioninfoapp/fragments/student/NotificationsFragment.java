package com.example.tuitioninfoapp.fragments.student;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitioninfoapp.R;
import com.example.tuitioninfoapp.adapters.NotificationAdapter;
import com.example.tuitioninfoapp.models.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private RecyclerView rvNotifications;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList = new ArrayList<>();

    private static final String TAG = "NotificationsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        rvNotifications = view.findViewById(R.id.rv_notifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationAdapter = new NotificationAdapter(notificationList);
        rvNotifications.setAdapter(notificationAdapter);

        loadNotifications();
        return view;
    }

    private void loadNotifications() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "Logged-in userId: " + userId);

        FirebaseFirestore.getInstance()
                .collection("notifications")
//                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    notificationList.clear();
                    if (!querySnapshot.isEmpty()) {
                        for (com.google.firebase.firestore.DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            Notification notification = doc.toObject(Notification.class);
                            if (notification != null) {
                                notificationList.add(notification);
                            }
                        }
                        notificationAdapter.notifyDataSetChanged();
                        Log.d(TAG, "Loaded " + notificationList.size() + " notifications");
                    } else {
                        Log.d(TAG, "No notifications found.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load notifications", e);
                });
    }
}