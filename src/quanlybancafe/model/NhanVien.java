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
public class NhanVien {
    private String maNV;
    private String matKhau;
    private String tenNV;
    private String gioiTinh;
    private int vaiTro;
    private boolean trangThai;
    private java.sql.Date ngaySinh;
    private String sdt;
    private String diaChi;
    private String email;
    private int maLHLV;
    private java.sql.Date ngayBatDauLam;

    public NhanVien() {}

    public NhanVien(
            String maNV,
            String matKhau,
            String tenNV, 
            String gioiTinh,
            int vaiTro,
            boolean trangThai,
            Date ngaySinh,
            String sdt, 
            String diaChi,
            String email, 
            int maLHLV, 
            Date ngayBatDauLam) {
                this.maNV = maNV;
                this.matKhau = matKhau;
                this.tenNV = tenNV;
                this.gioiTinh = gioiTinh;
                this.vaiTro = vaiTro;
                this.trangThai = trangThai;
                this.ngaySinh = ngaySinh;
                this.sdt = sdt;
                this.diaChi = diaChi;
                this.email = email;
                this.maLHLV = maLHLV;
                this.ngayBatDauLam = ngayBatDauLam;
    }
    
    public String getMaNV() { return maNV; }
    public String getMatKhau() { return matKhau; }
    public String getTenNV() { return tenNV; }
    public String getGioiTinh() { return gioiTinh; }
    public int isVaiTro() { return vaiTro; }
    public int getVaiTro() { return vaiTro; }
    public boolean isTrangThai() { return trangThai; }
    public java.sql.Date getNgaySinh() { return ngaySinh; }
    public String getSdt() { return sdt; }
    public String getDiaChi() { return diaChi; }
    public String getEmail() { return email; }
    public int getMaLHLV() { return maLHLV; }
    public java.sql.Date getNgayBatDauLam() { return ngayBatDauLam; }
    
    public void setMaNV(String maNV) { this.maNV = maNV; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }
    public void setVaiTro(int vaiTro) { this.vaiTro = vaiTro; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
    public void setNgaySinh(java.sql.Date ngaySinh) { this.ngaySinh = ngaySinh; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public void setEmail(String email) { this.email = email; }
    public void setMaLHLV(int maLHLV) { this.maLHLV = maLHLV; }
    public void setNgayBatDauLam(java.sql.Date ngayBatDauLam) { this.ngayBatDauLam = ngayBatDauLam; }
    
    @Override
    public String toString() {
        return this.tenNV;
    }
}
