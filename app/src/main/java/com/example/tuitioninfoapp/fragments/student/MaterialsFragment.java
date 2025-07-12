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

import com.example.tuitioninfoapp.R;
import com.example.tuitioninfoapp.adapters.MaterialAdapter;
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
    private List<Material> materialList = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView emptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_materials, container, false);

        recyclerView = view.findViewById(R.id.materialsRecyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        emptyView = view.findViewById(R.id.emptyTextView);

        setupRecyclerView();
        loadMaterials();

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MaterialAdapter(materialList, material -> {
            // Open material in browser or appropriate viewer
            openMaterial(material);
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadMaterials() {
        String studentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        progressBar.setVisibility(View.VISIBLE);
        materialList.clear();

        // Query the top-level 'materials' collection
        db.collection("materials")
                .whereArrayContains("studentIds", studentId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> courseIds = new ArrayList<>();
                    for (QueryDocumentSnapshot courseDoc : queryDocumentSnapshots) {
                        courseIds.add(courseDoc.getId());
                    }

                    if (courseIds.isEmpty()) {
                        showEmptyState();
                        return;
                    }

                    // Now get materials from the subcollection for each course
                    for (String courseId : courseIds) {
                        db.collection("materials/" + courseId + "/materials")
                                .get()
                                .addOnSuccessListener(materialSnapshots -> {
                                    for (QueryDocumentSnapshot materialDoc : materialSnapshots) {
                                        Material material = materialDoc.toObject(Material.class);
                                        material.setId(materialDoc.getId());
                                        material.setCourseId(courseId);
                                        materialList.add(material);
                                    }
                                    updateUI();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("MaterialsFragment", "Error loading materials for course: " + courseId, e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("MaterialsFragment", "Error loading courses", e);
                    progressBar.setVisibility(View.GONE);
                });
    }

    private void updateUI() {
        progressBar.setVisibility(View.GONE);
        if (materialList.isEmpty()) {
            showEmptyState();
        } else {
            adapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private void showEmptyState() {
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    private void openMaterial(Material material) {
        // Convert gs:// URL to https:// URL for viewing
        String httpsUrl = material.getFileUrl()
                .replace("gs://", "https://firebasestorage.googleapis.com/v0/b/")
                .replaceFirst("/", ".appspot.com/o/")
                + "?alt=media";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(httpsUrl));
        startActivity(intent);
    }
}