package com.example.tuitioninfoapp.fragments.student;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import com.example.tuitioninfoapp.R;
import com.example.tuitioninfoapp.adapters.MaterialAdapter;
import com.example.tuitioninfoapp.models.Material;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.*;

public class MaterialsFragment extends Fragment {

    private RecyclerView recyclerView;
    private MaterialAdapter adapter;
    private List<Material> materialList;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    public MaterialsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_materials, container, false);

        recyclerView = view.findViewById(R.id.materials_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        materialList = new ArrayList<>();
        adapter = new MaterialAdapter(getContext(), materialList);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        fetchMaterialsForStudent();

        return view;
    }

    private void fetchMaterialsForStudent() {
        String uid = auth.getCurrentUser().getUid();

        firestore.collection("courses")
                .whereArrayContains("studentIds", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    materialList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String courseName = doc.getString("name");
                        String materialUrl = doc.getString("material");

                        if (materialUrl != null && !materialUrl.isEmpty()) {
                            materialList.add(new Material(courseName, materialUrl));
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("MaterialsFragment", "Error fetching materials", e));
    }
}
