package com.example.kursach.ActionBarActivities;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.kursach.DB.Sights.BottomSheetSight;
import com.example.kursach.DB.Sights.Sight;
import com.example.kursach.DB.Sights.SightsDBH;
import com.example.kursach.MainActivity;
import com.example.kursach.ProfileSelectionActivity;
import com.example.kursach.R;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class SightActivity extends AppCompatActivity  implements BottomSheetSight.BottomSheetListener{
    public DrawerLayout drawer;
    public ActionBarDrawerToggle toggle;
    public NavigationView navigationView;
    private SightsDBH dbHelper;
    private LinearLayout contactsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sight);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Достопримечательности");
        }

        ////////////////////////////////////////////NavigationBar и BottomSheet////////////////////////////////////////////

        drawer = findViewById(R.id.drawer_layout_sight);
        toggle = new ActionBarDrawerToggle(
                SightActivity.this, drawer, R.string.drawer_open, R.string.drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView = findViewById(R.id.nav_view_sight);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_favorites) {
                    startActivity(new Intent(SightActivity.this, FavoritesActivity.class));
                } else if (id == R.id.nav_sight) {
                    startActivity(new Intent(SightActivity.this, SightActivity.class));
                } else if (id == R.id.nav_activity) {
                    startActivity(new Intent(SightActivity.this, EntertainmentActivity.class));
                } else if (id == R.id.nav_routs) {
                    startActivity(new Intent(SightActivity.this, RoutsActivity.class));
                } else if (id == R.id.nav_settings) {
                    startActivity(new Intent(SightActivity.this, SearchActivity.class));
                } else if (id == R.id.nav_main) {
                    startActivity(new Intent(SightActivity.this, MainActivity.class));
                } else if (id == R.id.change_profile) {
                    startActivity(new Intent(SightActivity.this, ProfileSelectionActivity.class));
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        contactsContainer = findViewById(R.id.contacts_container);

        dbHelper = new SightsDBH(this);
        refreshSightsList();

        View headerView = navigationView.getHeaderView(0);
        TextView profileTypeTextView = headerView.findViewById(R.id.profile_type);
        if (ProfileSelectionActivity.isAdmin) {
            profileTypeTextView.setText("Профиль: Администратор");
        } else {
            profileTypeTextView.setText("Профиль: Пользователь");
        }
    }

    private void refreshSightsList() {
        contactsContainer.removeAllViews();
        List<Sight> sights = dbHelper.getAllSights();
        for (Sight sight : sights) {
            View sightView = getLayoutInflater().inflate(R.layout.item_scrollview_sight, contactsContainer, false);

            TextView titleTextView = sightView.findViewById(R.id.sight_title);
            TextView dataTextView = sightView.findViewById(R.id.sight_data);
            TextView descriptionTextView = sightView.findViewById(R.id.sight_description);
            RadioButton addToFavorites = sightView.findViewById(R.id.radio_button_favorite);
            ImageButton deleteButton = sightView.findViewById(R.id.delete_button_sight);

            titleTextView.setText(sight.getTitle());
            dataTextView.setText(sight.getData());
            descriptionTextView.setText(sight.getDescription());

            addToFavorites.setOnClickListener(v -> {
                addToFavorites(sight);
                Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show();
            });

            if(!ProfileSelectionActivity.isAdmin) {
                deleteButton.setVisibility(View.GONE);
            } else {
                deleteButton.setOnClickListener(v -> {
                    deleteSight(sight);
                });
            }
            contactsContainer.addView(sightView);
        }
    }
    private void deleteSight(Sight sight){
        dbHelper.deleteSight(sight.getId());
        refreshSightsList();
        Toast.makeText(this, "Удалено успешно", Toast.LENGTH_SHORT).show();
    }

    private void addToFavorites(Sight sight) {
        dbHelper.addSightToFavorites(sight);
    }

    @Override
    public void onButtonClicked(String title, String data, String description) {
        if (dbHelper.addSight(new Sight(0, title, data, description))) {
            refreshSightsList();
            Toast.makeText(this, "Сохранено успешно", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sight_menu, menu);

        MenuItem addItem = menu.findItem(R.id.action_add);

        if (!ProfileSelectionActivity.isAdmin) {
            addItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == R.id.action_add) {
            BottomSheetSight bottomSheetFragment = new BottomSheetSight();
            bottomSheetFragment.setBottomSheetListener(this);
            bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragment");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}