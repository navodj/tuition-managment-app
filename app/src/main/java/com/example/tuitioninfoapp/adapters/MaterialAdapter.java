package com.example.tuitioninfoapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitioninfoapp.R;
import com.example.tuitioninfoapp.models.Material;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    private Context context;
    private List<Material> materialList;

    public MaterialAdapter(Context context, List<Material> materialList) {
        this.context = context;
        this.materialList = materialList;
    }

    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_material, parent, false);
        return new MaterialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {
        Material material = materialList.get(position);
        holder.courseName.setText(material.getCourseName());

        // ✅ Set click listener to button
        holder.viewPdfButton.setOnClickListener(v -> {
            FirebaseStorage.getInstance()
                    .getReferenceFromUrl(material.getMaterialUrl())
                    .getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        try {
                            context.startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(context, "No PDF viewer installed.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to get file URL.", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }

    public static class MaterialViewHolder extends RecyclerView.ViewHolder {
        TextView courseName;
        Button viewPdfButton;

        public MaterialViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.text_course_name);
            viewPdfButton = itemView.findViewById(R.id.btn_view_pdf);
        }
    }
}
