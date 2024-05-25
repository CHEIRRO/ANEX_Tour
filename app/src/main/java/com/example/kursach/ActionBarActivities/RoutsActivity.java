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

import com.example.kursach.DB.Routs.BottomSheetRout;
import com.example.kursach.DB.Routs.Rout;
import com.example.kursach.DB.Routs.RoutsDBH;
import com.example.kursach.MainActivity;
import com.example.kursach.ProfileSelectionActivity;
import com.example.kursach.R;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class RoutsActivity extends AppCompatActivity implements BottomSheetRout.BottomSheetListener {
    public DrawerLayout drawer;
    public ActionBarDrawerToggle toggle;
    public NavigationView navigationView;
    private RoutsDBH dbHelper;
    private LinearLayout contactsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routs);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Маршруты");
        }
        drawer = findViewById(R.id.drawer_layout_routs);
        toggle = new ActionBarDrawerToggle(
                RoutsActivity.this, drawer, R.string.drawer_open, R.string.drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.nav_view_routs);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_favorites) {
                    startActivity(new Intent(RoutsActivity.this, FavoritesActivity.class));
                } else if (id == R.id.nav_sight) {
                    startActivity(new Intent(RoutsActivity.this, SightActivity.class));
                } else if (id == R.id.nav_activity) {
                    startActivity(new Intent(RoutsActivity.this, EntertainmentActivity.class));
                } else if (id == R.id.nav_routs) {
                    startActivity(new Intent(RoutsActivity.this, RoutsActivity.class));
                } else if (id == R.id.nav_settings) {
                    startActivity(new Intent(RoutsActivity.this, SearchActivity.class));
                } else if (id == R.id.nav_main) {
                    startActivity(new Intent(RoutsActivity.this, MainActivity.class));
                } else if (id == R.id.change_profile) {
                    startActivity(new Intent(RoutsActivity.this, ProfileSelectionActivity.class));
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        contactsContainer = findViewById(R.id.contacts_container);

        dbHelper = new RoutsDBH(this);
        refreshRoutsList();
    }

    private void refreshRoutsList() {
        contactsContainer.removeAllViews();
        List<Rout> routs = dbHelper.getAllRouts();
        for (Rout rout : routs) {
            View routView = getLayoutInflater().inflate(R.layout.item_scrollview_rout, contactsContainer, false);

            TextView titleTextView = routView.findViewById(R.id.sight_title);
            TextView dataTextView = routView.findViewById(R.id.sight_lenght);
            TextView descriptionTextView = routView.findViewById(R.id.sight_description);
            RadioButton addToFavorites = routView.findViewById(R.id.radio_button_favorite);
            ImageButton deleteButton = routView.findViewById(R.id.delete_button_rout);

            titleTextView.setText(rout.getTitle());
            dataTextView.setText(rout.getLength());
            descriptionTextView.setText(rout.getDescription());

            addToFavorites.setOnClickListener(v -> {
                addToFavorites(rout);
                Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show();
            });

            if(!ProfileSelectionActivity.isAdmin) {
                deleteButton.setVisibility(View.GONE);
            } else {
                deleteButton.setOnClickListener(v -> {
                    deleteRout(rout);
                });
            }
            contactsContainer.addView(routView);
        }

        View headerView = navigationView.getHeaderView(0);
        TextView profileTypeTextView = headerView.findViewById(R.id.profile_type);
        if (ProfileSelectionActivity.isAdmin) {
            profileTypeTextView.setText("Профиль: Администратор");
        } else {
            profileTypeTextView.setText("Профиль: Пользователь");
        }
    }
    private void deleteRout(Rout rout){
        dbHelper.deleteRout(rout.getId());
        refreshRoutsList();
        Toast.makeText(this, "Удалено успешно", Toast.LENGTH_SHORT).show();
    }
    private void addToFavorites(Rout rout) {
        dbHelper.addRoutToFavorites(rout);
    }

    @Override
    public void onButtonClicked(String title, String length, String description) {

         if (dbHelper.addRout(new Rout(0, title, length, description))) {
             refreshRoutsList();
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
            BottomSheetRout bottomSheetFragment = new BottomSheetRout();
            bottomSheetFragment.setBottomSheetListener(this);
            bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragment");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
