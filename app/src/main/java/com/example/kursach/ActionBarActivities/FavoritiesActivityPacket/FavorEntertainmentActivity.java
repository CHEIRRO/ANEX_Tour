package com.example.kursach.ActionBarActivities.FavoritiesActivityPacket;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.kursach.ActionBarActivities.EntertainmentActivity;
import com.example.kursach.ActionBarActivities.FavoritesActivity;
import com.example.kursach.ActionBarActivities.RoutsActivity;
import com.example.kursach.ActionBarActivities.SearchActivity;
import com.example.kursach.ActionBarActivities.SightActivity;
import com.example.kursach.DB.Entertainments.Entertainment;
import com.example.kursach.DB.Entertainments.EntertainmentsDBH;
import com.example.kursach.MainActivity;
import com.example.kursach.ProfileSelectionActivity;
import com.example.kursach.R;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class FavorEntertainmentActivity extends AppCompatActivity {

    public DrawerLayout drawer;
    public ActionBarDrawerToggle toggle;
    public NavigationView navigationView;
    private LinearLayout favoritesContainerRouts;
    private EntertainmentsDBH dbHelperentertainments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favor_entertainment);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Избранное: Активности");
        }

        drawer = findViewById(R.id.drawer_layout_favor_enter);
        toggle = new ActionBarDrawerToggle(
                FavorEntertainmentActivity.this, drawer, R.string.drawer_open, R.string.drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.nav_view_favor_enter);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_favorites) {
                    startActivity(new Intent(FavorEntertainmentActivity.this, FavoritesActivity.class));
                } else if (id == R.id.nav_sight) {
                    startActivity(new Intent(FavorEntertainmentActivity.this, SightActivity.class));
                } else if (id == R.id.nav_activity) {
                    startActivity(new Intent(FavorEntertainmentActivity.this, EntertainmentActivity.class));
                } else if (id == R.id.nav_routs) {
                    startActivity(new Intent(FavorEntertainmentActivity.this, RoutsActivity.class));
                } else if (id == R.id.nav_settings) {
                    startActivity(new Intent(FavorEntertainmentActivity.this, SearchActivity.class));
                } else if (id == R.id.nav_main) {
                    startActivity(new Intent(FavorEntertainmentActivity.this, MainActivity.class));
                } else if (id == R.id.change_profile) {
                    startActivity(new Intent(FavorEntertainmentActivity.this, ProfileSelectionActivity.class));
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        favoritesContainerRouts = findViewById(R.id.favorites_container_enter);

        dbHelperentertainments = new EntertainmentsDBH(this);

        loadFavoritesRouts();

        View headerView = navigationView.getHeaderView(0);
        TextView profileTypeTextView = headerView.findViewById(R.id.profile_type);
        if (ProfileSelectionActivity.isAdmin) {
            profileTypeTextView.setText("Профиль: Администратор");
        } else {
            profileTypeTextView.setText("Профиль: Пользователь");
        }
    }

    private void loadFavoritesRouts() {
        favoritesContainerRouts.removeAllViews();
        List<Entertainment> enters = dbHelperentertainments.getFavoriteEntertainments();
        for (Entertainment enter : enters) {
            View routView = getLayoutInflater().inflate(R.layout.item_scrollview_entertainment_favor, favoritesContainerRouts, false);

            TextView titleTextView = routView.findViewById(R.id.enter_title);
            TextView descriptionTextView = routView.findViewById(R.id.enter_description);
            ImageButton removeButton = routView.findViewById(R.id.delete_button_enter);
            removeButton.setTag(enter.getId());

            titleTextView.setText(enter.getTitle());
            descriptionTextView.setText(enter.getDescription());

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer routeId = (Integer) v.getTag();
                    if (routeId != null) {
                        dbHelperentertainments.removeFavorite1(routeId);
                        loadFavoritesRouts();
                        Toast.makeText(FavorEntertainmentActivity.this, "Маршрут удален из избранного", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FavorEntertainmentActivity.this, "Ошибка: ID маршрута не найден", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            favoritesContainerRouts.addView(routView);
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