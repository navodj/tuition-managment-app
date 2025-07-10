package com.example.tuitioninfoapp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.tuitioninfoapp.R;
import com.example.tuitioninfoapp.fragments.admin.AssignStudentsFragment;
import com.example.tuitioninfoapp.fragments.admin.ManageUsersFragment;
import com.example.tuitioninfoapp.fragments.admin.ReportsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Set up navigation listener
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_manage_users) {
                loadFragment(new ManageUsersFragment());
                return true;
            } else if (itemId == R.id.nav_assign_students) {
                loadFragment(new AssignStudentsFragment());
                return true;
            } else if (itemId == R.id.nav_reports) {
                loadFragment(new ReportsFragment());
                return true;
            }
            return false;
        });

        // Load default fragment
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_manage_users); // This triggers the listener
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}