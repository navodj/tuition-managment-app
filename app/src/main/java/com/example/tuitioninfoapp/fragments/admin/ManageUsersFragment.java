package com.example.tuitioninfoapp.fragments.admin;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tuitioninfoapp.R;
import com.example.tuitioninfoapp.adapters.UserAdapter;
import com.example.tuitioninfoapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private FirebaseFirestore db;

    public ManageUsersFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_users, container, false);

        Button addUserButton = view.findViewById(R.id.addUserButton);
        recyclerView = view.findViewById(R.id.userRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this::showUserOptionsDialog); // Must pass listener!
        recyclerView.setAdapter(userAdapter);

        db = FirebaseFirestore.getInstance();
        loadUsers();
        addUserButton.setOnClickListener(v -> {
            View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_user, null);

            EditText etName = dialogView.findViewById(R.id.etName);
            EditText etEmail = dialogView.findViewById(R.id.etEmail);
            EditText etPassword = dialogView.findViewById(R.id.etPassword);
            Spinner spinnerRole = dialogView.findViewById(R.id.spinnerRole);

            new AlertDialog.Builder(getContext())
                    .setTitle("Add New User")
                    .setView(dialogView)
                    .setPositiveButton("Create", (dialog, which) -> {
                        String name = etName.getText().toString().trim();
                        String email = etEmail.getText().toString().trim();
                        String password = etPassword.getText().toString().trim();
                        String role = spinnerRole.getSelectedItem().toString();

                        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                            createUserInFirebase(name, email, password, role);
                        } else {
                            Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        return view;
    }


    private void loadUsers() {
        db.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    userList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        User user = doc.toObject(User.class);
                        userList.add(user);
                    }
                    userAdapter.notifyDataSetChanged();
                });
    }
    private void createUserInFirebase(String name, String email, String password, String role) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = task.getResult().getUser().getUid();
                        User user = new User(uid, name, email, role);

                        db.collection("users").document(uid)
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getContext(), "User created", Toast.LENGTH_SHORT).show();
                                    loadUsers(); // refresh list
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Firestore error", Toast.LENGTH_SHORT).show();
                                });

                    } else {
                        Toast.makeText(getContext(), "Auth failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void showEditDeleteDialog(User user) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_user, null);

        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etEmail = dialogView.findViewById(R.id.etEmail);
        EditText etPassword = dialogView.findViewById(R.id.etPassword);
        Spinner spinnerRole = dialogView.findViewById(R.id.spinnerRole);

        // Prefill user data
        etName.setText(user.getName());
        etEmail.setText(user.getEmail());
        etEmail.setEnabled(false);  // don't allow editing email
        etPassword.setVisibility(View.GONE); // hide password
        if (user.getRole().equals("admin")) spinnerRole.setSelection(0);
        else if (user.getRole().equals("teacher")) spinnerRole.setSelection(1);
        else spinnerRole.setSelection(2);

        new AlertDialog.Builder(getContext())
                .setTitle("Edit or Delete User")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    String newName = etName.getText().toString().trim();
                    String newEmail = etEmail.getText().toString().trim();
                    String newRole = spinnerRole.getSelectedItem().toString();

                    if (!newName.isEmpty()) {
                        db.collection("users").document(user.getUid())
                                .update("name", newName,"email", newEmail, "role", newRole)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(getContext(), "User updated", Toast.LENGTH_SHORT).show();
                                    loadUsers();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Failed to update user", Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .setNegativeButton("Delete", (dialog, which) -> {
                    db.collection("users").document(user.getUid())
                            .delete()
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(getContext(), "User deleted", Toast.LENGTH_SHORT).show();
                                loadUsers();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Failed to delete user", Toast.LENGTH_SHORT).show();
                            });
                })
                .setNeutralButton("Cancel", null)
                .show();
    }
    private void showUserOptionsDialog(User user) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_user, null);

        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etEmail = dialogView.findViewById(R.id.etEmail);
        EditText etPassword = dialogView.findViewById(R.id.etPassword);
        Spinner spinnerRole = dialogView.findViewById(R.id.spinnerRole);

        // Pre-fill user data
        etName.setText(user.getName());
        etEmail.setText(user.getEmail());
        etEmail.setEnabled(false); // avoid changing email (Firebase limitation)
        spinnerRole.setSelection(user.getRole().equals("teacher") ? 1 : user.getRole().equals("student") ? 2 : 0);

        new AlertDialog.Builder(getContext())
                .setTitle("Edit or Delete User")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    String newName = etName.getText().toString().trim();
                    String newRole = spinnerRole.getSelectedItem().toString();

                    if (!newName.isEmpty()) {
                        updateUser(user.getUid(), newName, newRole);
                    } else {
                        Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Delete", (dialog, which) -> {
                    deleteUser(user.getUid());
                })
                .setNeutralButton("Cancel", null)
                .show();
    }
    private void updateUser(String uid, String newName, String newRole) {
        db.collection("users").document(uid)
                .update("name", newName,  "role", newRole)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "User updated", Toast.LENGTH_SHORT).show();
                    loadUsers();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Update failed", Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteUser(String uid) {
        db.collection("users").document(uid)
                .delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "User deleted", Toast.LENGTH_SHORT).show();
                    loadUsers();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Delete failed", Toast.LENGTH_SHORT).show();
                });
    }



}




