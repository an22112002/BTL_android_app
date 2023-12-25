package com.example.btl_application;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_application.ApiConnect.ApiService;
import com.example.btl_application.Model.SimpleMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    ShowDialog showDialog = new ShowDialog();
    EditText etAccount, etPassword, etPassword2;
    Button btnTao, btnBack;
    TextView tv1, tv2, tv3;
    private boolean lookPassword = false;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getWidget();
        btnTao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taoTaiKhoan();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                seePassword(event);
                return false;
            }
        });
        etPassword2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                seePassword(event);
                return false;
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean[] allow = checkCondition(s.toString());
                Drawable satisfied = getResources().getDrawable(R.drawable.ic_satisfied);
                Drawable not_satisfied = getResources().getDrawable(R.drawable.ic_not_satisfied);
                if (allow[0]) {
                    tv1.setCompoundDrawablesRelativeWithIntrinsicBounds(satisfied, null, null, null);
                } else {
                    tv1.setCompoundDrawablesRelativeWithIntrinsicBounds(not_satisfied, null, null, null);
                }
                if (allow[1]) {
                    tv2.setCompoundDrawablesRelativeWithIntrinsicBounds(satisfied, null, null, null);
                } else {
                    tv2.setCompoundDrawablesRelativeWithIntrinsicBounds(not_satisfied, null, null, null);
                }
                if (allow[2]) {
                    tv3.setCompoundDrawablesRelativeWithIntrinsicBounds(satisfied, null, null, null);
                } else {
                    tv3.setCompoundDrawablesRelativeWithIntrinsicBounds(not_satisfied, null, null, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void taoTaiKhoan() {
        String account = etAccount.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String password2 = etPassword2.getText().toString().trim();
        if (account.isEmpty()) {
            showDialog.showAlertDialog(this, "Yêu cầu", "Nhập tên tài khoản");
            return;
        }
        if (password.isEmpty()) {
            showDialog.showAlertDialog(this, "Yêu cầu", "Nhập mật khẩu");
            return;
        }
        boolean[] result = checkCondition(etPassword.getText().toString());
        if (!result[0] || !result[1] || !result[2]) {
            showDialog.showAlertDialog(this, "Yêu cầu", "Mật khẩu chưa thỏa mãn các yêu cầu đề ra");
            return;
        }
        if (password2.isEmpty()) {
            showDialog.showAlertDialog(this, "Yêu cầu", "Nhập xác nhận mật khẩu");
            return;
        }
        if (!password.equals(password2)) {
            etPassword2.setText("");
            showDialog.showAlertDialog(this, "Lỗi", "Mật khẩu và mật khẩu xác nhận khác nhau. Yêu cầu nhập lại");
            return;
        }
        ApiService.apiService.createAccount(account, password).enqueue(new Callback<SimpleMessage>() {
            @Override
            public void onResponse(Call<SimpleMessage> call, Response<SimpleMessage> response) {
                if (response.isSuccessful()) {
                    SimpleMessage sm = response.body();
                    Toast.makeText(SignInActivity.this, "Tạo thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        Gson gson = new Gson();
                        JsonObject errorBody = gson.fromJson(response.errorBody().charStream(), JsonObject.class);
                        String errorMessage = errorBody.get("message").getAsString();
                        showDialog.showAlertDialog(SignInActivity.this, "Thông báo", errorMessage);
                    } catch (Exception e) {
                        showDialog.showAlertDialog(SignInActivity.this, "Lỗi", e.toString());
                    }
                }
            }
            @Override
            public void onFailure(Call<SimpleMessage> call, Throwable t) {
                showDialog.showAlertDialog(SignInActivity.this, "Lỗi", "Không nhận được phản hồi. Kiểm tra lại kết nối của bạn");
            }
        });
    }
    private void seePassword(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int drawableEndArea = etPassword.getRight() - etPassword.getCompoundDrawables()[2].getBounds().width();
            if (event.getRawX() >= drawableEndArea) {
                if (lookPassword) {
                    Drawable icon_eye = getResources().getDrawable(R.drawable.ic_eye);
                    lookPassword = false;
                    etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, icon_eye, null);
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etPassword2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, icon_eye, null);
                    etPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    Drawable icon_eye = getResources().getDrawable(R.drawable.ic_eye_off);
                    lookPassword = true;
                    etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, icon_eye, null);
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    etPassword2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, icon_eye, null);
                    etPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                }
            }
        }
    }
    private boolean[] checkCondition(String s) {
        boolean[] result = new boolean[3];
        String characterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numberChar = "0123456789";
        String specialChar = "!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?`~";

        if (s.length() >= 10) { result[0] = true; }
        if (check(s, characterChar) && check(s, numberChar)) { result[1] = true; }
        if (check(s, specialChar)) { result[2] = true; }
        return result;
    }
    private boolean check(String s, String c) {
        for (char ch: c.toCharArray()) {
            if (s.contains(String.valueOf(ch))) {
                return true;
            }
        }
        return false;
    }
    private void getWidget() {
        etAccount = findViewById(R.id.etAccount_signin);
        etPassword = findViewById(R.id.etPassword_signin);
        etPassword2 = findViewById(R.id.etPassword2_signin);
        btnTao = findViewById(R.id.btnTao);
        btnBack = findViewById(R.id.btnBack_signin);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
    }
}