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
import quanlybancafe.model.SanPham;

/**
 *
 * @author minhs
 */
public class SanPhamDAO {
    
    private static final String SELECT_ALL_SQL = "SELECT * FROM SAN_PHAM";
    private static final String INSERT_SQL = "INSERT INTO SAN_PHAM (MaSP, TenSP, GiaBan, MaDM, TrangThai) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE SAN_PHAM SET TenSP=?, GiaBan=?, MaDM=?, TrangThai=? WHERE MaSP=?";
    private static final String DELETE_SQL = "DELETE FROM SAN_PHAM WHERE MaSP = ?";
    public void insert(SanPham model) {
        try {
            JdbcHelper.executeUpdate(INSERT_SQL,
                    model.getMaSP(),
                    model.getTenSP(),
                    model.getGiaBan(),
                    model.getMaDM(),
                    model.isTrangThai());
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thêm sản phẩm: " + e.getMessage());
        }
    }
    
    public void update(SanPham model) {
        try {
            JdbcHelper.executeUpdate(UPDATE_SQL,
                    model.getTenSP(),
                    model.getGiaBan(),
                    model.getMaDM(),
                    model.isTrangThai(),
                    model.getMaSP());
        } catch (Exception e) {
            e.printStackTrace(); 
            throw new RuntimeException("Lỗi cập nhật sản phẩm: " + e.getMessage());
        }
    }
    
    public void delete(String maSP) {
        try {
            JdbcHelper.executeUpdate(DELETE_SQL, maSP);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xóa sản phẩm");
        }
    }
    
    public List<SanPham> selectByFilter(String maSP, String tenSP) {
        String sql = "SELECT * FROM SAN_PHAM WHERE MaSP LIKE ? AND TenSP LIKE ?";
        return selectBySql(sql, "%" + maSP + "%", "%" + tenSP + "%");
    }
    
    public List<SanPham> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }
    
    protected List<SanPham> selectBySql(String sql, Object... args) {
        List<SanPham> list = new ArrayList<>();
        try (Connection con = JdbcHelper.getConnection();
                ResultSet rs = JdbcHelper.executeQuery(con, sql, args)) {
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSP(rs.getString("MaSP"));
                sp.setTenSP(rs.getString("TenSP"));
                sp.setGiaBan(rs.getDouble("GiaBan"));
                sp.setMaDM(rs.getInt("MaDM"));
                sp.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi truy vấn sản phẩm");
        }
        return list;
    }

    public SanPham selectById(String maSP) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}
