package com.example.tuitioninfoapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tuitioninfoapp.R;
import com.example.tuitioninfoapp.models.User;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultiSelectStudentAdapter extends RecyclerView.Adapter<MultiSelectStudentAdapter.ViewHolder> {
    private final List<User> students;
    private final Set<String> selectedStudents = new HashSet<>();
    private final Set<String> initiallySelected = new HashSet<>();

    public MultiSelectStudentAdapter(List<User> students, List<String> preSelectedIds) {
        this.students = students;
        if (preSelectedIds != null) {
            selectedStudents.addAll(preSelectedIds);
            initiallySelected.addAll(preSelectedIds);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_checkbox, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User student = students.get(position);
        holder.tvStudentName.setText(student.getName());

        // Show current courses
        if (student.getCourses() != null && !student.getCourses().isEmpty()) {
            holder.tvCurrentCourses.setText("Courses: " + student.getCourses().size());
        } else {
            holder.tvCurrentCourses.setText("No courses");
        }

        // Set checkbox state
        holder.cbStudent.setChecked(selectedStudents.contains(student.getId()));

        holder.cbStudent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedStudents.add(student.getId());
            } else {
                selectedStudents.remove(student.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public Set<String> getSelectedStudents() {
        return selectedStudents;
    }

    public Set<String> getAddedStudents() {
        Set<String> added = new HashSet<>(selectedStudents);
        added.removeAll(initiallySelected);
        return added;
    }

    public Set<String> getRemovedStudents() {
        Set<String> removed = new HashSet<>(initiallySelected);
        removed.removeAll(selectedStudents);
        return removed;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final CheckBox cbStudent;
        final TextView tvStudentName;
        final TextView tvCurrentCourses;

        ViewHolder(View itemView) {
            super(itemView);
            cbStudent = itemView.findViewById(R.id.cb_student);
            tvStudentName = itemView.findViewById(R.id.tv_student_name);
            tvCurrentCourses = itemView.findViewById(R.id.tv_current_courses);
        }
    }
}