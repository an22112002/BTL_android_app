package com.example.btl_application;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_application.ApiConnect.ApiService;
import com.example.btl_application.Model.AccessResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    ShowDialog showDialog = new ShowDialog();
    TextView txtAccountCreate;
    EditText etAccount, etPassword;
    Button btnAccess;
    public boolean lookPassword = false;
    public String saveFile = "lastAccess.txt";
    public String lastAccess;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWidget();
        lastAccess = readFile();
        if (lastAccess != null)
            etAccount.setText(lastAccess);
        btnAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAccess();
            }
        });
        txtAccountCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent taoAccount = new Intent(LoginActivity.this, SignInActivity.class);
                startActivity(taoAccount);
            }
        });
        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int drawableEndArea = etPassword.getRight() - etPassword.getCompoundDrawables()[2].getBounds().width();
                    if (event.getRawX() >= drawableEndArea) {
                        if (lookPassword) {
                            Drawable icon_eye = getResources().getDrawable(R.drawable.ic_eye);
                            etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, icon_eye, null);
                            lookPassword = false;
                            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        } else {
                            Drawable icon_eye = getResources().getDrawable(R.drawable.ic_eye_off);
                            etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, icon_eye, null);
                            lookPassword = true;
                            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                        }
                    }
                }
                return false;
            }
        });
    }
    private String readFile() {
        try {
            FileInputStream fis = this.openFileInput(saveFile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            br.close();
            isr.close();
            fis.close();
            return line;
        } catch (IOException ex) {
            return null;
        }
    }
    private void writeFile(String name) {
        try {
            FileOutputStream fos = this.openFileOutput(saveFile, Context.MODE_PRIVATE);
            fos.write(name.getBytes());
            fos.close();
        } catch (IOException ignored) { }
    }
    private void getAccess() {
        String account = etAccount.getText().toString();
        String password = etPassword.getText().toString();
        if (account.isEmpty() || password.isEmpty()) {
            showDialog.showAlertDialog(this, "Yêu cầu", "Nhập đầy đủ tên tài khoản và mật khẩu");
        } else {
            ApiService.apiService.getAccess(account, password).enqueue(new Callback<AccessResponse>() {
                @Override
                public void onResponse(Call<AccessResponse> call, Response<AccessResponse> response) {
                    if (response.isSuccessful()) {
                        AccessResponse ar = response.body();
                        String id = ar.getId();
                        writeFile(etAccount.getText().toString());
                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                        mainIntent.putExtra("id", id);
                        mainIntent.putExtra("name", etAccount.getText().toString());
                        mainIntent.putExtra("pass", etPassword.getText().toString());
                        startActivity(mainIntent);
                        finish();
                    } else {
                        try {
                            Gson gson = new Gson();
                            JsonObject errorBody = gson.fromJson(response.errorBody().charStream(), JsonObject.class);
                            String errorMessage = errorBody.get("message").getAsString();
                            showDialog.showAlertDialog(LoginActivity.this, "Thông báo", errorMessage);
                        } catch (Exception e) {
                            showDialog.showAlertDialog(LoginActivity.this, "Lỗi", e.toString());
                        }
                    }
                }
                @Override
                public void onFailure(Call<AccessResponse> call, Throwable t) {
                    showDialog.showAlertDialog(LoginActivity.this, "Lỗi", "Không nhận được phản hồi. Kiểm tra lại kết nối của bạn");
                }
            });
        }
    }
    private void getWidget() {
        txtAccountCreate = findViewById(R.id.txtAccountCreate);
        etAccount = findViewById(R.id.etAccount);
        etPassword = findViewById(R.id.etPassword);
        btnAccess = findViewById(R.id.btnAccess);
        Drawable[] drawables = etPassword.getCompoundDrawables();
    }
}