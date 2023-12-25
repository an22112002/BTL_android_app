package com.example.btl_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_application.Model.DiemHocPhan;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ThongKe extends AppCompatActivity {
    Button btnBack;
    TextView opTTC,opGPA,opXL,opA,opBp,opB,opCp,opC,opDp,opD,opFp,opF;
    List<DiemHocPhan> dsQuaMon = new ArrayList<>();
    List<DiemHocPhan> dsDiem = new ArrayList<>();
    double tongTinChiCan;
    double tongTinChi = 0.0, gpa = -1.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);
        getWidget();
        Intent thisIntent = getIntent();
        tongTinChiCan = thisIntent.getDoubleExtra("tongTinChiEp", 0);
        dsDiem = (List<DiemHocPhan>) thisIntent.getSerializableExtra("dsdiem");
        getDanhSachQuaMon();
        setTongTinChi();
        setGPA();
        setXepLoai();
        setSoDiem();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void setTongTinChi() {
        for (DiemHocPhan diem:dsQuaMon) {
            tongTinChi += diem.getSoTinChi();
        }
        opTTC.setText(tongTinChi+"");
    }
    private void setGPA() {
        List<DiemHocPhan> dsCacMon = getDanhSachCacMon();
        if (dsDiem.size()==0) {
            opGPA.setText("0.0");
        } else {
            double s = 0.0;
            double t = 0.0;
            for (DiemHocPhan diem:dsCacMon) {
                s += diem.getDiemTongKet() * diem.getSoTinChi();
                t += diem.getSoTinChi();
            }
            gpa = (double) Math.round((s / t)*100)/100;
            opGPA.setText(gpa+"");
        }
    }
    private void setXepLoai() {
        if (tongTinChi == 0.0) {
            opXL.setText("");
        } else {
            double haBac = 0;
            if (haBac(dsDiem)) {
                haBac = -1;
            }
            if (gpa + haBac >= 9) {
                opXL.setText("Xuất sắc");
            } else if (gpa + haBac >= 8) {
                opXL.setText("Giỏi");
            } else if (gpa + haBac >= 7) {
                opXL.setText("Khá");
            } else if (gpa + haBac >= 6) {
                opXL.setText("Trung bình khá");
            } else if (gpa + haBac >= 5) {
                opXL.setText("Trung bình");
            } else if (gpa + haBac >= 4) {
                opXL.setText("Yếu");
            } else {
                opXL.setText("Kém");
            }
        }
    }
    private void setSoDiem() {
        int fA = 0;
        int fBp = 0;
        int fB = 0;
        int fCp = 0;
        int fC = 0;
        int fDp = 0;
        int fD = 0;
        int fFp = 0;
        int fF = 0;
        for (DiemHocPhan diem:dsDiem) {
            if (diem.isEdited()) {
                String[] xepLoai = diem.getKetQua();
                switch (xepLoai[0]) {
                    case "A":
                        fA += 1;
                        break;
                    case "B+":
                        fBp += 1;
                        break;
                    case "B":
                        fB += 1;
                        break;
                    case "C+":
                        fCp += 1;
                        break;
                    case "C":
                        fC += 1;
                        break;
                    case "D+":
                        fDp += 1;
                        break;
                    case "D":
                        fD += 1;
                        break;
                    case "F+":
                        fFp += 1;
                        break;
                    case "F":
                        fF += 1;
                        break;
                }
            }
        }
        opA.setText(fA+"");
        opBp.setText(fBp+"");
        opB.setText(fB+"");
        opCp.setText(fCp+"");
        opC.setText(fC+"");
        opDp.setText(fDp+"");
        opD.setText(fD+"");
        opFp.setText(fFp+"");
        opF.setText(fF+"");
    }
    private void getWidget() {
        btnBack = findViewById(R.id.btnBack_thongKe);
        opTTC = findViewById(R.id.outputTongTinChi);
        opGPA = findViewById(R.id.outputGPA);
        opXL = findViewById(R.id.outputXepLoai);
        opA = findViewById(R.id.outputA);
        opBp = findViewById(R.id.outputBp);
        opB = findViewById(R.id.outputB);
        opCp = findViewById(R.id.outputCp);
        opC = findViewById(R.id.outputC);
        opDp = findViewById(R.id.outputDp);
        opD = findViewById(R.id.outputD);
        opFp = findViewById(R.id.outputFp);
        opF = findViewById(R.id.outputF);
    }
    private void getDanhSachQuaMon() {
        for (DiemHocPhan diem:dsDiem) {
            if (diem.quaMon() && notIn(dsQuaMon, diem) && diem.isEdited()) {
                dsQuaMon.add(diem);
            }
        }
        for (DiemHocPhan diemQuaMon:dsQuaMon) {
            for (DiemHocPhan diem:dsDiem) {
                if (diem.equals(diemQuaMon) && diem.getDiemTongKet() > diemQuaMon.getDiemTongKet()) {
                    dsQuaMon.set(dsQuaMon.indexOf(diemQuaMon), diem);
                }
            }
        }
    }
    private List<DiemHocPhan> getDanhSachCacMon() {
        List<DiemHocPhan> dsCacMon = new ArrayList<>();
        for (DiemHocPhan diem:dsDiem) {
            if (diem.isEdited() && notIn(dsCacMon, diem)) {
                dsCacMon.add(diem);
            }
        }
        for (DiemHocPhan diemMon:dsCacMon) {
            for (DiemHocPhan diem:dsDiem) {
                if (diem.equals(diemMon) && diem.getDiemTongKet() > diemMon.getDiemTongKet()) {
                    dsCacMon.set(dsCacMon.indexOf(diemMon), diem);
                }
            }
        }
        return dsCacMon;
    }
    private boolean notIn(List<DiemHocPhan> dsDiem, DiemHocPhan diem) {
        for (DiemHocPhan diemQuaMon:dsDiem) {
            if (diemQuaMon.equals(diem)) {
                return false;
            }
        }
        return true;
    }
    private boolean haBac(List<DiemHocPhan> dsDiem) {
        List<DiemHocPhan> monKoQua = new ArrayList<>();
        double tongTinChiKoQua = 0.0;
        for (DiemHocPhan diem:dsDiem) {
            if (!diem.quaMon() && notIn(monKoQua, diem) && diem.isEdited()) {
                monKoQua.add(diem);
            }
        }
        for (DiemHocPhan diemKoQua:monKoQua) {
            tongTinChiKoQua += diemKoQua.getSoTinChi();
        }
        return (tongTinChiKoQua / tongTinChiCan) * 100 > 5;
    }
}