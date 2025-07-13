package com.example.tuitioninfoapp.activities;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tuitioninfoapp.R;
import com.example.tuitioninfoapp.fragments.teacher.AttendanceFragment;
import com.example.tuitioninfoapp.fragments.teacher.AssignmentsFragment;
import com.example.tuitioninfoapp.fragments.teacher.MaterialsFragment;
import com.example.tuitioninfoapp.fragments.teacher.ProfileFragment;
import com.example.tuitioninfoapp.fragments.teacher.TMaterialFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TeacherActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        bottomNav = findViewById(R.id.teacher_bottom_nav);

        // Default fragment
        loadFragment(new AttendanceFragment());

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            int itemId = item.getItemId(); // Get the item ID

            if (itemId == R.id.nav_attendance) {
                selected = new AttendanceFragment();
            } else if (itemId == R.id.nav_assignments) {
                selected = new AssignmentsFragment();
            } else if (itemId == R.id.nav_materials) {
                selected = new TMaterialFragment();
            } else if (itemId == R.id.nav_profile) {
                selected = new ProfileFragment();
            }

            if (selected != null) {
                loadFragment(selected);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.teacher_fragment_container, fragment)
                .commit();
    }
}
