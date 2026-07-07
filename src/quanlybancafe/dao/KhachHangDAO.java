/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlybancafe.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import quanlybancafe.helper.JdbcHelper;
import quanlybancafe.model.KhachHang;

/**
 *
 * @author minhs
 */
public class KhachHangDAO {
    
    public void insert(KhachHang model) {
        String sql = "INSERT INTO KHACH_HANG (TenKH, Email, MatKhau, DiemTichLuy, NgayDangKy) VALUES (?, ?, ?, 0, GETDATE())";
        JdbcHelper.executeUpdate(sql, model.getTenKH(), model.getEmail(), model.getMatKhau());
    }

    public void update(KhachHang model) {
        String sql = "UPDATE KHACH_HANG SET TenKH=?, MatKhau=?, DiemTichLuy=? WHERE MaKH=?";
        JdbcHelper.executeUpdate(sql, model.getTenKH(), model.getMatKhau(), model.getDiemTichLuy(), model.getMaKH());
    }

    public void delete(int maKH) {
        String sql = "DELETE FROM KHACH_HANG WHERE MaKH=?";
        JdbcHelper.executeUpdate(sql, maKH);
    }

    public List<KhachHang> selectAll() {
        String sql = "SELECT * FROM KHACH_HANG";
        return selectBySql(sql);
    }

    public List<KhachHang> selectByKeyword(String keyword) {
        String sql = "SELECT * FROM KHACH_HANG WHERE TenKH LIKE ? OR Email LIKE ?";
        String key = "%" + keyword + "%";
        return selectBySql(sql, key, key);
    }

    public KhachHang selectById(String id) {
        String sql = "SELECT * FROM KHACH_HANG WHERE MaKH=?";
        List<KhachHang> list = selectBySql(sql, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public KhachHang checkLogin(String email, String password) {
        String sql = "SELECT * FROM KHACH_HANG WHERE Email = ? AND MatKhau = ?";
        List<KhachHang> list = selectBySql(sql, email, password);
        return list.isEmpty() ? null : list.get(0);
    }

    protected List<KhachHang> selectBySql(String sql, Object... args) {
        List<KhachHang> list = new ArrayList<>();
        try (java.sql.Connection con = JdbcHelper.getConnection();
             ResultSet rs = JdbcHelper.executeQuery(con, sql, args)) {
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getInt("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setEmail(rs.getString("Email"));
                kh.setMatKhau(rs.getString("MatKhau"));
                kh.setDiemTichLuy(rs.getInt("DiemTichLuy"));
                kh.setNgayDangKy(rs.getDate("NgayDangKy")); 
                list.add(kh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public double getTongChiTieu(int maKH) {
        String sql = "SELECT SUM(TongTien) FROM HOA_DON WHERE MaKH = ?"; 
        try (java.sql.Connection con = JdbcHelper.getConnection();
             ResultSet rs = JdbcHelper.executeQuery(con, sql, maKH)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getMonYeuThich(int maKH) {
        String sql = "SELECT TOP 1 sp.TenSP " +
                     "FROM HOA_DON hd " +
                     "JOIN CHI_TIET_HOA_DON cthd ON hd.MaHD = cthd.MaHD " +
                     "JOIN SAN_PHAM sp ON cthd.MaSP = sp.MaSP " +
                     "WHERE hd.MaKH = ? " +
                     "GROUP BY sp.TenSP " +
                     "ORDER BY SUM(cthd.SoLuong) DESC";
        
        try (java.sql.Connection con = JdbcHelper.getConnection();
             ResultSet rs = JdbcHelper.executeQuery(con, sql, maKH)) {
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Chưa có";
    }

}
