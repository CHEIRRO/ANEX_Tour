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

import com.example.kursach.ActionBarActivities.FavoritiesActivityPacket.FavorSightActivity;
import com.example.kursach.DB.Entertainments.BottomSheetEntertainment;
import com.example.kursach.DB.Entertainments.Entertainment;
import com.example.kursach.DB.Entertainments.EntertainmentsDBH;
import com.example.kursach.MainActivity;
import com.example.kursach.ProfileSelectionActivity;
import com.example.kursach.R;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class EntertainmentActivity extends AppCompatActivity implements BottomSheetEntertainment.BottomSheetListener {

    public DrawerLayout drawer;
    public ActionBarDrawerToggle toggle;
    public NavigationView navigationView;
    private EntertainmentsDBH dbHelper;
    private LinearLayout contactsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Активности");
        }
        drawer = findViewById(R.id.drawer_layout_entertainment);
        toggle = new ActionBarDrawerToggle(
                EntertainmentActivity.this, drawer, R.string.drawer_open, R.string.drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.nav_view_entertainment);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_favorites) {
                startActivity(new Intent(EntertainmentActivity.this, FavoritesActivity.class));
            } else if (id == R.id.nav_sight) {
                startActivity(new Intent(EntertainmentActivity.this, SightActivity.class));
            } else if (id == R.id.nav_activity) {
                startActivity(new Intent(EntertainmentActivity.this, EntertainmentActivity.class));
            } else if (id == R.id.nav_routs) {
                startActivity(new Intent(EntertainmentActivity.this, RoutsActivity.class));
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(EntertainmentActivity.this, SearchActivity.class));
            } else if (id == R.id.nav_main) {
                startActivity(new Intent(EntertainmentActivity.this, MainActivity.class));
            } else if (id == R.id.change_profile) {
                startActivity(new Intent(EntertainmentActivity.this, ProfileSelectionActivity.class));
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        contactsContainer = findViewById(R.id.contacts_container);

        dbHelper = new EntertainmentsDBH(this);
        refreshEntertainmentsList();

        View headerView = navigationView.getHeaderView(0);
        TextView profileTypeTextView = headerView.findViewById(R.id.profile_type);
        if (ProfileSelectionActivity.isAdmin) {
            profileTypeTextView.setText("Профиль: Администратор");
        } else {
            profileTypeTextView.setText("Профиль: Пользователь");
        }
    }

    private void refreshEntertainmentsList() {
        contactsContainer.removeAllViews();
        List<Entertainment> enters = dbHelper.getAllEntertainments();
        for (Entertainment enter : enters) {
            View routView = getLayoutInflater().inflate(R.layout.item_scrollview_entertainment, contactsContainer, false);

            TextView titleTextView = routView.findViewById(R.id.enter_title);
            TextView descriptionTextView = routView.findViewById(R.id.enter_description);
            RadioButton addToFavorites = routView.findViewById(R.id.radio_button_favorite);
            ImageButton deleteButton = routView.findViewById(R.id.delete_button_enter);

            titleTextView.setText(enter.getTitle());
            descriptionTextView.setText(enter.getDescription());

            addToFavorites.setOnClickListener(v -> {
                addToFavorites(enter);
                Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show();
            });
            if(!ProfileSelectionActivity.isAdmin) {
                deleteButton.setVisibility(View.GONE);
            } else {
                deleteButton.setOnClickListener(v -> {
                    deleteEntertainment(enter);
                });
            }
            contactsContainer.addView(routView);
        }
    }

    private void deleteEntertainment(Entertainment enter) {
        dbHelper.deleteEntertainment(enter.getId());
        refreshEntertainmentsList();
        Toast.makeText(this, "Удалено успешно", Toast.LENGTH_SHORT).show();
    }

    private void addToFavorites(Entertainment enter) {
        dbHelper.addEntertainmentToFavorites(enter);
    }

    @Override
    public void onButtonClicked(String title, String description) {
        if (dbHelper.addEntertainment(new Entertainment(0, title, description))) {
            refreshEntertainmentsList();
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
            BottomSheetEntertainment bottomSheetFragment = new BottomSheetEntertainment();
            bottomSheetFragment.setBottomSheetListener(this);
            bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragment");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
