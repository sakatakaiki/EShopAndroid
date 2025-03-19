package com.example.loginmvp.ui.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.loginmvp.R;
import com.example.loginmvp.data.network.AuthResponse;
import com.example.loginmvp.data.session.UserSession;
import com.example.loginmvp.ui.presenter.LoginContract;
import com.example.loginmvp.ui.presenter.LoginPresenter;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnGoToRegister;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        presenter = new LoginPresenter(this);

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                presenter.login(email, password);
            } else {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu!", Toast.LENGTH_SHORT).show();
            }
        });
        btnGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onLoginSuccess(AuthResponse response) {
        if (response != null) {
            UserSession session = UserSession.getInstance(this);
            session.saveUserSession((long) response.getId());

            Log.d("Login", "User ID lưu: " + session.getUserId());
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Lỗi: User không tồn tại!", Toast.LENGTH_SHORT).show();
        }
    }


//    private void saveUserSession(int userId) {
//        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("user_id", userId);
//        editor.apply();
//    }



    @Override
    public void onLoginError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}