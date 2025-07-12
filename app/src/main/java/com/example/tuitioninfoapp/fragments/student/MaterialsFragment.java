package com.example.tuitioninfoapp.fragments.student;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuitioninfoapp.R;
import com.example.tuitioninfoapp.adapters.MaterialAdapter;
import com.example.tuitioninfoapp.models.Course;
import com.example.tuitioninfoapp.models.Material;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
public class MaterialsFragment extends Fragment {
    private RecyclerView recyclerView;
    private MaterialAdapter adapter;
    private List<Course> courseList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_materials, container, false);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        courseList = new ArrayList<>();
        adapter = new MaterialAdapter(courseList, getContext());
        recyclerView.setAdapter(adapter);

        // Load courses
        loadStudentCourses();
        return view;
    }

    private void loadStudentCourses() {
        String studentId = auth.getCurrentUser().getUid();
        db.collection("courses")
                .whereArrayContains("studentIds", studentId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        courseList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Course course = document.toObject(Course.class);
                            course.setId(document.getId());
                            courseList.add(course);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Error loading courses", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}