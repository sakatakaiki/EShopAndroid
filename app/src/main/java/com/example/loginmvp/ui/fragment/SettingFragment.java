package com.example.loginmvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.loginmvp.R;
import com.example.loginmvp.data.session.UserSession;
import com.example.loginmvp.ui.view.LoginActivity;
import com.google.android.material.button.MaterialButton;

public class SettingFragment extends Fragment {

    private MaterialButton btnAuth;
    private UserSession userSession;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        btnAuth = view.findViewById(R.id.btnAuth);
        userSession = UserSession.getInstance(requireContext());

        updateButton(); // Cập nhật giao diện nút

        btnAuth.setOnClickListener(v -> {
            if (userSession.isLoggedIn()) {
                logout();
            } else {
                login();
            }
        });

        return view;
    }

    private void updateButton() {
        if (userSession.isLoggedIn()) {
            btnAuth.setText("Đăng xuất");
        } else {
            btnAuth.setText("Đăng nhập");
        }
    }

    private void logout() {
        userSession.clearSession();
        updateButton(); // Cập nhật lại nút
        returnToHome(); // Quay về HomeFragment
    }

    private void login() {
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        startActivity(intent);
    }

    private void returnToHome() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
    }
}
