package com.example.tuitioninfoapp.activities;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.tuitioninfoapp.R;
import com.example.tuitioninfoapp.fragments.student.MaterialsFragment;
import com.example.tuitioninfoapp.fragments.student.MyAttendanceFragment;
import com.example.tuitioninfoapp.fragments.student.NotificationsFragment;
import com.example.tuitioninfoapp.fragments.student.ProfileFragment;
import com.example.tuitioninfoapp.fragments.student.STProfileFragment;
import com.example.tuitioninfoapp.fragments.student.SubmitAssignmentFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView; // Import this

public class StudentActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // It's good practice to use NavigationBarView.OnItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId(); // Get the ID once

            if (itemId == R.id.nav_attendance) {
                selectedFragment = new MyAttendanceFragment();
            } else if (itemId == R.id.nav_assignments) {
                selectedFragment = new SubmitAssignmentFragment();
            } else if (itemId == R.id.nav_materials) {
                selectedFragment = new MaterialsFragment();
            } else if (itemId == R.id.nav_notifications) {
                selectedFragment = new NotificationsFragment();
            }else if (itemId == R.id.nav_profile) {
                selectedFragment = new STProfileFragment();

            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                return true;
            }

            return false;
        });

        // Load default fragment
        if (savedInstanceState == null) { // Only set default if not restoring from instance state
            bottomNavigationView.setSelectedItemId(R.id.nav_attendance);
        }
    }
}
