package com.example.btl_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.btl_application.ApiConnect.ApiService;
import com.example.btl_application.Model.SimpleMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProgram extends AppCompatActivity {
    ShowDialog showDialog;
    Button btnBack, btnThem;
    EditText inputTen, inputSoTinChi;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_program);
        getWidget();
        Intent n = getIntent();
        id = n.getStringExtra("id");
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenCt = inputTen.getText().toString().trim();
                String strTongTinChi = inputSoTinChi.getText().toString();
                if (tenCt.isEmpty() || strTongTinChi.isEmpty()) {
                    showDialog.showAlertDialog(AddProgram.this, "Lỗi nhập liệu", "Tên chương trình và tổng tín chỉ không được để trống");
                    return;
                }
                double tongTinChi = Double.parseDouble(strTongTinChi);
                ApiService.apiService.createNewEducationProgram(id, tenCt, tongTinChi).enqueue(new Callback<SimpleMessage>() {
                    @Override
                    public void onResponse(Call<SimpleMessage> call, Response<SimpleMessage> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AddProgram.this, "Chương trình đã được lưu", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            showDialog.showAlertDialog(AddProgram.this, "Thông báo", "Chương trình chưa được lưu");
                        }
                    }

                    @Override
                    public void onFailure(Call<SimpleMessage> call, Throwable t) {
                        showDialog.showAlertDialog(AddProgram.this, "Lỗi", "Không nhận được phản hồi. Kiểm tra lại kết nối của bạn");
                    }
                });
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getWidget() {
        btnBack = findViewById(R.id.btnBack_addProgram);
        btnThem = findViewById(R.id.btnThem_addProgram);
        inputTen = findViewById(R.id.inputTenCc);
        inputSoTinChi = findViewById(R.id.inputSoTinChi);
    }
}