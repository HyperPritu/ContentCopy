package com.pritamdey.contentcopy;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;

import com.google.gson.Gson;
import com.pritamdey.contentcopy.databinding.ActivityMainBinding;
import com.pritamdey.contentcopy.databinding.LayoutCardCategoryBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private List<Text> textList;
    private TextAdapter textAdapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onResume() {
        super.onResume();
        loadTexts();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        boolean hasAccessibilityPermission = accessibilityManager.isEnabled();

        if (!hasAccessibilityPermission) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Accessibility Permission Required")
                    .setMessage("This app needs accessibility permission to function properly. Would you like to grant the permission now?")
                    .setPositiveButton("Grant", (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent);
                    })
                    .setNegativeButton("Deny", (dialog, which) -> {
                        finish();
                    })
                    .show();
        }


        sharedPreferences = getApplicationContext().getSharedPreferences("Text_Preferences", MODE_PRIVATE);

        textList = new ArrayList<>();

        buildRecycler();
        loadTexts();

        binding.swiper.setOnRefreshListener(()->{
            loadTexts();
            binding.swiper.setRefreshing(false);
        });

    }

    private void buildRecycler() {
        textAdapter = new TextAdapter(this, textList);
        binding.recyclerCategory.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerCategory.setAdapter(textAdapter);

    }


    private void loadTexts() {
        textList.clear();
        Map<String, ?> allEntries = sharedPreferences.getAll();
        Gson gson = new Gson();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String textJson = entry.getValue().toString();
            Text text = gson.fromJson(textJson, Text.class);
            textList.add(text);
        }

        Collections.sort(textList, (o1, o2) -> Long.compare(o2.getTimestamp(), o1.getTimestamp()));

        textAdapter.notifyDataSetChanged();
    }
}