/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlybancafe.model;

/**
 *
 * @author minhs
 */
public class LoaiHinhLV {
    private int maLHLV;
    private String tenLHLV;
    private double mucLuongCoDinh;
    private double mucLuongTheoGio;
    
    public LoaiHinhLV() {
        
    }
    
    public LoaiHinhLV(int maLHLV, String tenLHLV, double mucLuongCoDinh, double mucLuongTheoGio) {
        this.maLHLV = maLHLV;
        this.tenLHLV = tenLHLV;
        this.mucLuongCoDinh = mucLuongCoDinh;
        this.mucLuongTheoGio = mucLuongTheoGio;
    }
    
    public int getMaLHLV() { return maLHLV; }
    public void setMaLHLV (int maLHLV) { this.maLHLV = maLHLV; }
    public String getTenLHLV() { return tenLHLV; }
    public void setTenLHLV(String tenLHLV) { this.tenLHLV = tenLHLV; }
    public double getMucLuongCoDinh() { return mucLuongCoDinh; }
    public void getMucLuongCoDInh(double mucLuongCoDinh) { this.mucLuongCoDinh = mucLuongCoDinh; }
    public double getMucLuongTheoGio() { return mucLuongTheoGio; }
    public void setMucLuongTheoGio(double mucLuongTheoGio) { this.mucLuongTheoGio = mucLuongTheoGio; }
    
    @Override
    public String toString() {
        return this.tenLHLV;
    }

    public void setMucLuongCoDinh(double aDouble) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
