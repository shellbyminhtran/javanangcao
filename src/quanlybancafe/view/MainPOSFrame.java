/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package quanlybancafe.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import quanlybancafe.model.NhanVien;
import quanlybancafe.dao.SanPhamDAO;
import quanlybancafe.helper.JdbcHelper;
import quanlybancafe.model.SanPham;

/**
 *
 * @author minhs
 */
public class MainPOSFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainPOSFrame.class.getName());
    private NhanVien nvHienTai;

    /**
     * Creates new form MainPOSFrame
     */
    public MainPOSFrame() {
        initComponents();
        this.setLocationRelativeTo(null);
    }
    
    private Integer maKH_Chon = null;

    public MainPOSFrame(NhanVien nv) {
        initComponents();
        this.nvHienTai = nv;
        this.setLocationRelativeTo(null);

        btnVoid.addActionListener(e -> voidItem());
        btnClear.addActionListener(e -> clearOrder());

        btnVisa.addActionListener(e -> thanhToan("Visa"));
        btnASPay.addActionListener(e -> thanhToan("ASPay"));
        btnCard.addActionListener(e -> thanhToan("Thẻ"));
        btnQR.addActionListener(e -> thanhToan("QR"));
        
        initCategoryEvents();
        loadProducts(1);
    }
    
    private void setupTable() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"STT", "Mã SP", "Tên món", "SL", "Giá", "Thành tiền"});
        tblOrder.setModel(model);
    }
    
    private void initCategoryEvents() {
        btnEnC.addActionListener(e -> loadProducts(1));
        btnVN.addActionListener(e -> loadProducts(2));
        btnFnM.addActionListener(e -> loadProducts(3));
        btnTnR.addActionListener(e -> loadProducts(4));
        
        btnExit.addActionListener(e -> {
            new LoginFramePOS().setVisible(true);
            this.dispose();
        });
    }

    private void loadProducts(int maDM) {
        pnlProducts.removeAll();

        try {
            SanPhamDAO spDao = new SanPhamDAO();
            List<SanPham> list = spDao.selectAll(); 

            int count = 0;
            for (SanPham sp : list) {

                if (sp.getMaDM() == maDM && sp.isTrangThai()) {
                    JButton btn = new JButton("<html><center>" + sp.getTenSP() + "<br>" + 
                                             String.format("%,.0f", sp.getGiaBan()) + "</center></html>");
                    btn.setPreferredSize(new java.awt.Dimension(120, 80));
                    btn.setBackground(java.awt.Color.WHITE);
                    btn.setFocusable(false);

                    btn.addActionListener(e -> addToOrder(sp));

                    pnlProducts.add(btn);
                    count++;
                }
            }

            if (count == 0) {
                pnlProducts.add(new javax.swing.JLabel("Danh mục này hiện chưa có món nào!"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        pnlProducts.revalidate();
        pnlProducts.repaint();
    }
    
    private void addToOrder(SanPham sp) {
        DefaultTableModel model = (DefaultTableModel) tblOrder.getModel();
        boolean found = false;

        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, 1); // Lấy giá trị cột Mã SP

            if (value == null) continue; 

            String maInTable = value.toString();
            if (maInTable.equals(sp.getMaSP())) {
                int currentQty = Integer.parseInt(model.getValueAt(i, 3).toString());
                model.setValueAt(currentQty + 1, i, 3);

                double price = Double.parseDouble(model.getValueAt(i, 4).toString());
                model.setValueAt((currentQty + 1) * price, i, 5);
                found = true;
                break;
            }
        }

        if (!found) {
            int stt = model.getRowCount() + 1;
            model.addRow(new Object[]{
                stt, 
                sp.getMaSP(), 
                sp.getTenSP(), 
                1, 
                sp.getGiaBan(), 
                sp.getGiaBan()
            });
        }
        updateTotal();
    }
    
    private void updateTotal() {
        double total = 0;
        DefaultTableModel model = (DefaultTableModel) tblOrder.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            total += Double.parseDouble(model.getValueAt(i, 5).toString());
        }
        lblTotal.setText(String.format("TỔNG TIỀN: %,.0f VND", total));
    }
    
    private void xuLyTimKhachHang() {
        String input = JOptionPane.showInputDialog(this, "Nhập Tên hoặc Email khách hàng:");
        if (input == null || input.trim().isEmpty()) {
            maKH_Chon = null; 
            return;
        }

        try {
            // SQL bám theo bảng KHACH_HANG (MaKH, TenKH, Email)
            String sql = "SELECT MaKH, TenKH, Email FROM KHACH_HANG WHERE TenKH LIKE ? OR Email LIKE ?";
            String param = "%" + input.trim() + "%";

            Connection con = JdbcHelper.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            ps.setString(2, param);
            java.sql.ResultSet rs = ps.executeQuery();

            java.util.List<Object[]> listKH = new java.util.ArrayList<>();
            while (rs.next()) {
                listKH.add(new Object[]{
                    rs.getInt("MaKH"), 
                    rs.getNString("TenKH"), 
                    rs.getString("Email")
                });
            }

            if (listKH.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng. Tính cho khách vãng lai.");
                maKH_Chon = null;
            } else if (listKH.size() == 1) {
                maKH_Chon = (Integer) listKH.get(0)[0];
                JOptionPane.showMessageDialog(this, "Đã chọn khách hàng: " + listKH.get(0)[1]);
            } else {
                maKH_Chon = hienBangChonKhachHang(listKH);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Integer hienBangChonKhachHang(java.util.List<Object[]> list) {
        String[] columns = {"Mã KH", "Tên khách hàng", "SĐT", "Email"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (Object[] kh : list) {
            model.addRow(kh);
        }

        javax.swing.JTable table = new javax.swing.JTable(model);
        int result = JOptionPane.showConfirmDialog(this, new javax.swing.JScrollPane(table), 
                     "Tìm thấy nhiều khách hàng, vui lòng chọn:", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && table.getSelectedRow() >= 0) {
            return (Integer) table.getValueAt(table.getSelectedRow(), 0);
        }
        return null;
    }
    
    private void thanhToan(String hinhThuc) {
        DefaultTableModel model = (DefaultTableModel) tblOrder.getModel();
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống!");
            return;
        }

        xuLyTimKhachHang();

        String maHD = "HD" + System.currentTimeMillis();
        String thoiGian = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());
        String tenNV = (nvHienTai != null) ? nvHienTai.getTenNV() : "Chưa đăng nhập";

        double tongGoc = 0;
        double tongGiam = 0;
        int giamMoiMon = hinhThuc.equals("Visa") ? 12000 : (hinhThuc.equals("ASPay") ? 25000 : 0);

        StringBuilder chiTiet = new StringBuilder();
        for (int i = 0; i < model.getRowCount(); i++) {
            String tenSP = model.getValueAt(i, 2).toString();
            int sl = Integer.parseInt(model.getValueAt(i, 3).toString());
            double giaGoc = Double.parseDouble(model.getValueAt(i, 4).toString());

            double giamHienTai = sl * giamMoiMon;
            double thanhTienDòng = (sl * giaGoc) - giamHienTai;

            tongGoc += (sl * giaGoc);
            tongGiam += giamHienTai;

            chiTiet.append(String.format("- %s | SL: %d | Giá: %,.0f | Giảm: %,.0f\n", 
                            tenSP, sl, giaGoc, giamHienTai));
        }

        double tongPhaiTra = Math.max(0, tongGoc - tongGiam);

        String billInfo = String.format(
            "MÃ HD: %s\nThời gian: %s\nNV: %s\nKH: %s\n" +
            "------------------------------------------\n" +
            "%s" +
            "------------------------------------------\n" +
            "TỔNG TIỀN GỐC: %,.0f VND\n" +
            "TỔNG GIẢM GIÁ: %,.0f VND\n" +
            "THỰC THU (%s): %,.0f VND",
            maHD, thoiGian, tenNV, (maKH_Chon == null ? "Khách vãng lai" : "Mã KH: " + maKH_Chon), 
            chiTiet.toString(), tongGoc, tongGiam, hinhThuc, tongPhaiTra
        );

        int confirm = JOptionPane.showConfirmDialog(this, billInfo, "Xác nhận thanh toán", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            saveInvoiceToDatabase(maHD, maKH_Chon, hinhThuc, tongPhaiTra);
            
            String billContent = formatInvoiceText(maHD, maKH_Chon, hinhThuc, tongPhaiTra);
            exportInvoiceFile(maHD, billContent);

            JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
            clearOrder();
        }
    }
    
    private void voidItem() {
        int row = tblOrder.getSelectedRow();
        if (row >= 0) {
            DefaultTableModel model = (DefaultTableModel) tblOrder.getModel();
            model.removeRow(row);

            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt(i + 1, i, 0); 
            }

            updateTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 sản phẩm trên bảng để xóa!");
        }
    }

    private void clearOrder() {
        DefaultTableModel model = (DefaultTableModel) tblOrder.getModel();
        model.setRowCount(0);
        updateTotal();
    }
    
    private void saveInvoiceToDatabase(String maHD, Integer maKH, String hinhThuc, double tongPhaiTra) {
        String sqlHD = "INSERT INTO HOA_DON (MaHD, MaNV, MaKH, NgayLap, TongTien, TrangThaiThanhToan) VALUES (?, ?, ?, GETDATE(), ?, ?)";
        String sqlCT = "INSERT INTO Chi_Tiet_Hoa_Don (MaHD, MaSP, SoLuong, DonGia, ThanhTien) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = JdbcHelper.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement psHD = con.prepareStatement(sqlHD)) {
                psHD.setString(1, maHD);
                psHD.setString(2, nvHienTai.getMaNV());
                if (maKH != null) psHD.setInt(3, maKH); 
                else psHD.setNull(3, java.sql.Types.INTEGER);
                psHD.setDouble(4, tongPhaiTra);
                psHD.setNString(5, "Đã thanh toán (" + hinhThuc + ")");
                psHD.executeUpdate();
            }

            DefaultTableModel model = (DefaultTableModel) tblOrder.getModel();
            try (PreparedStatement psCT = con.prepareStatement(sqlCT)) {
                for (int i = 0; i < model.getRowCount(); i++) {
                    psCT.setString(1, maHD);
                    psCT.setString(2, model.getValueAt(i, 1).toString()); // MaSP
                    psCT.setInt(3, Integer.parseInt(model.getValueAt(i, 3).toString())); // SoLuong
                    psCT.setDouble(4, Double.parseDouble(model.getValueAt(i, 4).toString())); // DonGia
                    psCT.setDouble(5, Double.parseDouble(model.getValueAt(i, 5).toString())); // ThanhTien
                    psCT.addBatch();
                }
                psCT.executeBatch();
            }

            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi Database: " + e.getMessage());
        }
    }
    
    private String formatInvoiceText(String maHD, Integer maKH, String hinhThuc, double tongPhaiTra) {
        StringBuilder sb = new StringBuilder();
        sb.append("==========================================\n");
        sb.append("            STARBUCKS TRIỀU KHÚC          \n");
        sb.append("==========================================\n");
        sb.append("Mã HĐ: ").append(maHD).append("\n");
        sb.append("Ngày: ").append(new java.util.Date()).append("\n");
        sb.append("Nhân viên: ").append(nvHienTai.getTenNV()).append("\n");
        sb.append("Khách hàng: ").append(maKH == null ? "Khách vãng lai" : maKH).append("\n");
        sb.append("------------------------------------------\n");
        sb.append(String.format("%-20s %-5s %-15s\n", "Tên món", "SL", "Thành tiền"));

        DefaultTableModel model = (DefaultTableModel) tblOrder.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String ten = model.getValueAt(i, 2).toString();
            String sl = model.getValueAt(i, 3).toString();
            String tt = model.getValueAt(i, 5).toString();
            sb.append(String.format("%-20s %-5s %-15s\n", ten, sl, tt));
        }

        sb.append("------------------------------------------\n");
        sb.append("Hình thức: ").append(hinhThuc).append("\n");
        sb.append("THỰC THU: ").append(String.format("%,.0f", tongPhaiTra)).append(" VND\n");
        sb.append("==========================================\n");
        sb.append("      CẢM ƠN QUÝ KHÁCH - HẸN GẶP LẠI      \n");

        return sb.toString();
    }
    
    private void exportInvoiceFile(String maHD, String content) {
        try {
            java.io.File folder = new java.io.File("invoices");
            if (!folder.exists()) folder.mkdir();
            
            java.io.FileWriter writer = new java.io.FileWriter("invoices/" + maHD + ".txt");
            writer.write(content);
            writer.close();
            
            System.out.println("Đã xuất hóa đơn hiện tại: " + folder.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlContainer = new javax.swing.JPanel();
        pnlOrder = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblOrder = new javax.swing.JTable();
        lblTotal = new javax.swing.JLabel();
        pnlMenu = new javax.swing.JPanel();
        pnlCategory = new javax.swing.JPanel();
        btnEnC = new javax.swing.JButton();
        btnFnM = new javax.swing.JButton();
        btnTnR = new javax.swing.JButton();
        btnVN = new javax.swing.JButton();
        pnlProducts = new javax.swing.JPanel();
        pnlFunctions = new javax.swing.JPanel();
        btnClear = new javax.swing.JButton();
        btnVoid = new javax.swing.JButton();
        btnBill = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        pnlPayment = new javax.swing.JPanel();
        btnCard = new javax.swing.JButton();
        btnVisa = new javax.swing.JButton();
        btnASPay = new javax.swing.JButton();
        btnQR = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlContainer.setLayout(new java.awt.GridLayout(2, 2, 5, 5));

        pnlOrder.setLayout(new java.awt.BorderLayout());

        tblOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã SP", "Tên món", "SL", "Giá", "Thành tiền"
            }
        ));
        jScrollPane1.setViewportView(tblOrder);

        pnlOrder.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        lblTotal.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(255, 51, 0));
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal.setText("TỔNG TIỀN: 0 VND");
        pnlOrder.add(lblTotal, java.awt.BorderLayout.PAGE_END);

        pnlContainer.add(pnlOrder);

        pnlMenu.setLayout(new java.awt.BorderLayout());

        pnlCategory.setLayout(new java.awt.GridLayout(2, 2, 10, 10));

        btnEnC.setText("Espresso & Coffee");
        pnlCategory.add(btnEnC);

        btnFnM.setText("Frappuccino & More");
        pnlCategory.add(btnFnM);

        btnTnR.setText("Tea & Refreshment");
        pnlCategory.add(btnTnR);

        btnVN.setText("Vietnam Only");
        btnVN.addActionListener(this::btnVNActionPerformed);
        pnlCategory.add(btnVN);

        pnlMenu.add(pnlCategory, java.awt.BorderLayout.PAGE_START);

        pnlProducts.setLayout(new java.awt.GridLayout(0, 3, 10, 10));
        pnlMenu.add(pnlProducts, java.awt.BorderLayout.CENTER);

        pnlContainer.add(pnlMenu);

        btnClear.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnClear.setText("CLEAR");
        pnlFunctions.add(btnClear);

        btnVoid.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnVoid.setForeground(new java.awt.Color(0, 204, 0));
        btnVoid.setText("VOID");
        pnlFunctions.add(btnVoid);

        btnBill.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnBill.setText("BILL");
        btnBill.addActionListener(this::btnBillActionPerformed);
        pnlFunctions.add(btnBill);

        btnExit.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnExit.setForeground(new java.awt.Color(255, 204, 0));
        btnExit.setText("EXIT");
        btnExit.addActionListener(this::btnExitActionPerformed);
        pnlFunctions.add(btnExit);

        pnlContainer.add(pnlFunctions);

        pnlPayment.setLayout(new java.awt.GridLayout(2, 2));

        btnCard.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnCard.setText("Card");
        pnlPayment.add(btnCard);

        btnVisa.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnVisa.setText("Visa");
        pnlPayment.add(btnVisa);

        btnASPay.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnASPay.setText("Apple/SamSung Pay");
        pnlPayment.add(btnASPay);

        btnQR.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnQR.setText("QR");
        pnlPayment.add(btnQR);

        pnlContainer.add(pnlPayment);

        getContentPane().add(pnlContainer, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVNActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed

            // 2. Mở lại màn hình LoginFramePOS
            LoginFramePOS login = new LoginFramePOS();
            login.setVisible(true);
            login.setLocationRelativeTo(null);

            // 3. Đóng màn hình POS hiện tại
            this.dispose();
        
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillActionPerformed
        BillHistoryDialog dialog = new BillHistoryDialog(this, true);
        dialog.loadInvoiceList();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }//GEN-LAST:event_btnBillActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainPOSFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnASPay;
    private javax.swing.JButton btnBill;
    private javax.swing.JButton btnCard;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnEnC;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnFnM;
    private javax.swing.JButton btnQR;
    private javax.swing.JButton btnTnR;
    private javax.swing.JButton btnVN;
    private javax.swing.JButton btnVisa;
    private javax.swing.JButton btnVoid;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JPanel pnlCategory;
    private javax.swing.JPanel pnlContainer;
    private javax.swing.JPanel pnlFunctions;
    private javax.swing.JPanel pnlMenu;
    private javax.swing.JPanel pnlOrder;
    private javax.swing.JPanel pnlPayment;
    private javax.swing.JPanel pnlProducts;
    private javax.swing.JTable tblOrder;
    // End of variables declaration//GEN-END:variables
}
