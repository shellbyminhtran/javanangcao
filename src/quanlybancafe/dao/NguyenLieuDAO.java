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
import quanlybancafe.model.NguyenLieu;

/**
 *
 * @author minhs
 */
public class NguyenLieuDAO {
    public void insert(NguyenLieu nl) {
        String sql = "INSERT INTO KHO_NGUYEN_LIEU VALUES (?, ?, ?, ?, ?)";
        JdbcHelper.executeUpdate(sql,
                nl.getMaNL(),
                nl.getTenNL(),
                nl.getDonViTinh(),
                nl.getSoLuongTon(),
                nl.getDiemCanhBao()
        );       
    }
    
    public void update(NguyenLieu nl) {
        String sql = "UPDATE KHO_NGUYEN_LIEU SET TenNL=?, DonViTinh=?, SoLuongTon=?, DiemCanhBao=? WHERE MaNL=?";
        JdbcHelper.executeUpdate(sql,
                nl.getTenNL(),
                nl.getDonViTinh(),
                nl.getSoLuongTon(),
                nl.getDiemCanhBao(),
                nl.getMaNL()
        );
    }
    
    public void delete(String maNL) {
        String sql = "DELETE FROM KHO_NGUYEN_LIEU WHERE MaNL=?";
        JdbcHelper.executeUpdate(sql, maNL);
    }
    
    protected List<NguyenLieu> selectBySql(String sql, Object... args) {
        List<NguyenLieu> list = new ArrayList<>();
        try (Connection con = JdbcHelper.getConnection();
                ResultSet rs = JdbcHelper.executeQuery(con, sql, args)) {
            while (rs.next()) {
                NguyenLieu nl = new NguyenLieu();
                nl.setMaNL(rs.getString("MaNL"));
                nl.setTenNL(rs.getString("TenNL"));
                nl.setDonViTinh(rs.getString("DonViTinh"));
                nl.setSoLuongTon(rs.getDouble("SoLuongTon"));
                nl.setDiemCanhBao(rs.getDouble("DiemCanhBao"));
                list.add(nl);
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    public List<NguyenLieu> selectAll() {
        String sql = "SELECT * FROM KHO_NGUYEN_LIEU";
        return selectBySql(sql);
    }
}
