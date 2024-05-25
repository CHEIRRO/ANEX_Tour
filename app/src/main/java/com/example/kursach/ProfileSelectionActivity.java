package com.example.kursach;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileSelectionActivity extends AppCompatActivity {
    public static boolean isAdmin;
    private ImageButton btnAdmin;
    private ImageButton btnUser;
    private EditText edittextPassword;
    private View btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_selection);

        btnAdmin = findViewById(R.id.btn_admin);
        btnUser = findViewById(R.id.btn_user);
        edittextPassword = findViewById(R.id.edittext_password);
        btnSubmit = findViewById(R.id.btn_submit);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isAdmin = preferences.getBoolean("isAdmin", false);

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edittextPassword.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.VISIBLE);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edittextPassword.getText().toString();
                if (password.equals("11")) {
                    isAdmin = true;
                    SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                    preferences.edit().putBoolean("isAdmin", isAdmin).apply();
                    Intent intent = new Intent(ProfileSelectionActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(ProfileSelectionActivity.this, "Выбран профиль администратора", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileSelectionActivity.this, "Некорректный пароль", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAdmin = false;
                SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                preferences.edit().putBoolean("isAdmin", isAdmin).apply();
                Intent intent = new Intent(ProfileSelectionActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(ProfileSelectionActivity.this, "Профиль выбран", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

