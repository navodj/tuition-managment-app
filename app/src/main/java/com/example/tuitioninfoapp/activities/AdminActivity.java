package com.example.tuitioninfoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.tuitioninfoapp.R;
import com.example.tuitioninfoapp.fragments.admin.AssignStudentsFragment;
import com.example.tuitioninfoapp.fragments.admin.ManageUsersFragment;
import com.example.tuitioninfoapp.fragments.admin.ReportsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish(); // Close current dashboard
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}