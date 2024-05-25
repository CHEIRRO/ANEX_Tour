package com.example.kursach.ActionBarActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.kursach.ActionBarActivities.FavoritiesActivityPacket.FavorEntertainmentActivity;
import com.example.kursach.ActionBarActivities.FavoritiesActivityPacket.FavorRoutsActivity;
import com.example.kursach.ActionBarActivities.FavoritiesActivityPacket.FavorSightActivity;
import com.example.kursach.MainActivity;
import com.example.kursach.ProfileSelectionActivity;
import com.example.kursach.R;
import com.google.android.material.navigation.NavigationView;

public class FavoritesActivity extends AppCompatActivity {
    public DrawerLayout drawer;
    public ActionBarDrawerToggle toggle;
    public NavigationView navigationView;
    private Button btnSights;
    private Button btnActivities;
    private Button btnRoutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Избранное");
        }

        drawer = findViewById(R.id.drawer_layout_favor);
        toggle = new ActionBarDrawerToggle(
                FavoritesActivity.this, drawer, R.string.drawer_open, R.string.drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.nav_view_favor);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_favorites) {
                    startActivity(new Intent(FavoritesActivity.this, FavoritesActivity.class));
                } else if (id == R.id.nav_sight) {
                    startActivity(new Intent(FavoritesActivity.this, SightActivity.class));
                } else if (id == R.id.nav_activity) {
                    startActivity(new Intent(FavoritesActivity.this, EntertainmentActivity.class));
                } else if (id == R.id.nav_routs) {
                    startActivity(new Intent(FavoritesActivity.this, RoutsActivity.class));
                } else if (id == R.id.nav_settings) {
                    startActivity(new Intent(FavoritesActivity.this, SearchActivity.class));
                } else if (id == R.id.nav_main) {
                    startActivity(new Intent(FavoritesActivity.this, MainActivity.class));
                } else if (id == R.id.change_profile) {
                    startActivity(new Intent(FavoritesActivity.this, ProfileSelectionActivity.class));
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        btnSights = findViewById(R.id.btn_sights);
        btnActivities = findViewById(R.id.btn_activities);
        btnRoutes = findViewById(R.id.btn_routes);

        btnSights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoritesActivity.this, FavorSightActivity.class);
                startActivity(intent);
            }
        });

        btnActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoritesActivity.this, FavorEntertainmentActivity.class);
                startActivity(intent);
            }
        });

        btnRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoritesActivity.this, FavorRoutsActivity.class);
                startActivity(intent);
            }
        });

        View headerView = navigationView.getHeaderView(0);
        TextView profileTypeTextView = headerView.findViewById(R.id.profile_type);
        if (ProfileSelectionActivity.isAdmin) {
            profileTypeTextView.setText("Профиль: Администратор");
        } else {
            profileTypeTextView.setText("Профиль: Пользователь");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
