package com.example.btl_application.Model;

import java.io.Serializable;
import java.util.List;

public class EducationProgram implements Serializable {
    private String Id_chuongTrinh;
    private String TenChuongTrinh;
    private double TongTinChi;

    public String getId_chuongTrinh() {
        return Id_chuongTrinh;
    }

    public String getTenChuongTrinh() {
        return TenChuongTrinh;
    }

    public double getTongTinChi() {
        return TongTinChi;
    }

    @Override
    public String toString() {
        return "Chương trình: "+TenChuongTrinh+" - Số tín chỉ: "+TongTinChi;
    }
}
