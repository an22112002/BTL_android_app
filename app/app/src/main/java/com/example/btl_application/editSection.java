package com.example.btl_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_application.ApiConnect.ApiService;
import com.example.btl_application.Model.DiemHocPhan;
import com.example.btl_application.Model.SimpleMessage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class editSection extends AppCompatActivity {
    ShowDialog showDialog;
    TableLayout tableTx, tableDiem;
    Button btnLuu, btnBack;
    TextView textView;
    List<EditText> inputTxs = new ArrayList<>();
    List<EditText> inputDiems = new ArrayList<>();
    double[] allTx, allDiem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_section);
        getWidget();

        Intent editSection = getIntent();
        // lấy điểm học phần từ activity trước
        DiemHocPhan diemHocPhan = (DiemHocPhan) editSection.getSerializableExtra("diemhocphan");
        String[] tiLeTx = diemHocPhan.layTiLeTx();
        String[] tiLeDiem = diemHocPhan.layTiLeDiem();
        allTx = diemHocPhan.getAllTx();
        allDiem = diemHocPhan.getAllDiem();
        if (diemHocPhan.isEdited()) {
            btnLuu.setEnabled(false);
            textView.setText("Chi tiết điểm học phần");
        }
        for (int i=0; i<5; i++) {
            double t = Double.parseDouble(tiLeTx[i]);
            if (t!=0) {
                // tạo table row cho các điểm thường xuyên
                TableRow tableRow = new TableRow(this);
                // lable
                TextView tv = createTextView("Điểm thường xuyên "+(i+1)+":");
                tableRow.addView(tv);
                // editText
                EditText ed = createEditText();
                if (diemHocPhan.isEdited()) {
                    ed.setText(allTx[i]+"");
                    ed.setEnabled(false);
                }
                inputTxs.add(ed);
                tableRow.addView(ed);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT));
                tableTx.addView(tableRow);
            } else {
                inputTxs.add(null);
            }
        }
        String labeltext = "";
        for (int i=0; i<3; i++) {
            if (i==0) {
                labeltext = "Chuyên cần";
            } else if (i==1) {
                labeltext = "Giữa kỳ";
            } else if (i==2) {
                labeltext = "Cuối kỳ";
            }
            double t = Double.parseDouble(tiLeDiem[i+1]);
            if (t!=0) {
                // tạo table row cho các điểm thường xuyên
                TableRow tableRow = new TableRow(this);
                // lable
                TextView tv = createTextView(labeltext);
                tableRow.addView(tv);
                // editText
                EditText ed = createEditText();
                if (diemHocPhan.isEdited()) {
                    ed.setText(allDiem[i]+"");
                    ed.setEnabled(false);
                }
                inputDiems.add(ed);
                tableRow.addView(ed);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT));
                tableDiem.addView(tableRow);
            } else {
                inputDiems.add(null);
            }
        }
        if (diemHocPhan.isEdited()) {
            String[] ketQuaValues = diemHocPhan.getKetQua();
            TextView ketQua = createTextView("Điểm tổng kết: "+diemHocPhan.getDiemTongKet()+
                    " - Điểm chữ: "+ketQuaValues[0]+"\nĐiểm 4: "+ketQuaValues[1]+
                    " - Xếp loại: "+ketQuaValues[2]);
            TableRow tableRowKetQua = new TableRow(this);
            tableRowKetQua.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT));
            tableRowKetQua.setGravity(Gravity.CENTER);
            tableRowKetQua.addView(ketQua);
            tableDiem.addView(tableRowKetQua);
        }
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double[] txValue = new double[5];
                for (int i=0; i<5; i++) {
                    try {
                        String s = inputTxs.get(i).getText().toString();
                        if (s.isEmpty()) {
                            showDialog.showAlertDialog(editSection.this, "Lỗi nhập liệu", "Nhập đầy đủ điểm thường xuyên");
                            return;
                        }
                        txValue[i] = Double.parseDouble(s);
                    } catch (Exception ignored){
                        txValue[i]=0;
                    }
                }
                double[] diemValue = new double[3];
                for (int i=0; i<3; i++) {
                    try {
                        String s = inputDiems.get(i).getText().toString();
                        if (s.isEmpty()) {
                            showDialog.showAlertDialog(editSection.this, "Lỗi nhập liệu", "Nhập đầy đủ điểm");
                            return;
                        }
                        diemValue[i] = Double.parseDouble(s);
                    } catch (Exception ignored){
                        diemValue[i]=0;
                    }
                }
                ApiService.apiService.updateSectionScore(diemHocPhan.getId_HocPhan(), txValue[0],
                        txValue[1], txValue[2], txValue[3], txValue[4], diemValue[0], diemValue[1], diemValue[2]).enqueue(new Callback<SimpleMessage>() {
                    @Override
                    public void onResponse(Call<SimpleMessage> call, Response<SimpleMessage> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(editSection.this, "Đã lưu điểm", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            showDialog.showAlertDialog(editSection.this, "Lỗi", "Chưa lưu được điểm");
                        }
                    }
                    @Override
                    public void onFailure(Call<SimpleMessage> call, Throwable t) {
                        showDialog.showAlertDialog(editSection.this, "Lỗi", "Không nhận được phản hồi. Kiểm tra lại kết nối của bạn");
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
    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setLayoutParams(new TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return textView;
    }
    private EditText createEditText() {
        EditText editText = new EditText(this);
        editText.setLayoutParams(new TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        return editText;
    }
    private void getWidget() {
        tableTx = findViewById(R.id.tableTx);
        tableDiem = findViewById(R.id.tableDiem);
        btnLuu = findViewById(R.id.btnSave_editSection);
        btnBack = findViewById(R.id.btnBack_editSection);
        textView = findViewById(R.id.tv_editSection);
    }
}