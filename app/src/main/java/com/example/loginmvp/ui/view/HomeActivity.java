package com.example.loginmvp.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.loginmvp.R;
import com.example.loginmvp.ui.fragment.ChatFragment;
import com.example.loginmvp.ui.fragment.FavoriteFragment;
import com.example.loginmvp.ui.fragment.HomeFragment;
import com.example.loginmvp.ui.fragment.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView; // Thêm biến

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        // Gán giá trị cho bottomNavigationView trước khi sử dụng
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Không cần inflateMenu vì menu đã được khai báo trong XML
        // bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu); // CÓ THỂ BỎ DÒNG NÀY

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_favorite) {
                selectedFragment = new FavoriteFragment();
            } else if (itemId == R.id.nav_chat) {
                selectedFragment = new ChatFragment();
            } else if (itemId == R.id.nav_setting) {
                selectedFragment = new SettingFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });


        handleIntent(getIntent(), savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Cập nhật Intent mới nhất
        handleIntent(intent, null); // Xử lý Intent mới
    }

    private void handleIntent(Intent intent, Bundle savedInstanceState) {
        boolean openChat = intent.getBooleanExtra("openChat", false);
        Log.d("HomeActivity", "Handling intent with openChat = " + openChat);

        if (openChat) {
            openChatFragment();
            bottomNavigationView.post(() -> bottomNavigationView.setSelectedItemId(R.id.nav_chat));
        } else if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
    }
    private void openChatFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ChatFragment())
                .commit();
    }
}
