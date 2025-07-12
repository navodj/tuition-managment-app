package com.example.tuitioninfoapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tuitioninfoapp.R;
import com.example.tuitioninfoapp.models.Assignment;
import com.example.tuitioninfoapp.models.Course;
import com.example.tuitioninfoapp.models.User;
import java.util.ArrayList;
import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ViewHolder> {
    private List<Assignment> assignments = new ArrayList<>();
    private List<Course> allCourses = new ArrayList<>();
    private List<User> allTeachers = new ArrayList<>();
    private List<User> allStudents = new ArrayList<>();

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments != null ? assignments : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setAllCourses(List<Course> courses) {
        this.allCourses = courses != null ? courses : new ArrayList<>();
    }

    public void setAllTeachers(List<User> teachers) {
        this.allTeachers = teachers != null ? teachers : new ArrayList<>();
    }

    public void setAllStudents(List<User> students) {
        this.allStudents = students != null ? students : new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_assignment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Assignment assignment = assignments.get(position);

        // Find course
        Course course = findCourseById(assignment.getCourseId());
        if (course != null) {
            holder.tvCourseName.setText(course.getName());
        } else {
            holder.tvCourseName.setText("Course not found");
        }

        // Find teacher
        User teacher = findUserById(assignment.getTeacherId(), allTeachers);
        if (teacher != null) {
            holder.tvTeacherName.setText("Teacher: " + teacher.getName());
        } else {
            holder.tvTeacherName.setText("Teacher: Not assigned");
        }

        // Setup student list
        List<User> assignedStudents = new ArrayList<>();
        if (assignment.getStudentIds() != null) {
            for (String studentId : assignment.getStudentIds()) {
                User student = findUserById(studentId, allStudents);
                if (student != null) {
                    assignedStudents.add(student);
                }
            }
        }

        // Create and set student adapter
        StudentListAdapter studentAdapter = new StudentListAdapter(assignedStudents);
        holder.rvStudents.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.rvStudents.setAdapter(studentAdapter);

        // Show student count if needed
        holder.tvStudentCount.setText("Students: " + assignedStudents.size());
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    private Course findCourseById(String courseId) {
        for (Course course : allCourses) {
            if (course.getId().equals(courseId)) {
                return course;
            }
        }
        return null;
    }

    private User findUserById(String userId, List<User> userList) {
        if (userId == null || userList == null) return null;

        for (User user : userList) {
            if (userId.equals(user.getId())) {
                return user;
            }
        }
        return null;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName;
        TextView tvTeacherName;
        TextView tvStudentCount;
        RecyclerView rvStudents;

        ViewHolder(View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tv_course_name);
            tvTeacherName = itemView.findViewById(R.id.tv_teacher_name);
            tvStudentCount = itemView.findViewById(R.id.tv_student_count);
            rvStudents = itemView.findViewById(R.id.rv_assignment_students);
        }
    }
}