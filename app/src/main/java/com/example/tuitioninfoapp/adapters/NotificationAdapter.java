package com.example.tuitioninfoapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitioninfoapp.R;
import com.example.tuitioninfoapp.models.Notification;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notificationList;

    public NotificationAdapter(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.messageTextView.setText(notification.getMessage());

        // Optional timestamp formatting
        Timestamp timestamp = notification.getTimestamp();
        if (timestamp != null) {
            String formatted = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                    .format(timestamp.toDate());
            holder.timestampTextView.setText(formatted);
        } else {
            holder.timestampTextView.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView, timestampTextView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.tv_notification_message);
            timestampTextView = itemView.findViewById(R.id.tv_notification_timestamp);
        }
    }
}