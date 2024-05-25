package com.example.kursach.ActionBarActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.kursach.DB.Entertainments.Entertainment;
import com.example.kursach.DB.Entertainments.EntertainmentsDBH;
import com.example.kursach.DB.Routs.Rout;
import com.example.kursach.DB.Routs.RoutsDBH;
import com.example.kursach.DB.Sights.Sight;
import com.example.kursach.DB.Sights.SightsDBH;
import com.example.kursach.MainActivity;
import com.example.kursach.ProfileSelectionActivity;
import com.example.kursach.R;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    public DrawerLayout drawer;
    public ActionBarDrawerToggle toggle;
    public NavigationView navigationView;

    private Spinner spinnerSection;
    private EditText editTextSearchQuery;
    private Button buttonSearch;
    private LinearLayout resultsContainer;

    private SightsDBH sightsDBH;
    private EntertainmentsDBH entertainmentsDBH;
    private RoutsDBH routsDBH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Настройки");
        }

        ////////////////////////////////////////////NavigationBar////////////////////////////////////////////

        drawer = findViewById(R.id.drawer_layout_setting);
        toggle = new ActionBarDrawerToggle(
                SearchActivity.this, drawer, R.string.drawer_open, R.string.drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.nav_view_setting);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_favorites) {
                    startActivity(new Intent(SearchActivity.this, FavoritesActivity.class));
                } else if (id == R.id.nav_sight) {
                    startActivity(new Intent(SearchActivity.this, SightActivity.class));
                } else if (id == R.id.nav_activity) {
                    startActivity(new Intent(SearchActivity.this, EntertainmentActivity.class));
                } else if (id == R.id.nav_routs) {
                    startActivity(new Intent(SearchActivity.this, RoutsActivity.class));
                } else if (id == R.id.nav_settings) {
                    startActivity(new Intent(SearchActivity.this, SearchActivity.class));
                } else if (id == R.id.nav_main) {
                    startActivity(new Intent(SearchActivity.this, MainActivity.class));
                } else if (id == R.id.change_profile) {
                    startActivity(new Intent(SearchActivity.this, ProfileSelectionActivity.class));
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Initialize UI elements
        spinnerSection = findViewById(R.id.spinner_section);
        editTextSearchQuery = findViewById(R.id.edittext_search_query);
        buttonSearch = findViewById(R.id.button_search);
        resultsContainer = findViewById(R.id.results_container);

        // Initialize database helpers
        sightsDBH = new SightsDBH(this);
        entertainmentsDBH = new EntertainmentsDBH(this);
        routsDBH = new RoutsDBH(this);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
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

    private void performSearch() {
        String query = editTextSearchQuery.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(this, "Введите поисковой запрос", Toast.LENGTH_SHORT).show();
            return;
        }

        resultsContainer.removeAllViews();
        String selectedSection = spinnerSection.getSelectedItem().toString();

        if (selectedSection.equals("Достопримечательности")) {
            List<Sight> sights = sightsDBH.searchSightsByTitle(query);
            for (Sight sight : sights) {
                addResultView(sight.getTitle(), sight.getDescription(), sight.getData(), null);
            }
        } else if (selectedSection.equals("Активности")) {
            List<Entertainment> entertainments = entertainmentsDBH.searchEntertainmentsByTitle(query);
            for (Entertainment entertainment : entertainments) {
                addResultView(entertainment.getTitle(), entertainment.getDescription(), null, null);
            }
        } else if (selectedSection.equals("Маршруты")) {
            List<Rout> routs = routsDBH.searchRoutsByTitle(query);
            for (Rout rout : routs) {
                addResultView(rout.getTitle(), rout.getDescription(), null, rout.getLength());
            }
        }
    }

    private void addResultView(String title, String description, String data, String length) {
        View resultView = getLayoutInflater().inflate(R.layout.item_result, resultsContainer, false);

        TextView titleTextView = resultView.findViewById(R.id.result_title);
        TextView descriptionTextView = resultView.findViewById(R.id.result_description);
        TextView dataTextView = resultView.findViewById(R.id.result_data);
        TextView lengthTextView = resultView.findViewById(R.id.result_length);

        titleTextView.setText(title);
        descriptionTextView.setText(description);

        if (data != null) {
            dataTextView.setText("Дата: " + data);
            dataTextView.setVisibility(View.VISIBLE);
        }

        if (length != null) {
            lengthTextView.setText("Длина: " + length);
            lengthTextView.setVisibility(View.VISIBLE);
        }

        resultsContainer.addView(resultView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
