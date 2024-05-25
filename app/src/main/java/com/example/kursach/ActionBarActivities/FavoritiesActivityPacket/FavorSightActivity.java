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
import com.example.kursach.DB.Sights.Sight;
import com.example.kursach.DB.Sights.SightsDBH;
import com.example.kursach.MainActivity;
import com.example.kursach.ProfileSelectionActivity;
import com.example.kursach.R;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class FavorSightActivity extends AppCompatActivity {
    public DrawerLayout drawer;
    public ActionBarDrawerToggle toggle;
    public NavigationView navigationView;
    private LinearLayout favoritesContainerSights;
    private SightsDBH dbHelperSights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favor_sight);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Избранное: Достопримечательности");
        }

        drawer = findViewById(R.id.drawer_layout_favor_sight);
        toggle = new ActionBarDrawerToggle(
                FavorSightActivity.this, drawer, R.string.drawer_open, R.string.drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.nav_view_favor_sight);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_favorites) {
                    startActivity(new Intent(FavorSightActivity.this, FavoritesActivity.class));
                } else if (id == R.id.nav_sight) {
                    startActivity(new Intent(FavorSightActivity.this, SightActivity.class));
                } else if (id == R.id.nav_activity) {
                    startActivity(new Intent(FavorSightActivity.this, EntertainmentActivity.class));
                } else if (id == R.id.nav_routs) {
                    startActivity(new Intent(FavorSightActivity.this, RoutsActivity.class));
                } else if (id == R.id.nav_settings) {
                    startActivity(new Intent(FavorSightActivity.this, SearchActivity.class));
                } else if (id == R.id.nav_main) {
                    startActivity(new Intent(FavorSightActivity.this, MainActivity.class));
                } else if (id == R.id.change_profile) {
                    startActivity(new Intent(FavorSightActivity.this, ProfileSelectionActivity.class));
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        favoritesContainerSights = findViewById(R.id.favorites_container_sights);

        dbHelperSights = new SightsDBH(this);

        loadFavoritesSights();

        View headerView = navigationView.getHeaderView(0);
        TextView profileTypeTextView = headerView.findViewById(R.id.profile_type);
        if (ProfileSelectionActivity.isAdmin) {
            profileTypeTextView.setText("Профиль: Администратор");
        } else {
            profileTypeTextView.setText("Профиль: Пользователь");
        }
    }

    private void loadFavoritesSights() {
        favoritesContainerSights.removeAllViews();
        List<Sight> favorites = dbHelperSights.getFavoriteSights();
        for (Sight sight : favorites) {
            View sightView = getLayoutInflater().inflate(R.layout.item_scrollview_sight_favor, favoritesContainerSights, false);

            TextView titleTextView = sightView.findViewById(R.id.sight_title);
            TextView dataTextView = sightView.findViewById(R.id.sight_data);
            TextView descriptionTextView = sightView.findViewById(R.id.sight_description);
            ImageButton removeButton = sightView.findViewById(R.id.delete_button_sight);
            removeButton.setTag(sight.getId());

            titleTextView.setText(sight.getTitle());
            dataTextView.setText(sight.getData());
            descriptionTextView.setText(sight.getDescription());

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer sightId = (Integer) v.getTag();
                    if (sightId != null) {
                        dbHelperSights.removeFavoriteSight(sightId);
                        loadFavoritesSights();
                        Toast.makeText(FavorSightActivity.this, "Достопримечательность удалена из избранного", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FavorSightActivity.this, "Ошибка: ID достопримечательности не найден", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            favoritesContainerSights.addView(sightView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle != null && toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
