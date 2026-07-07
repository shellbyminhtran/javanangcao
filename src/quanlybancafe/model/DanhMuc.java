/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlybancafe.model;

/**
 *
 * @author minhs
 */
public class DanhMuc {
    private int maDM;
    private String tenDM;
    
    public DanhMuc() {}
    
    public DanhMuc(int maDM, String tenDM) {
        this.maDM = maDM;
        this.tenDM = tenDM;
    }
    
    public int getMaDM() {
        return maDM;
    }
    
    public void setMaDM(int maDM) {
        this.maDM = maDM;
    }
    
    public String getTenDM() {
        return tenDM;
    }

    public void setTenDM(String tenDM) {
        this.tenDM = tenDM;
    }

    @Override
    public String toString() {
        return tenDM;
    }
}
