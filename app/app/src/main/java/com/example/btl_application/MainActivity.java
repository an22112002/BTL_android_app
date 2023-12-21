package com.example.btl_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.btl_application.ApiConnect.ApiService;
import com.example.btl_application.Model.DiemHocPhan;
import com.example.btl_application.Model.EducationProgram;
import com.example.btl_application.Model.SimpleMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public ShowDialog showDialog = new ShowDialog();
    public List<EducationProgram> programList;
    public String id, name, pass;
    public ListView lvDanhSachChuongTrinh;
    public Button btnAdd;
    Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWidget();
        getIntentExtra();

        //getData();

        tb.setTitle("Hello, "+name);
        setSupportActionBar(tb);
        registerForContextMenu(lvDanhSachChuongTrinh);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(MainActivity.this, AddProgram.class);
                n.putExtra("id", id);
                startActivity(n);
            }
        });
        lvDanhSachChuongTrinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("educationProgram", programList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getData();
    }

    private void getWidget() {
        tb = findViewById(R.id.tb);
        lvDanhSachChuongTrinh = findViewById(R.id.lvDanhSachChuongTrinh);
        btnAdd = findViewById(R.id.btnAddProgram);
    }
    private void getIntentExtra() {
        Intent thisIntent = getIntent();
        id = thisIntent.getStringExtra("id");
        name = thisIntent.getStringExtra("name");
        pass = thisIntent.getStringExtra("pass");
    }
    private void getData() {
        ApiService.apiService.getData(id).enqueue(new Callback<List<EducationProgram>>() {
            @Override
            public void onResponse(Call<List<EducationProgram>> call, Response<List<EducationProgram>> response) {
                if (response.isSuccessful()) {
                    programList = response.body();
                    ArrayAdapter<EducationProgram> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, programList);
                    lvDanhSachChuongTrinh.setAdapter(arrayAdapter);
                } else {
                    try {
                        Gson gson = new Gson();
                        JsonObject messageBody = gson.fromJson(response.errorBody().charStream(), JsonObject.class);
                        String message = messageBody.get("message").getAsString();
                        showDialog.showAlertDialog(MainActivity.this, "Thông báo", message+". Hay bắt đầu nhập chương trình đào tạo của mình.");
                    } catch (Exception e) {
                        showDialog.showAlertDialog(MainActivity.this, "Lỗi", e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<EducationProgram>> call, Throwable t) {
                showDialog.showAlertDialog(MainActivity.this, "Lỗi", "Không nhận được phản hồi. Kiểm tra lại kết nối của bạn");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btnMenuLogout) {
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login);
            finish();
            return true;
        }
        if (item.getItemId() == R.id.btnMenuChangePass) {
            Intent changePass = new Intent(MainActivity.this, changePassword.class);
            changePass.putExtra("id", id);
            changePass.putExtra("password", pass);
            startActivity(changePass);
            return true;
        }
        if (item.getItemId() == R.id.btnMenuOut) {
            finish();
            return true;
        }
        return onOptionsItemSelected(item);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_xoa, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        EducationProgram educationProgram = programList.get(info.position);
        if (item.getItemId() == R.id.btnMenuXoa) {
            ApiService.apiService.deleteEducationProgram(educationProgram.getId_chuongTrinh()).enqueue(new Callback<SimpleMessage>() {
                @Override
                public void onResponse(Call<SimpleMessage> call, Response<SimpleMessage> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Chương trình đã được xóa", Toast.LENGTH_SHORT).show();
                        getData();
                    } else {
                        showDialog.showAlertDialog(MainActivity.this, "Thông báo", "Chương trình đào tạo chưa được xóa");
                    }
                }

                @Override
                public void onFailure(Call<SimpleMessage> call, Throwable t) {
                    showDialog.showAlertDialog(MainActivity.this, "Lỗi", "Không nhận được phản hồi. Kiểm tra lại kết nối của bạn");
                }
            });
        }
        return super.onContextItemSelected(item);
    }
}