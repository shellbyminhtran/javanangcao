/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlybancafe.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import quanlybancafe.helper.JdbcHelper;
import quanlybancafe.model.LoaiHinhLV;
/**
 *
 * @author minhs
 */
public class LoaiHinhLVDAO {
    public List<LoaiHinhLV> selectAll() {
        String sql = "SELECT * FROM LOAI_HINH_LV";
        List<LoaiHinhLV> list = new ArrayList<>();
        try (java.sql.Connection con = JdbcHelper.getConnection();
             ResultSet rs = JdbcHelper.executeQuery(con, sql)) {
            while (rs.next()) {
                LoaiHinhLV model = new LoaiHinhLV();
                model.setMaLHLV(rs.getInt("MaLHLV"));
                model.setTenLHLV(rs.getString("TenLHLV"));
                model.setMucLuongCoDinh(rs.getDouble("MucLuongCoDinh"));
                model.setMucLuongTheoGio(rs.getDouble("MucLuongTheoGio"));
                list.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
