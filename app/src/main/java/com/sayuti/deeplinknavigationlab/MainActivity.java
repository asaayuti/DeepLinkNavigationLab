package com.sayuti.deeplinknavigationlab;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDeepLinkBuilder;
import androidx.navigation.fragment.NavHostFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DeepLinkLab";
    private static final String CHANNEL_ID = "product_notifications";
    private ActivityResultLauncher<String> notificationPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        showProductNotification();
                    } else {
                        Log.w(TAG, "Notification permission denied");
                    }
                }
        );

        setContentView(R.layout.activity_main);

        createNotificationChannel();

        Intent intent = getIntent();

        handleIntent(intent);
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        Log.d(TAG, "onNewIntent dipanggil: " + intent.getData());

        handleIntent(intent);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment == null) {
            throw new IllegalStateException("NavHostFragment tidak ditemukan");
        }

        NavController navController = navHostFragment.getNavController();

        boolean handled = navController.handleDeepLink(intent);
        Log.d(TAG, "Deep link handled: " + handled);
    }

    private void handleIntent(Intent intent) {
        Uri data = intent.getData();

        if (data != null) {
            Log.d(TAG, "Received URI: " + data);
            Log.d(TAG, "Product ID: " + data.getLastPathSegment());
            Log.d(TAG, "Source: " + data.getQueryParameter("source"));
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Product Notifications";
            String description = "Notifikasi terkait produk";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    name,
                    importance
            );

            channel.setDescription(description);

            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);

            Log.d(TAG, "Notification channel created");
        }
    }

    public void requestPermissionAndShowNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED) {

            notificationPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
            );
            return;
        }

        showProductNotification();
    }

    private void showProductNotification() {
        Bundle args = new Bundle();
        args.putString("productId", "888");
        args.putString("source", "notification");

        PendingIntent pendingIntent = new NavDeepLinkBuilder(this)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.productFragment)
                .setArguments(args)
                .createPendingIntent();

        Notification notification =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("Detail Produk")
                        .setContentText("Ketuk untuk membuka produk #888")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .build();

        NotificationManagerCompat.from(this)
                .notify(1001, notification);

        Log.d(TAG, "Product notification posted");
    }
}