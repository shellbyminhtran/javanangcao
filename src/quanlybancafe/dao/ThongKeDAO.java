/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlybancafe.dao;

/**
 *
 * @author minhs
 */
public class ThongKeDAO {
    public static final String SQL_TIEN_CHO_CHOT = 
        "SELECT SUM(TongTien) FROM HOA_DON WHERE DaTongKet = 0";

    public static final String SQL_DOANH_THU = 
        "SELECT SUM(TongTien) FROM HOA_DON WHERE CONVERT(DATE, NgayLap) = CONVERT(DATE, GETDATE())";

    public static final String SQL_TONG_HD = 
        "SELECT COUNT(*) FROM HOA_DON WHERE CONVERT(DATE, NgayLap) = CONVERT(DATE, GETDATE())";

    public static final String SQL_BEST_SELLER = 
        "SELECT TOP 1 s.TenSP FROM Chi_Tiet_Hoa_Don c " +
        "JOIN SAN_PHAM s ON c.MaSP = s.MaSP " +
        "GROUP BY s.TenSP ORDER BY SUM(c.SoLuong) DESC";

    public static final String SQL_CHI_TIET_THANG = 
        "SELECT CONVERT(DATE, NgayLap) as Ngay, COUNT(MaHD) as SoDon, SUM(TongTien) as DoanhThu " +
        "FROM HOA_DON WHERE MONTH(NgayLap) = MONTH(GETDATE()) AND YEAR(NgayLap) = YEAR(GETDATE()) " +
        "GROUP BY CONVERT(DATE, NgayLap) ORDER BY Ngay DESC";
}