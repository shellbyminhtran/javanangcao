/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlybancafe.helper;

import java.sql.*;
import javax.swing.JOptionPane;
/**
 *
 * @author minhs
 */
public class JdbcHelper {
    private static final String SERVER_NAME = "MINHTRAN";
    private static final String DB_NAME = "QuanLyBanCafe";
    
    private static final String URL = "jdbc:sqlserver://" + SERVER_NAME +
                                      ";databaseName=" + DB_NAME + 
                                      ";integratedSecurity=true" + 
                                      ";encrypt=true;trustServerCertificate=true;";
    

    /**
     * @return 
     * @throws java.sql.SQLException
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Thiếu Driver JDBC");
        }
        return DriverManager.getConnection(URL);
    }
    
    /**
     * @param con
     * @param sql
     * @param args
     * @return 
     * @throws java.sql.SQLException
     */
    public static PreparedStatement getStmt(Connection con, String sql, Object... args) throws SQLException {        PreparedStatement ps = con.prepareStatement(sql);
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
        }
        return ps;
    }
    
    public static ResultSet executeQuery(Connection con, String sql, Object... args) throws SQLException {
        PreparedStatement ps = getStmt(con, sql, args); 
        return ps.executeQuery();
    }
    
    /**
     * Thực hiện truy vấn INSERT, UPDATE, DELETE và trả về số dòng bị ảnh hưởng.
     * @param sql
     * @param args
     * @return 
     */
    public static int executeUpdate(String sql, Object... args) {
        try (Connection con = getConnection();
                PreparedStatement ps = getStmt(con, sql, args)) {
            return ps.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi SQL: " + ex.getMessage(), "Lỗi Database", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return -1;
        }
    }

//    public static ResultSet executeQuery(String sql, Object[] args) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//
//    public static ResultSet executeQuery(String sql, int thang, int nam) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//
//    public static ResultSet executeQuery(String SELECT_ALL_SQL) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//
//    public static ResultSet executeQuery(String sql, int maKH) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//
//    public static ResultSet executeQuery(String sql, String maNV, String matKhau) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//
//    public static ResultSet executeQuery(String sql, String maHD) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
}