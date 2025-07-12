package com.example.tuitioninfoapp.adapters;

import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitioninfoapp.R;
import com.example.tuitioninfoapp.models.Material;

import java.text.SimpleDateFormat;
import java.util.List;
public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ViewHolder> {
    private List<Material> materials;
    private OnMaterialClickListener listener;

    public interface OnMaterialClickListener {
        void onMaterialClick(Material material);
    }

    public MaterialAdapter(List<Material> materials, OnMaterialClickListener listener) {
        this.materials = materials;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_material, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Material material = materials.get(position);

        // Set title - handle empty titles
        String displayTitle = !TextUtils.isEmpty(material.getTitle()) ?
                material.getTitle() :
                "Material " + (position + 1);
        holder.title.setText(displayTitle);

        // Set file type indicator
        String fileType = !TextUtils.isEmpty(material.getType()) ?
                material.getType().toLowerCase() :
                "file";

        int iconRes = R.drawable.ic_document; // Default
        switch (fileType) {
            case "pdf": iconRes = R.drawable.ic_pdf; break;
            case "doc":
            case "docx": iconRes = R.drawable.ic_word; break;
            case "ppt":
            case "pptx": iconRes = R.drawable.ic_powerpoint; break;
//            case "xls":
//            case "xlsx": iconRes = R.drawable.ic_excel; break;
//            case "jpg":
//            case "jpeg":
//            case "png": iconRes = R.drawable.ic_image; break;
        }
        holder.icon.setImageResource(iconRes);

        // Set file name from URL
        if (!TextUtils.isEmpty(material.getFileUrl())) {
            Uri uri = Uri.parse(material.getFileUrl());
            String fileName = uri.getLastPathSegment();
            holder.fileName.setText(fileName);
        } else {
            holder.fileName.setText("Unknown file");
        }

        holder.itemView.setOnClickListener(v -> listener.onMaterialClick(material));
    }

    @Override
    public int getItemCount() {
        return materials.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, fileName;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.materialTitle);
            fileName = itemView.findViewById(R.id.materialFileName);
            icon = itemView.findViewById(R.id.materialIcon);
        }
    }
}