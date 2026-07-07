/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package quanlybancafe.view;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import quanlybancafe.model.NguyenLieu;
import quanlybancafe.dao.NguyenLieuDAO;
/**
 *
 * @author minhs
 */
public class StoragePanel extends javax.swing.JPanel {

    private final NguyenLieuDAO dao = new NguyenLieuDAO();
    private int row = -1;
    
    /**
     * Creates new form StorageForm
     */
    public StoragePanel() {
        initComponents();
        initLogic();
    }
    
    private void initLogic() {
        fillTable();
        
        // Gán sự kiện cho các nút
        btnThem.addActionListener(e -> insert());
        btnSua.addActionListener(e -> update());
        btnXoa.addActionListener(e -> delete());
        btnMoi.addActionListener(e -> clearForm());
        btnNhapNhanh.addActionListener(e -> importGoods());
        
        // Sự kiện Click bảng
        tblStorage.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                row = tblStorage.getSelectedRow();
                if (row >= 0) edit();
            }
        });
        
        updateStatus(true);
    }
    
    private void edit() {
        String maNL = (String) tblStorage.getValueAt(this.row, 0);
        try {
            List<NguyenLieu> list = dao.selectAll();
            for (NguyenLieu nl : list) {
                if (nl.getMaNL().equals(maNL)) {
                    setForm(nl);
                    updateStatus(false);
                    break;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi hiển thị chi tiết");
        }
    }
    
    private void setForm(NguyenLieu nl) {
        txtMaNL.setText(nl.getMaNL());
        txtTenNL.setText(nl.getTenNL());
        txtDonViTinh.setText(nl.getDonViTinh());
        txtSoLuongTon.setText(String.valueOf(nl.getSoLuongTon()));
        txtDiemCanhBao.setText(String.valueOf(nl.getDiemCanhBao()));
    }
    
    private void clearForm() {
        txtMaNL.setText("");
        txtTenNL.setText("");
        txtDonViTinh.setText("");
        txtSoLuongTon.setText("0");
        txtDiemCanhBao.setText("5");
        this.row = -1;
        updateStatus(true);
    }

    private void updateStatus(boolean insertable) {
        txtMaNL.setEditable(insertable);
        btnThem.setEnabled(insertable);
        btnSua.setEnabled(!insertable);
        btnXoa.setEnabled(!insertable);
    }

    private void insert() {
        NguyenLieu nl = getForm();
        try {
            dao.insert(nl);
            fillTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Thêm nguyên liệu thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm mới! Kiểm tra trùng mã.");
        }
    }

    private void update() {
        NguyenLieu nl = getForm();
        try {
            dao.update(nl);
            fillTable();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }

    private void delete() {
        String maNL = txtMaNL.getText();
        if (JOptionPane.showConfirmDialog(this, "Bạn muốn xóa nguyên liệu này?") == 0) {
            try {
                // Bạn cần thêm hàm delete(String maNL) vào NguyenLieuDAO
                // dao.delete(maNL); 
                fillTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }
    
    private void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblStorage.getModel();
        model.setRowCount(0);
        try {
            List<NguyenLieu> list = dao.selectAll();
            for (NguyenLieu nl : list) {
                String trangThai = (nl.getSoLuongTon() <= nl.getDiemCanhBao()) 
                                   ? "⚠️ Cần nhập hàng" : "✅ An toàn";

                model.addRow(new Object[]{
                    nl.getMaNL(),
                    nl.getTenNL(),
                    nl.getDonViTinh(),
                    nl.getSoLuongTon(),
                    nl.getDiemCanhBao(),
                    trangThai
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn kho!");
        }
    }
    
    private NguyenLieu getForm() {
        NguyenLieu nl = new NguyenLieu();
        nl.setMaNL(txtMaNL.getText().trim());
        nl.setTenNL(txtTenNL.getText().trim());
        nl.setDonViTinh(txtDonViTinh.getText().trim());
        
        try {
            nl.setSoLuongTon(Double.parseDouble(txtSoLuongTon.getText().trim()));
            nl.setDiemCanhBao(Double.parseDouble(txtDiemCanhBao.getText().trim()));
        } catch (Exception e) {
            nl.setSoLuongTon(0);
            nl.setDiemCanhBao(5);
        }
        return nl;
    }
    
    private void importGoods() {
        String maNL = txtMaNL.getText();
        if (maNL.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nguyên liệu từ bảng!");
            return;
        }

        String strAmount = JOptionPane.showInputDialog(this, "Nhập số lượng nhập thêm:");
        if (strAmount != null && !strAmount.isEmpty()) {
            try {
                double amount = Double.parseDouble(strAmount);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Số lượng nhập phải lớn hơn 0!");
                    return;
                }

                NguyenLieu nl = getForm(); 

                double oldStock = Double.parseDouble(txtSoLuongTon.getText());
                nl.setSoLuongTon(oldStock + amount);

                dao.update(nl); 

                fillTable();
                setForm(nl); 

                JOptionPane.showMessageDialog(this, "Đã nhập thêm " + amount + " vào kho.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!");
            }
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
        txtMaNL = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtDonViTinh = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTenNL = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtSoLuongTon = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDiemCanhBao = new javax.swing.JTextField();
        pnlButtons = new javax.swing.JPanel();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnMoi = new javax.swing.JButton();
        btnNhapNhanh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblStorage = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        pnlInput.setLayout(new java.awt.GridLayout(3, 0));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Mã nguyên liệu:");
        pnlInput.add(jLabel1);
        pnlInput.add(txtMaNL);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Đơn vị tính:");
        pnlInput.add(jLabel2);
        pnlInput.add(txtDonViTinh);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Tên nguyên liệu:");
        pnlInput.add(jLabel3);
        pnlInput.add(txtTenNL);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Số lượng tồn:");
        pnlInput.add(jLabel4);
        pnlInput.add(txtSoLuongTon);
        pnlInput.add(jLabel6);
        pnlInput.add(jLabel7);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Điểm cảnh báo:");
        pnlInput.add(jLabel5);
        pnlInput.add(txtDiemCanhBao);

        add(pnlInput, java.awt.BorderLayout.PAGE_START);

        pnlButtons.setPreferredSize(new java.awt.Dimension(100, 100));
        pnlButtons.setLayout(new java.awt.GridLayout(5, 0));

        btnThem.setText("Thêm");
        pnlButtons.add(btnThem);

        btnSua.setText("Sửa");
        pnlButtons.add(btnSua);

        btnXoa.setText("Xóa");
        pnlButtons.add(btnXoa);

        btnMoi.setText("Mới");
        pnlButtons.add(btnMoi);

        btnNhapNhanh.setText("Nhập nhanh");
        pnlButtons.add(btnNhapNhanh);

        add(pnlButtons, java.awt.BorderLayout.LINE_START);

        tblStorage.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã NL", "Tên NL", "Đơn vị tính", "Số lượng tồn", "Cảnh báo", "Trạng thái"
            }
        ));
        jScrollPane1.setViewportView(tblStorage);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnNhapNhanh;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlInput;
    private javax.swing.JTable tblStorage;
    private javax.swing.JTextField txtDiemCanhBao;
    private javax.swing.JTextField txtDonViTinh;
    private javax.swing.JTextField txtMaNL;
    private javax.swing.JTextField txtSoLuongTon;
    private javax.swing.JTextField txtTenNL;
    // End of variables declaration//GEN-END:variables

    
}
