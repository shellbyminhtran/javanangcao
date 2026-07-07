/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlybancafe.model;

import java.sql.Date;


/**
 *
 * @author minhs
 */
public class KhachHang {
    private int maKH;
    private String tenKH;
    private String email;
    private String matKhau;
    private int diemTichLuy;
    private Date ngayDangKy;

    public KhachHang() {
    }

    public KhachHang(int maKH, String tenKH, String email, String matKhau, int diemTichLuy) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.email = email;
        this.matKhau = matKhau;
        this.diemTichLuy = diemTichLuy;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    @Override
    public String toString() {
        return this.tenKH;
    }

    public void setNgayDangKy(Date ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }
}
