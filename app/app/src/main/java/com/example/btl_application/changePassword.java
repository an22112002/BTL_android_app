package com.example.btl_application;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class changePassword extends AppCompatActivity {
    ShowDialog showDialog = new ShowDialog();
    Button btnDoi, btnBack;
    EditText etPassword, etPassword2, etLastPassword;
    TextView tv1, tv2, tv3;
    String id, pass;
    boolean lookPassword = false;
    @SuppressLint("ClickableViewAccessibility")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getWidget();
        Intent thisIntent = getIntent();
        id = thisIntent.getStringExtra("id");
        pass = thisIntent.getStringExtra("password");

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
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
            public void afterTextChanged(Editable s) { }
        });

        btnDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doiPassword();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void doiPassword() {
        String password = etPassword.getText().toString().trim();
        String password2 = etPassword2.getText().toString().trim();
        String lastPass = etLastPassword.getText().toString().trim();
        if (lastPass.isEmpty()) {
            showDialog.showAlertDialog(changePassword.this, "Yêu cầu", "Nhập mật khẩu cũ");
            return;
        }
        if (!lastPass.equals(pass)) {
            showDialog.showAlertDialog(changePassword.this, "Yêu cầu", "Nhập sai mật khẩu cũ");
            return;
        }
        if (password.isEmpty()) {
            showDialog.showAlertDialog(changePassword.this, "Yêu cầu", "Nhập mật khẩu mới");
            return;
        }
        boolean[] result = checkCondition(password);
        if (!result[0] || !result[1] || !result[2]) {
            showDialog.showAlertDialog(changePassword.this, "Yêu cầu", "Mật khẩu mới chưa thỏa mãn các yêu cầu đề ra");
            return;
        }
        if (password2.isEmpty()) {
            showDialog.showAlertDialog(changePassword.this, "Yêu cầu", "Nhập xác nhận mật khẩu");
            return;
        }
        if (!password.equals(password2)) {
            etPassword2.setText("");
            showDialog.showAlertDialog(changePassword.this, "Lỗi", "Mật khẩu mới và mật khẩu xác nhận khác nhau. Yêu cầu nhập lại");
            return;
        }
        ApiService.apiService.changePassword(id, password).enqueue(new Callback<SimpleMessage>() {
            @Override
            public void onResponse(Call<SimpleMessage> call, Response<SimpleMessage> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(changePassword.this, "Mật khẩu đã được thay đổi", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    showDialog.showAlertDialog(changePassword.this, "Lỗi", "Mật khẩu chưa thay đổi");
                }
            }
            @Override
            public void onFailure(Call<SimpleMessage> call, Throwable t) {
                showDialog.showAlertDialog(changePassword.this, "Lỗi", "Không nhận được phản hồi. Kiểm tra lại kết nối của bạn");
            }
        });
    }
    private void getWidget() {
        btnDoi = findViewById(R.id.btnDoi_changePass);
        btnBack = findViewById(R.id.btnBack_changePass);
        etPassword = findViewById(R.id.etPassword_changePass);
        etPassword2 = findViewById(R.id.etPassword2_changePass);
        etLastPassword = findViewById(R.id.etLastPassword);
        tv1 = findViewById(R.id.tv1_);
        tv2 = findViewById(R.id.tv2_);
        tv3 = findViewById(R.id.tv3_);
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
}