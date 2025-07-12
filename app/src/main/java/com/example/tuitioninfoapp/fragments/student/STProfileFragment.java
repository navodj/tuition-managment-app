package com.example.tuitioninfoapp.fragments.student;
import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.location.*;


import android.net.Uri;
import android.content.Intent;


import android.content.Intent;
import com.example.tuitioninfoapp.activities.LoginActivity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.tuitioninfoapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.*;

import java.util.*;

public class STProfileFragment extends Fragment {

    private EditText nameEdit, dobEdit, addressEdit, mobileEdit;
    private Spinner nationalitySpinner, genderSpinner;
    private ImageView profileImage;
    private Button saveButton;

    private Uri selectedImageUri;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;

    private final String[] nationalities = {"Sinhala", "Tamil", "Muslim", "Other"};
    private final String[] genders = {"Male", "Female", "Other", "Not Specified"};

    private String uid;

    public STProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_s_t_profile, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_fragment);

        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                LatLng classLocation = new LatLng(6.907383306115219, 79.87109048982776);
                googleMap.addMarker(new MarkerOptions().position(classLocation).title("Class Location"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(classLocation, 15));

                // Optionally enable My Location
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
                }
            });
        }

        Button logoutButton = view.findViewById(R.id.btn_logout);

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();  // sign out user
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // clear back stack
            startActivity(intent);
        });

        Button locationButton = view.findViewById(R.id.btn_location);

        locationButton.setOnClickListener(v -> openGoogleMapsDirections());

        nameEdit = view.findViewById(R.id.edit_name);
        dobEdit = view.findViewById(R.id.edit_dob);
        addressEdit = view.findViewById(R.id.edit_address);
        mobileEdit = view.findViewById(R.id.edit_mobile);
        nationalitySpinner = view.findViewById(R.id.spinner_nationality);
        genderSpinner = view.findViewById(R.id.spinner_gender);
        profileImage = view.findViewById(R.id.profile_image);
        saveButton = view.findViewById(R.id.btn_save);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) uid = currentUser.getUid();

        setupSpinners();
        setupDOBPicker();
        loadUserData();

        profileImage.setOnClickListener(v -> selectImage());
        saveButton.setOnClickListener(v -> saveUserData());

        return view;


    }

    private void setupSpinners() {
        nationalitySpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, nationalities));

        genderSpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, genders));
    }

    private void setupDOBPicker() {
        dobEdit.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(getContext(),
                    (view, year, month, dayOfMonth) ->
                            dobEdit.setText(String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth)),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
    }

    private void loadUserData() {
        if (uid == null) return;
        firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        nameEdit.setText(doc.getString("name"));
                        dobEdit.setText(doc.getString("dob"));
                        addressEdit.setText(doc.getString("address"));
                        mobileEdit.setText(doc.getString("mobile"));

                        setSpinnerSelection(nationalitySpinner, nationalities, doc.getString("nationality"));
                        setSpinnerSelection(genderSpinner, genders, doc.getString("gender"));

                        String imageUrl = doc.getString("profileUrl");
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(this).load(imageUrl).into(profileImage);
                        }
                    }
                });
    }

    private void setSpinnerSelection(Spinner spinner, String[] items, String value) {
        if (value == null) return;
        for (int i = 0; i < items.length; i++) {
            if (value.equalsIgnoreCase(items[i])) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1001);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            profileImage.setImageURI(selectedImageUri);
        }
    }

    private void saveUserData() {
        String name = nameEdit.getText().toString().trim();
        String dob = dobEdit.getText().toString().trim();
        String address = addressEdit.getText().toString().trim();
        String mobile = mobileEdit.getText().toString().trim();
        String nationality = nationalitySpinner.getSelectedItem().toString();
        String gender = genderSpinner.getSelectedItem().toString();

        if (name.isEmpty() || dob.isEmpty() || address.isEmpty() || mobile.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mobile.matches("\\d{10}")) {
            Toast.makeText(getContext(), "Mobile number must be 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("dob", dob);
        data.put("address", address);
        data.put("mobile", mobile);
        data.put("nationality", nationality);
        data.put("gender", gender);

        if (selectedImageUri != null) {
            StorageReference ref = storage.getReference().child("profile_images/" + uid + ".jpg");
            ref.putFile(selectedImageUri)
                    .continueWithTask(task -> ref.getDownloadUrl())
                    .addOnSuccessListener(uri -> {
                        data.put("profileUrl", uri.toString());
                        updateFirestore(data);
                    });
        } else {
            updateFirestore(data);
        }
    }

    private void updateFirestore(Map<String, Object> data) {
        if (uid == null || uid.isEmpty()) {
            Toast.makeText(getContext(), "User ID is null", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("STProfile", "Updating user: " + uid);
        Log.d("STProfile", "Data: " + data.toString());

        firestore.collection("users").document(uid)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    Log.d("STProfile", "Firestore update SUCCESS");
                    Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("STProfile", "Firestore update FAILED", e);
                    Toast.makeText(getContext(), "Failed to update: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
    private void openGoogleMapsDirections() {
        // Class coordinates
        double classLat = 6.907383306115219;
        double classLng = 79.87109048982776;

        // Create a Google Maps directions URL
        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1" +
                "&destination=" + classLat + "," + classLng +
                "&travelmode=driving");

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//        mapIntent.setPackage("com.google.android.apps.maps");
//
//        if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
//            startActivity(mapIntent);
//        } else {
//            Toast.makeText(getContext(), "Google Maps app not found", Toast.LENGTH_SHORT).show();
//        }
        startActivity(mapIntent);

    }

}