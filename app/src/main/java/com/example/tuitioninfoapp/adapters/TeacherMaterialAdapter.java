package com.example.tuitioninfoapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitioninfoapp.R;
import com.example.tuitioninfoapp.models.Course;

import java.util.List;

public class TeacherMaterialAdapter extends RecyclerView.Adapter<TeacherMaterialAdapter.CourseViewHolder> {

    public interface OnUploadClickListener {
        void onUploadClicked(Course course);
    }

    private List<Course> courseList;
    private OnUploadClickListener listener;

    public TeacherMaterialAdapter(List<Course> courseList, OnUploadClickListener listener) {
        this.courseList = courseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_teacher_material, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.courseName.setText(course.getName());

        if (course.getMaterialUrl() != null && !course.getMaterialUrl().isEmpty()) {
            holder.status.setText("Already Uploaded");
            holder.uploadBtn.setEnabled(false);
        } else {
            holder.status.setText("Not Uploaded");
            holder.uploadBtn.setEnabled(true);
            holder.uploadBtn.setOnClickListener(v -> listener.onUploadClicked(course));
        }
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, status;
        Button uploadBtn;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.courseNameTextView);
            status = itemView.findViewById(R.id.statusTextView);
            uploadBtn = itemView.findViewById(R.id.uploadPdfBtn);
        }
    }
}
