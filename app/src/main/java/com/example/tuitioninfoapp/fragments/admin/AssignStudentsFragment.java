package com.example.tuitioninfoapp.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tuitioninfoapp.R;
import com.example.tuitioninfoapp.adapters.StudentAssignAdapter;
import com.example.tuitioninfoapp.models.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AssignStudentsFragment extends Fragment {

    private Spinner teacherSpinner;
    private RecyclerView studentRecyclerView;
    private Button assignButton;

    private FirebaseFirestore db;
    private List<User> teachers = new ArrayList<>();
    private List<User> students = new ArrayList<>();
    private StudentAssignAdapter adapter;

    public AssignStudentsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assign_students, container, false);

        teacherSpinner = view.findViewById(R.id.teacherSpinner);
        studentRecyclerView = view.findViewById(R.id.studentRecyclerView);
        assignButton = view.findViewById(R.id.assignButton);

        db = FirebaseFirestore.getInstance();

        studentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StudentAssignAdapter(students);
        studentRecyclerView.setAdapter(adapter);

        loadTeachers();
        loadStudents();

        assignButton.setOnClickListener(v -> assignStudents());

        return view;
    }

    private void loadTeachers() {
        db.collection("users")
                .whereEqualTo("role", "teacher")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    teachers.clear();
                    List<String> teacherNames = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        User user = doc.toObject(User.class);
                        teachers.add(user);
                        teacherNames.add(user.getName());
                    }

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                            android.R.layout.simple_spinner_item, teacherNames);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    teacherSpinner.setAdapter(spinnerAdapter);
                });
    }

    private void loadStudents() {
        db.collection("users")
                .whereEqualTo("role", "student")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    students.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        students.add(doc.toObject(User.class));
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void assignStudents() {
        int selectedTeacherIndex = teacherSpinner.getSelectedItemPosition();
        if (selectedTeacherIndex == -1 || teachers.isEmpty()) {
            Toast.makeText(getContext(), "Select a teacher", Toast.LENGTH_SHORT).show();
            return;
        }

        String teacherUid = teachers.get(selectedTeacherIndex).getUid();
        List<String> selectedStudentIds = adapter.getSelectedStudentIds();

        for (String studentId : selectedStudentIds) {
            db.collection("users").document(studentId)
                    .update("assignedTeacherId", teacherUid);
        }

        Toast.makeText(getContext(), "Students assigned successfully", Toast.LENGTH_SHORT).show();
    }
}
