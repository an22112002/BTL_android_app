package com.example.btl_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_application.ApiConnect.ApiService;
import com.example.btl_application.Model.DiemHocPhan;
import com.example.btl_application.Model.EducationProgram;
import com.example.btl_application.Model.SimpleMessage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity {
    ShowDialog showDialog;
    Button btnBack, btnThem, btnThongKe;
    TextView tv1;
    ListView lvDanhsachDiem;
    EducationProgram educationProgram;
    List<DiemHocPhan> dsDiem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWidget();
        registerForContextMenu(lvDanhsachDiem);
        Intent intent = getIntent();
        educationProgram = (EducationProgram) intent.getSerializableExtra("educationProgram");
        tv1.setText("Chương trình: " + educationProgram.getTenChuongTrinh() + "\n" + "Tổng tín chỉ: " + educationProgram.getTongTinChi());
        lvDanhsachDiem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DiemHocPhan diem = dsDiem.get(position);
                Intent editSection = new Intent(MainActivity2.this, editSection.class);
                editSection.putExtra("diemhocphan", diem);
                startActivity(editSection);
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, AddSection.class);
                intent.putExtra("id_chuongtrinh", educationProgram.getId_chuongTrinh());
                double sumTinChiDaCo=getSoTinChiDaCo();
                intent.putExtra("allowSoTinChi", educationProgram.getTongTinChi() - sumTinChiDaCo);
                startActivity(intent);
            }
        });
        btnThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent thongKe = new Intent(MainActivity2.this, ThongKe.class);
                thongKe.putExtra("dsdiem", new ArrayList<>(dsDiem));
                thongKe.putExtra("tongTinChiEp", educationProgram.getTongTinChi());
                startActivity(thongKe);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        getDiem();
    }
    private void getWidget() {
        btnBack = findViewById(R.id.btnBack_actMain2);
        btnThem = findViewById(R.id.btnThem_actMain2);
        btnThongKe = findViewById(R.id.btnThongKe_actMain2);
        lvDanhsachDiem = findViewById(R.id.lvDanhSachDiem);
        tv1 = findViewById(R.id.textView1);
    }
    private double getSoTinChiDaCo() {
        double tongTinChi = 0.0;
        List<DiemHocPhan> dsQuaMon = new ArrayList<>();
        for (DiemHocPhan diem:dsDiem) {
            if ((diem.quaMon() || !diem.isEdited()) && notIn(dsQuaMon, diem)) {
                dsQuaMon.add(diem);
            }
        }
        for (DiemHocPhan diem:dsQuaMon) {
            tongTinChi += diem.getSoTinChi();
        }
        return tongTinChi;
    }
    private boolean notIn(List<DiemHocPhan> dsDiem, DiemHocPhan diem) {
        for (DiemHocPhan diemQuaMon:dsDiem) {
            if (diemQuaMon.equals(diem)) {
                return false;
            }
        }
        return true;
    }
    private void getDiem() {
        ApiService.apiService.getDiemHocPhan(educationProgram.getId_chuongTrinh()).enqueue(new Callback<List<DiemHocPhan>>() {
            @Override
            public void onResponse(Call<List<DiemHocPhan>> call, Response<List<DiemHocPhan>> response) {
                if (response.isSuccessful()) {
                    dsDiem = response.body();
                    ArrayAdapter<DiemHocPhan> arrayAdapter = new ArrayAdapter<>(MainActivity2.this, android.R.layout.simple_list_item_1, dsDiem);
                    lvDanhsachDiem.setAdapter(arrayAdapter);
                }
            }
            @Override
            public void onFailure(Call<List<DiemHocPhan>> call, Throwable t) {
                showDialog.showAlertDialog(MainActivity2.this, "Lỗi", "Không nhận được phản hồi. Kiểm tra lại kết nối của bạn");
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_xoa, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btnMenuXoa) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            DiemHocPhan diem = dsDiem.get(info.position);
            ApiService.apiService.deleteSectionScore(diem.getId_HocPhan()).enqueue(new Callback<SimpleMessage>() {
                @Override
                public void onResponse(Call<SimpleMessage> call, Response<SimpleMessage> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity2.this, "Học phần đã được xóa", Toast.LENGTH_SHORT).show();
                        getDiem();
                    } else {
                        showDialog.showAlertDialog(MainActivity2.this, "Thông báo", "Học phần chưa được xóa");
                    }
                }

                @Override
                public void onFailure(Call<SimpleMessage> call, Throwable t) {
                    showDialog.showAlertDialog(MainActivity2.this, "Lỗi", "Không nhận được phản hồi. Kiểm tra lại kết nối của bạn");
                }
            });
        }
        return super.onContextItemSelected(item);
    }
}