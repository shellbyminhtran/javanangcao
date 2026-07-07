/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package quanlybancafe.view;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import quanlybancafe.model.KhachHang;

/**
 *
 * @author minhs
 */
public class CustomerPanel extends javax.swing.JPanel {

    private final quanlybancafe.dao.KhachHangDAO khDao = new quanlybancafe.dao.KhachHangDAO();
    private int row = -1;
    
        /**
     * Creates new form CustomerPanel
     */
    public CustomerPanel() {
        initComponents();
        initLogic();
    }

    private void initLogic() {
        dongBoDiemTichLuy();
        fillTableKhachHang();
                
        jButton1.addActionListener(e -> insert());
        jButton2.addActionListener(e -> update());
        jButton3.addActionListener(e -> delete());
        jButton4.addActionListener(e -> clearForm());
    }
    
    private void fillTableKhachHang() {
        DefaultTableModel model = (DefaultTableModel) tblKhachHang.getModel();
        model.setRowCount(0);
        try {
            List<quanlybancafe.model.KhachHang> list = khDao.selectAll(); 
            for (quanlybancafe.model.KhachHang kh : list) {
                model.addRow(new Object[]{
                    kh.getMaKH(), kh.getTenKH(), kh.getEmail(), kh.getDiemTichLuy()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillForm() {
        txtMaKH.setText(tblKhachHang.getValueAt(row, 0).toString());
        txtTenKH.setText(tblKhachHang.getValueAt(row, 1).toString());
        txtEmail.setText(tblKhachHang.getValueAt(row, 2).toString());

        int maKH = Integer.parseInt(tblKhachHang.getValueAt(row, 0).toString());
        int diem = Integer.parseInt(tblKhachHang.getValueAt(row, 3).toString());

        lblDiem.setText("Điểm hiện có: " + diem);
        lblDiemTichLuy.setText("Điểm Tích Lũy: " + diem);

        double tongChi = khDao.getTongChiTieu(maKH);
        String monThich = khDao.getMonYeuThich(maKH);

        lblTongChi.setText(String.format("Tổng chi tiêu: %,.0f VNĐ", tongChi));
        lblMonYeuThich.setText("Món yêu thích: " + monThich);

        fillTableLichSu(maKH);
    }

    private void fillTableLichSu(int maKH) {
        DefaultTableModel model = (DefaultTableModel) tblLichSuHD.getModel();
        model.setRowCount(0);
        String sql = "SELECT MaHD, NgayLap, TongTien, (TongTien/60000) as DiemCong " +
                         "FROM HOA_DON WHERE MaKH = ? ORDER BY NgayLap DESC";
        try (java.sql.Connection con = quanlybancafe.helper.JdbcHelper.getConnection();
            java.sql.ResultSet rs = quanlybancafe.helper.JdbcHelper.executeQuery(con, sql, maKH)) {
        
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString(1), 
                    rs.getTimestamp(2), 
                    rs.getDouble(3), 
                    rs.getInt(4)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private KhachHang getForm() {
        KhachHang kh = new KhachHang();
        if (!txtMaKH.getText().isEmpty()) {
            kh.setMaKH(Integer.parseInt(txtMaKH.getText()));
        }
        kh.setTenKH(txtTenKH.getText().trim());
        kh.setEmail(txtEmail.getText().trim());
        kh.setMatKhau("1");
        return kh;
    }
    
    private void insert() {
        if (txtTenKH.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Không được để trống Tên và Email!");
            return;
        }

        KhachHang kh = getForm();
        try {
            khDao.insert(kh);
            fillTableKhachHang();
            clearForm();
            javax.swing.JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi: Email này đã tồn tại!");
        }
    }
    
    private void update() {
        KhachHang kh = getForm();
        try {
            khDao.update(kh);
            fillTableKhachHang();
            javax.swing.JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi cập nhật!");
        }
    }
    
    private void delete() {
        if (txtMaKH.getText().isEmpty()) return;
        int maKH = Integer.parseInt(txtMaKH.getText());
        if (javax.swing.JOptionPane.showConfirmDialog(this, "Xóa khách hàng này?") == 0) {
            try {
                khDao.delete(maKH);
                fillTableKhachHang();
                clearForm();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Không thể xóa khách đã có lịch sử mua hàng!");
            }
        }
    }
    
    private void clearForm() {
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtEmail.setText("");
        lblDiem.setText("Điểm hiện có: 0");
        lblTongChi.setText("Tổng chi tiêu: 0 VNĐ");
        lblMonYeuThich.setText("Món yêu thích: N/A");
        ((DefaultTableModel) tblLichSuHD.getModel()).setRowCount(0);
        row = -1;
    }

    private void dongBoDiemTichLuy() {
        String sql = "UPDATE KHACH_HANG SET DiemTichLuy = ISNULL((SELECT SUM(TongTien)/60000 FROM HOA_DON WHERE HOA_DON.MaKH = KHACH_HANG.MaKH), 0)";
        try {
            quanlybancafe.helper.JdbcHelper.executeUpdate(sql);
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

        pnlInput = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtMaKH = new javax.swing.JTextField();
        txtTenKH = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        lblDiemTichLuy = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKhachHang = new javax.swing.JTable();
        pnlDateails = new javax.swing.JPanel();
        pnlCard = new javax.swing.JPanel();
        lblDiem = new javax.swing.JLabel();
        lblTongChi = new javax.swing.JLabel();
        lblMonYeuThich = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblLichSuHD = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jLabel1.setText("Mã Khách Hàng:");

        jLabel2.setText("Tên Khách Hàng:");

        jLabel3.setText("Email:");

        txtMaKH.setEditable(false);
        txtMaKH.setPreferredSize(new java.awt.Dimension(200, 25));

        txtTenKH.setPreferredSize(new java.awt.Dimension(200, 25));

        txtEmail.setPreferredSize(new java.awt.Dimension(200, 25));

        lblDiemTichLuy.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblDiemTichLuy.setForeground(new java.awt.Color(51, 153, 0));
        lblDiemTichLuy.setText("Điểm Tích Lũy:");

        javax.swing.GroupLayout pnlInputLayout = new javax.swing.GroupLayout(pnlInput);
        pnlInput.setLayout(pnlInputLayout);
        pnlInputLayout.setHorizontalGroup(
            pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDiemTichLuy)
                    .addGroup(pnlInputLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlInputLayout.setVerticalGroup(
            pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDiemTichLuy)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        add(pnlInput, java.awt.BorderLayout.PAGE_START);

        jPanel1.setLayout(new java.awt.GridLayout(4, 1));

        jButton1.setMnemonic('a');
        jButton1.setText("Thêm");
        jButton1.addActionListener(this::jButton1ActionPerformed);
        jPanel1.add(jButton1);

        jButton2.setText("Sửa");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        jPanel1.add(jButton2);

        jButton3.setText("Xóa");
        jPanel1.add(jButton3);

        jButton4.setMnemonic('n');
        jButton4.setText("Mới");
        jPanel1.add(jButton4);

        add(jPanel1, java.awt.BorderLayout.LINE_START);

        jSplitPane1.setDividerLocation(250);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setOneTouchExpandable(true);

        tblKhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã KH", "Tên KH", "Email", "Điểm Tích Lũy"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKhachHangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblKhachHang);

        jSplitPane1.setTopComponent(jScrollPane1);

        pnlDateails.setLayout(new java.awt.BorderLayout());

        pnlCard.setLayout(new java.awt.GridLayout(3, 1, 0, 10));

        lblDiem.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblDiem.setForeground(new java.awt.Color(0, 102, 51));
        lblDiem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDiem.setText("Điểm hiện có:");
        lblDiem.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlCard.add(lblDiem);

        lblTongChi.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblTongChi.setForeground(new java.awt.Color(204, 0, 0));
        lblTongChi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTongChi.setText("Tổng chi tiêu:");
        lblTongChi.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlCard.add(lblTongChi);

        lblMonYeuThich.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblMonYeuThich.setForeground(new java.awt.Color(0, 0, 153));
        lblMonYeuThich.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMonYeuThich.setText("Món yêu thích:");
        lblMonYeuThich.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlCard.add(lblMonYeuThich);

        pnlDateails.add(pnlCard, java.awt.BorderLayout.LINE_START);

        tblLichSuHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã HD", "Ngày lập", "Tổng tiền", "Điểm cộng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblLichSuHD);

        pnlDateails.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(pnlDateails);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tblKhachHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKhachHangMouseClicked
        // TODO add your handling code here:
        row = tblKhachHang.getSelectedRow();
        if (row >= 0) fillForm();
    }//GEN-LAST:event_tblKhachHangMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lblDiem;
    private javax.swing.JLabel lblDiemTichLuy;
    private javax.swing.JLabel lblMonYeuThich;
    private javax.swing.JLabel lblTongChi;
    private javax.swing.JPanel pnlCard;
    private javax.swing.JPanel pnlDateails;
    private javax.swing.JPanel pnlInput;
    private javax.swing.JTable tblKhachHang;
    private javax.swing.JTable tblLichSuHD;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtTenKH;
    // End of variables declaration//GEN-END:variables

}
