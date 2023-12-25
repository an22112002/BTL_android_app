package com.example.btl_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.btl_application.ApiConnect.ApiService;
import com.example.btl_application.Model.SimpleMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSection extends AppCompatActivity {
    ShowDialog showDialog = new ShowDialog();
    CheckBox CbTx1, CbTx2, CbTx3, CbTx4, CbTx5, CbTx, CbCc, CbGk, CbCk;
    EditText editTenMonHoc, editSoTinChi, inputTx1, inputTx2, inputTx3, inputTx4, inputTx5, inputTx, inputCc, inputGk, inputCk;
    Button btnThem, btnBack;
    String Id_ChuongTrinh;
    double AllowTinChi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_section);
        getWidget();

        Intent intent = getIntent();
        Id_ChuongTrinh = intent.getStringExtra("id_chuongtrinh");
        AllowTinChi = intent.getDoubleExtra("allowSoTinChi", 0);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themHocPhan();
            }
        });
        CbTx1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    inputTx1.setText(""); inputTx1.setEnabled(true);
                }else{
                    inputTx1.setText("0"); inputTx1.setEnabled(false);
                }
            }
        });
        CbTx2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    inputTx2.setText(""); inputTx2.setEnabled(true);
                }else{
                    inputTx2.setText("0"); inputTx2.setEnabled(false);
                }
            }
        });
        CbTx3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    inputTx3.setText(""); inputTx3.setEnabled(true);
                }else{
                    inputTx3.setText("0"); inputTx3.setEnabled(false);
                }
            }
        });
        CbTx4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    inputTx4.setText(""); inputTx4.setEnabled(true);
                }else{
                    inputTx4.setText("0"); inputTx4.setEnabled(false);
                }
            }
        });
        CbTx5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    inputTx5.setText(""); inputTx5.setEnabled(true);
                }else{
                    inputTx5.setText("0"); inputTx5.setEnabled(false);
                }
            }
        });
        CbTx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    inputTx.setText(""); inputTx.setEnabled(true);
                }else{
                    inputTx.setText("0"); inputTx.setEnabled(false);
                }
            }
        });
        CbCc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    inputCc.setText(""); inputCc.setEnabled(true);
                }else{
                    inputCc.setText("0"); inputCc.setEnabled(false);
                }
            }
        });
        CbGk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    inputGk.setText(""); inputGk.setEnabled(true);
                }else{
                    inputGk.setText("0"); inputGk.setEnabled(false);
                }
            }
        });
        CbCk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    inputCk.setText(""); inputCk.setEnabled(true);
                }else{
                    inputCk.setText("0"); inputCk.setEnabled(false);
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void themHocPhan() {
        String tenHocPhan = editTenMonHoc.getText().toString().trim();
        if (tenHocPhan.isEmpty()) {
            showDialog.showAlertDialog(AddSection.this, "Lỗi nhập liệu", "Tên học phần không được để trống");
            return;
        }
        double soTinChi, tx1, tx2, tx3, tx4, tx5, tx, cc, gk, ck;
        try {
            soTinChi = Double.parseDouble(editSoTinChi.getText().toString());
            tx1 = Double.parseDouble(inputTx1.getText().toString());
            tx2 = Double.parseDouble(inputTx2.getText().toString());
            tx3 = Double.parseDouble(inputTx3.getText().toString());
            tx4 = Double.parseDouble(inputTx4.getText().toString());
            tx5 = Double.parseDouble(inputTx5.getText().toString());
            tx = Double.parseDouble(inputTx.getText().toString());
            cc = Double.parseDouble(inputCc.getText().toString());
            gk = Double.parseDouble(inputGk.getText().toString());
            ck = Double.parseDouble(inputCk.getText().toString());
        } catch (Exception ex) {
            showDialog.showAlertDialog(AddSection.this, "Lỗi nhập liệu", "Nhập đầy đủ số tín chỉ và các giá trị bạn đã chọn");
            return;
        }
        if (soTinChi > AllowTinChi) {
            showDialog.showAlertDialog(AddSection.this, "Lỗi nhập liệu", "Số tín chỉ này sẽ làm vượt quá tổng số tín chỉ bạn đã nhập");
            return;
        }
        double x = tx1+tx2+tx3+tx4+tx5;
        if (x != 100) {
            showDialog.showAlertDialog(AddSection.this, "Lỗi nhập liệu", "Tỉ lệ điểm thường xuyên có tổng phải bằng 100% không phải "+x+"%");
            return;
        }
        x = tx+cc+gk+ck;
        if (x != 100) {
            showDialog.showAlertDialog(AddSection.this, "Lỗi nhập liệu", "Tỉ lệ điểm có tổng phải bằng 100% không phải "+x+"%");
            return;
        }
        String TiLeTX = Double.toString(tx1)+'|'+Double.toString(tx2)+'|'+Double.toString(tx3)+'|'+Double.toString(tx4)+'|'+Double.toString(tx5);
        String TiLeDiem = Double.toString(tx)+'|'+Double.toString(cc)+'|'+Double.toString(gk)+'|'+Double.toString(ck);
        ApiService.apiService.createNewSectionScore(Id_ChuongTrinh,tenHocPhan,soTinChi,TiLeTX,TiLeDiem).enqueue(new Callback<SimpleMessage>() {
            @Override
            public void onResponse(Call<SimpleMessage> call, Response<SimpleMessage> response) {
                if (response.isSuccessful()) {
                    SimpleMessage simpleMessage = response.body();
                    Toast.makeText(AddSection.this, simpleMessage.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        Gson gson = new Gson();
                        JsonObject messageBody = gson.fromJson(response.errorBody().charStream(), JsonObject.class);
                        String message = messageBody.get("message").getAsString();
                        showDialog.showAlertDialog(AddSection.this, "Thông báo", message);
                    } catch (Exception e) {
                        showDialog.showAlertDialog(AddSection.this, "Lỗi", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleMessage> call, Throwable t) {
                showDialog.showAlertDialog(AddSection.this, "Lỗi", "Không nhận được phản hồi. Kiểm tra lại kết nối của bạn");
            }
        });
    }
    private void getWidget() {
        editTenMonHoc = findViewById(R.id.inputTenCc);
        editSoTinChi = findViewById(R.id.inputSoTinChi);
        btnThem = findViewById(R.id.btnThem_addSection);
        btnBack = findViewById(R.id.btnBack_addSection);
        CbTx1 = findViewById(R.id.cbTx1);
        CbTx2 = findViewById(R.id.cbTx2);
        CbTx3 = findViewById(R.id.cbTx3);
        CbTx4 = findViewById(R.id.cbTx4);
        CbTx5 = findViewById(R.id.cbTx5);
        CbTx = findViewById(R.id.cbTx);
        CbCc = findViewById(R.id.cbCc);
        CbGk = findViewById(R.id.cbGk);
        CbCk = findViewById(R.id.cbCk);
        inputTx1 = findViewById(R.id.inputTx1);
        inputTx2 = findViewById(R.id.inputTx2);
        inputTx3 = findViewById(R.id.inputTx3);
        inputTx4 = findViewById(R.id.inputTx4);
        inputTx5 = findViewById(R.id.inputTx5);
        inputTx = findViewById(R.id.inputTx);
        inputCc = findViewById(R.id.inputCc);
        inputGk = findViewById(R.id.inputGk);
        inputCk = findViewById(R.id.inputCk);
    }
}