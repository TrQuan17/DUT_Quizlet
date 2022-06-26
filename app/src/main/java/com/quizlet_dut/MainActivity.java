package com.quizlet_dut;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.TextView;

import com.quizlet_dut.fragment.AddFragment;
import com.quizlet_dut.fragment.HomeFragment;
import com.quizlet_dut.fragment.ProfileFragment;
import com.quizlet_dut.fragment.RankFragment;
import com.quizlet_dut.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_SEARCH = 1;
    private static final int FRAGMENT_RANK = 2;
    private static final int FRAGMENT_ADD = 3;
    private static final int FRAGMENT_PROFILE = 4;

    private int mCurrentFragment = FRAGMENT_HOME;

    private DrawerLayout mDrawerLayout;
    private BottomNavigationView mBottomNavigationView;
    private TextView drawerProfileName, drawerProfileText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerProfileName = navigationView.getHeaderView(0).findViewById(R.id.nav_draw_name);
        drawerProfileText = navigationView.getHeaderView(0).findViewById(R.id.nav_draw_img);

        String name = DbQuery.myProfileModel.getName();

        if (name != null) {
            drawerProfileName.setText(name);
            drawerProfileText.setText(name.toUpperCase().substring(0, 1));
        }

        replaceFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        mBottomNavigationView.getMenu().findItem(R.id.bottom_home).setChecked(true);

        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.bottom_home) {
                    openHomeFragment();
                    navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
//                } else if (id == R.id.bottom_search) {
//                    openSearchFragment();
//                    navigationView.getMenu().findItem(R.id.nav_search).setChecked(true);
                } else if (id == R.id.bottom_rank) {
                    openRankFragment();
                    navigationView.getMenu().findItem(R.id.nav_rank).setChecked(true);
                } else if (id == R.id.bottom_add) {
                    openAddFragment();
                    navigationView.getMenu().findItem(R.id.nav_add).setChecked(true);
                } else if (id == R.id.bottom_profile) {
                    openProfileFragment();
                    navigationView.getMenu().findItem(R.id.nav_my_profile).setChecked(true);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home){
            openHomeFragment();
            mBottomNavigationView.getMenu().findItem(R.id.bottom_home).setChecked(true);
//        } else if (id == R.id.nav_search) {
//            openSearchFragment();
//            mBottomNavigationView.getMenu().findItem(R.id.bottom_search).setChecked(true);
        } else if (id == R.id.nav_rank){
            openRankFragment();
            mBottomNavigationView.getMenu().findItem(R.id.bottom_rank).setChecked(true);
        } else if (id == R.id.nav_add) {
            openAddFragment();
            mBottomNavigationView.getMenu().findItem(R.id.bottom_add).setChecked(true);
        } else if (id == R.id.nav_my_profile) {
            openProfileFragment();
            mBottomNavigationView.getMenu().findItem(R.id.bottom_profile).setChecked(true);
        } else if(id == R.id.nav_bookmark) {
            Intent intent = new Intent(MainActivity.this, BookMarksActivity.class);
            startActivity(intent);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openHomeFragment() {
        if (mCurrentFragment != FRAGMENT_HOME) {
            replaceFragment(new HomeFragment());
            mCurrentFragment = FRAGMENT_HOME;
        }
    }

    private void openSearchFragment() {
        if (mCurrentFragment != FRAGMENT_SEARCH) {
            replaceFragment(new SearchFragment());
            mCurrentFragment = FRAGMENT_SEARCH;
        }
    }

    private void openRankFragment() {
        if (mCurrentFragment != FRAGMENT_RANK) {
            replaceFragment(new RankFragment());
            mCurrentFragment = FRAGMENT_RANK;
        }
    }

    private void openAddFragment() {
        if (mCurrentFragment != FRAGMENT_ADD) {
            replaceFragment(new AddFragment());
            mCurrentFragment = FRAGMENT_ADD;
        }
    }

    private void openProfileFragment() {
        if (mCurrentFragment != FRAGMENT_PROFILE) {
            replaceFragment(new ProfileFragment());
            mCurrentFragment = FRAGMENT_PROFILE;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }
}