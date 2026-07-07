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

/**
 *
 * @author minhs
 */
public class ChamCongDAO {
    
    public void saveOrUpdate(String maNV, int thang, int nam, double soGio) {
        String sqlUpdate = "UPDATE CHAM_CONG SET SoGioLam = ? WHERE MaNV = ? AND Thang = ? AND Nam = ?";
        int rowsAffected = JdbcHelper.executeUpdate(sqlUpdate, soGio, maNV, thang, nam);

        if (rowsAffected == 0) {
            String sqlInsert = "INSERT INTO CHAM_CONG (MaNV, Thang, Nam, SoGioLam) VALUES (?, ?, ?, ?)";
            JdbcHelper.executeUpdate(sqlInsert, maNV, thang, nam, soGio);
        }
    }
    
    public List<Object[]> getBangLuongHienThi(int thang, int nam) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT n.MaNV, n.TenNV, l.TenLHLV, " +
                     "ISNULL(l.MucLuongCoDinh, l.MucLuongTheoGio), " +
                     "ISNULL(c.SoGioLam, 0) " +
                     "FROM NHAN_VIEN n " +
                     "JOIN LOAI_HINH_LV l ON n.MaLHLV = l.MaLHLV " +
                     "LEFT JOIN CHAM_CONG c ON n.MaNV = c.MaNV AND c.Thang = ? AND c.Nam = ?";

        try (Connection con = JdbcHelper.getConnection();
                ResultSet rs = JdbcHelper.executeQuery(con, sql, thang, nam)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString(1), rs.getString(2), rs.getString(3), 
                    rs.getDouble(4), rs.getDouble(5)
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
        
    public void upsert(String maNV, int thang, int nam, double soGio) {
        String sql = "IF EXISTS (SELECT 1 FROM CHAM_CONG WHERE MaNV=? AND Thang=? AND Nam=?) " +
                     "UPDATE CHAM_CONG SET SoGioLam=? WHERE MaNV=? AND Thang=? AND Nam=? " +
                     "ELSE INSERT INTO CHAM_CONG(MaNV, Thang, Nam, SoGioLam) VALUES(?,?,?,?)";
        JdbcHelper.executeUpdate(sql, maNV, thang, nam, soGio, maNV, thang, nam, maNV, thang, nam, soGio);
    }

}

