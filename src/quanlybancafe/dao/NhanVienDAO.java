/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlybancafe.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import quanlybancafe.helper.JdbcHelper;
import static quanlybancafe.helper.JdbcHelper.getConnection;
import quanlybancafe.model.NhanVien;

/**
 *
 * @author minhs
 */
    public class NhanVienDAO {
    
    public void insert(NhanVien model) {
        String sql = "INSERT INTO NHAN_VIEN (MaNV, TenNV, GioiTinh, NgaySinh, SDT, DiaChi, Email, MatKhau, VaiTro, MaLHLV, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        JdbcHelper.executeUpdate(sql, 
            model.getMaNV(), 
            model.getTenNV(),
            model.getGioiTinh(),
            model.getNgaySinh(),
            model.getSdt(),
            model.getDiaChi(),
            model.getEmail(),
            model.getMatKhau(),
            model.getVaiTro(),
            model.getMaLHLV(),
            model.isTrangThai());
    }

    public void update(NhanVien model) {
        String sql = "UPDATE NHAN_VIEN SET TenNV=?, GioiTinh=?, NgaySinh=?, SDT=?, DiaChi=?, Email=?, MatKhau=?, VaiTro=?, TrangThai=? WHERE MaNV=?";
        JdbcHelper.executeUpdate(sql, 
            model.getTenNV(),
            model.getGioiTinh(),
            model.getNgaySinh(),
            model.getSdt(),
            model.getDiaChi(),
            model.getEmail(), 
            model.getMatKhau(), 
            model.getVaiTro(),
            model.isTrangThai(),
            model.getMaNV());
    }

    public void delete(String maNV) {
        String sql = "DELETE FROM NHAN_VIEN WHERE MaNV = ?";
        JdbcHelper.executeUpdate(sql, maNV);
    }

    public List<NhanVien> selectAll() {
        String sql = "SELECT * FROM NHAN_VIEN";
        return selectBySql(sql);
    }

    protected List<NhanVien> selectBySql(String sql, Object... args) {
        List<NhanVien> list = new ArrayList<>();
        try (Connection con = getConnection();
                ResultSet rs = JdbcHelper.executeQuery(con, sql, args)) {
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setMatKhau(rs.getString("MatKhau"));
                nv.setTenNV(rs.getString("TenNV"));
                nv.setGioiTinh(rs.getString("GioiTinh"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setSdt(rs.getString("SDT"));
                nv.setDiaChi(rs.getString("DiaChi"));
                nv.setEmail(rs.getString("Email"));
                nv.setVaiTro(rs.getInt("VaiTro"));
                nv.setMaLHLV(rs.getInt("MaLHLV"));
                nv.setNgayBatDauLam(rs.getDate("NgayBatDauLam"));
                nv.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<NhanVien> selectByKeyword(String keyword) {
    String sql = "SELECT * FROM NHAN_VIEN WHERE MaNV LIKE ? OR TenNV LIKE ?";
    return selectBySql(sql, "%" + keyword + "%", "%" + keyword + "%");
    }

    public NhanVien selectById(String user) {
        String sql = "SELECT * FROM NHAN_VIEN WHERE MaNV = ?";
        List<NhanVien> list = this.selectBySql(sql, user);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
