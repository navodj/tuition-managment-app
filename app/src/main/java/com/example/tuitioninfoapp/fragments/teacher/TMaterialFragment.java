package com.example.tuitioninfoapp.fragments.teacher;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitioninfoapp.R;
import com.example.tuitioninfoapp.adapters.TeacherMaterialAdapter;
import com.example.tuitioninfoapp.models.Course;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class TMaterialFragment extends Fragment {

    private RecyclerView recyclerView;
    private TeacherMaterialAdapter adapter;
    private List<Course> courseList = new ArrayList<>();

    private String selectedCourseName = "";
    private String selectedCourseId = "";

    private final ActivityResultLauncher<Intent> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Uri pdfUri = result.getData().getData();
                            uploadPdf(pdfUri);
                        }
                    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_t_material, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewMaterials); // You must define this in your fragment_materials.xml
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TeacherMaterialAdapter(courseList, this::onUploadClicked);
        recyclerView.setAdapter(adapter);

        loadCourses();

        return view;
    }

    private void loadCourses() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("courses")
                .whereEqualTo("teacherId", uid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    courseList.clear();
                    snapshot.getDocuments().forEach(doc -> {
                        Course course = doc.toObject(Course.class);
                        course.setId(doc.getId());
                        courseList.add(course);
                    });
                    adapter.notifyDataSetChanged();
                });
    }

    private void onUploadClicked(Course course) {
        selectedCourseName = course.getName();
        selectedCourseId = course.getId();

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        filePickerLauncher.launch(intent);
    }

    private void uploadPdf(Uri pdfUri) {
        if (pdfUri == null) return;

        FirebaseStorage.getInstance()
                .getReference("course_materials/" + selectedCourseName + ".pdf")
                .putFile(pdfUri)
                .addOnSuccessListener(taskSnapshot ->
                        taskSnapshot.getStorage().getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    String url = uri.toString();
                                    FirebaseFirestore.getInstance()
                                            .collection("courses")
                                            .document(selectedCourseId)
                                            .update("material", url)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                                loadCourses(); // Refresh list
                                            });
                                }));
    }
}

