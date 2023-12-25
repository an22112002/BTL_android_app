package com.example.btl_application.Model;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.math.RoundingMode;

public class DiemHocPhan implements Serializable {
    private String Id_ChuongTrinh;
    private String Id_HocPhan;
    private Double SoTinChi;
    private String TenMon;
    private String TiLeDiem;
    private String TiLeTX;
    private double Tx1;
    private double Tx2;
    private double Tx3;
    private double Tx4;
    private double Tx5;
    private double ChuyenCan;
    private double GiuaKy;
    private double CuoiKy;
    private boolean Edited;
    private double diemTongKet = -1;

    public String getId_HocPhan() {
        return Id_HocPhan;
    }

    public double getSoTinChi() {
        return SoTinChi;
    }

    public String getTenMon() {
        return TenMon;
    }

    public String getTiLeDiem() {
        return TiLeDiem;
    }

    public String getTiLeTX() {
        return TiLeTX;
    }

    public boolean isEdited() {
        return Edited;
    }

    public String[] layTiLeTx() { return TiLeTX.split("\\|"); }
    public String[] layTiLeDiem() { return TiLeDiem.split("\\|"); }
    public double[] getAllTx() {
        return new double[]{Tx1, Tx2, Tx3, Tx4, Tx5};
    }
    public double[] getAllDiem() {
        return new double[]{ChuyenCan, GiuaKy, CuoiKy};
    }
    public double getDiemTongKet() {
        if (diemTongKet == -1) {
            diemTongKet = tinhDiemTongKet();
        }
        return diemTongKet;
    }
    public double tinhDiemTongKet() {
        String[] tx = layTiLeTx();
        String[] diem = layTiLeDiem();
        double dtk = (Tx1*Double.parseDouble(tx[0]) / 100
                + Tx2 * Double.parseDouble(tx[1]) / 100
                + Tx3 * Double.parseDouble(tx[2]) / 100
                + Tx4 * Double.parseDouble(tx[3]) / 100
                + Tx5 * Double.parseDouble(tx[4]) / 100) * Double.parseDouble(diem[0])/100
                + ChuyenCan * Double.parseDouble(diem[1]) / 100
                + GiuaKy * Double.parseDouble(diem[2]) / 100
                + CuoiKy * Double.parseDouble(diem[3]) / 100;
        return Math.round(dtk * 100.0) / 100.0;
    }
    public String[] getKetQua() {
        double x = getDiemTongKet();
        if (x>=8.5) {
            return new String[]{"A","4.0","Giỏi"};
        } else if (x>=7.8) {
            return new String[]{"B+","3.5","Khá"};
        } else if (x>=7) {
            return new String[]{"B","3.0","Khá"};
        } else if (x>=6.3) {
            return new String[]{"C+","2.5","Trung bình"};
        } else if (x>=5.5) {
            return new String[]{"C","2.0","Trung bình"};
        } else if (x>=4.8) {
            return new String[]{"D+","1.5","Trung bình yếu"};
        } else if (x>=4.0) {
            return new String[]{"D","1.0","Trung bình yếu"};
        } else if (x>=3.0) {
            return new String[]{"F+","0.5","Kém"};
        } else {
            return new String[]{"F","0.0","Kém"};
        }
    }
    @Override
    public String toString() {
        String s="Tên môn: " + TenMon + '\n' + "Số tín chỉ: " + SoTinChi ;
        if (Edited) s+="\nĐiểm tổng kết: "+getDiemTongKet();
        if (getDiemTongKet() < 4.0 && isEdited()) s+=" - Học lại";
        return s;
    }
    public boolean quaMon() {
        return getDiemTongKet() >= 4.0;
    }
    public boolean equals(DiemHocPhan obj) {
        return this.TenMon.equals(obj.getTenMon()) && this.SoTinChi == obj.getSoTinChi() && this.TiLeTX.equals(obj.getTiLeTX()) && this.TiLeDiem.equals(obj.getTiLeDiem());
    }
}
