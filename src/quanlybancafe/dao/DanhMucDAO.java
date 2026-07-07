/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlybancafe.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import quanlybancafe.helper.JdbcHelper;
import quanlybancafe.model.DanhMuc;
/**
 *
 * @author minhs
 */
public class DanhMucDAO {
    private static final String SELECT_ALL_SQL = "SELECT MaDM, TenDM FROM DANH_MUC";
    
    private static final String INSERT_SQL = "INSERT INTO DANH_MUC (TenDM) VALUES (?)";
    private static final String UPDATE_SQL = "UPDATE DANH_MUC SET TenDM = ? WHERE MaDM = ?";
    private static final String DELETE_SQL = "DELETE FROM DANH_MUC WHERE MaDM = ?";
    /**
     * Phương thức thực hiện truy vấn SELECT và ánh xạ kết quả ra List<DanhMuc>
     * @return 
     */
    public List<DanhMuc> selectAll() {
        List<DanhMuc> list = new ArrayList<>();
        try (Connection con = JdbcHelper.getConnection();
                ResultSet rs = JdbcHelper.executeQuery(con, SELECT_ALL_SQL)) {
            while (rs.next()) {
                DanhMuc dm = new DanhMuc();
                dm.setMaDM(rs.getInt("MaDM"));
                dm.setTenDM(rs.getString("TenDM"));
                list.add(dm);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Ném lỗi Runtime nếu cần xử lý lỗi ở tầng giao diện
            throw new RuntimeException("Lỗi truy vấn danh mục: " + ex.getMessage());
        }
        return list;
    }
    
    /**
     * Thêm Danh Mục mới vào Database
     */
    public void insert(DanhMuc model) {
        try {
            JdbcHelper.executeUpdate(INSERT_SQL, model.getTenDM());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Thêm danh mục thất bại: " + ex.getMessage());
        }
    }
    
    public void update(DanhMuc model) {
        try {
            JdbcHelper.executeUpdate(UPDATE_SQL, model.getTenDM(), model.getMaDM());
        } catch (Exception ex) {
            throw new RuntimeException("Cập nhật danh mục thất bại");
        }
    }
    
    public void delete(int maDM) {
        try {
            JdbcHelper.executeUpdate(DELETE_SQL, maDM);
        } catch (Exception ex) {
            throw new RuntimeException("Xóa danh mục thất bạn. (Không thể xóa danh mục đang có sản phầm)");
        }
    }
}
