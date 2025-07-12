package com.example.tuitioninfoapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tuitioninfoapp.R;
import com.example.tuitioninfoapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class StudentAssignAdapter extends RecyclerView.Adapter<StudentAssignAdapter.StudentViewHolder> {

    private List<User> studentList;
    private List<String> selectedStudentIds = new ArrayList<>();

    public StudentAssignAdapter(List<User> studentList) {
        this.studentList = studentList;
    }

    public List<String> getSelectedStudentIds() {
        return selectedStudentIds;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_assign, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        User student = studentList.get(position);
        holder.name.setText(student.getName());
        holder.checkBox.setChecked(selectedStudentIds.contains(student.getUid()));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedStudentIds.add(student.getUid());
            } else {
                selectedStudentIds.remove(student.getUid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CheckBox checkBox;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.studentNameText);
            checkBox = itemView.findViewById(R.id.selectCheckBox);
        }
    }
}