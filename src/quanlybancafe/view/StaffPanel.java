/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package quanlybancafe.view;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import quanlybancafe.dao.NhanVienDAO;
import quanlybancafe.model.NhanVien;

/**
 *
 * @author minhs
 */
public class StaffPanel extends javax.swing.JPanel {

    private final NhanVienDAO dao = new NhanVienDAO();
    private int row = -1;
    private int vaiTro;
    
    /**
     * Creates new form StaffPanel
     */
    public StaffPanel() {
        initComponents();
        initLogic();
    }

    private void initLogic() {
        fillTable();
        
        btnThem.addActionListener(e -> insert());
        btnSua.addActionListener(e -> update());
        btnXoa.addActionListener(e -> delete());
        btnTimKiem.addActionListener(e -> search());
        btnOpenSalary.addActionListener(e -> {
            new SalaryFrame().setVisible(true);
        });
        
        tblNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                row = tblNhanVien.getSelectedRow();
                if (row >= 0) {
                    edit();
                }
            }
        });
        clearForm();
    }
    
    private void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblNhanVien.getModel();
        model.setRowCount(0);

        String[] headers = {"Mã NV", "Mật Khẩu", "Họ tên", "Ngày Sinh", "SĐT", "Địa Chỉ", "Email", "Vai Trò", "Ngày Bắt Đầu", "Trạng Thái"};
        model.setColumnIdentifiers(headers);

        try {
            List<NhanVien> list = dao.selectAll();
            for (NhanVien nv : list) {
                model.addRow(new Object[]{
                    nv.getMaNV(),
                    nv.getMatKhau(),
                    nv.getTenNV(),
                    nv.getNgaySinh(),       
                    nv.getSdt(),            
                    nv.getDiaChi(),         
                    nv.getEmail(),         
                    getVaiTroString(nv.getVaiTro()),
                    nv.getNgayBatDauLam(),
                    nv.isTrangThai() ? "Đang làm việc" : "Đã nghỉ"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn dữ liệu table!");
        }
    }
    
    private String getVaiTroString(int vaiTro) {
        switch (vaiTro) {
            case 1: return "Quản lý";
            case 2: return "Nhân viên (Part time)";
            case 3: return "Nhân viên (Full time)";
            case 4: return "Trưởng ca (Part time)";
            case 5: return "Trưởng ca (Full time)";
            default: return "Khác (" + vaiTro + ")"; // Thêm + vaiTro để debug nếu vẫn lỗi
        }
    }
    private void edit() {
        String maNV = (String) tblNhanVien.getValueAt(this.row, 0);
        try {
            List<NhanVien> list = dao.selectAll();
            for (NhanVien nv : list) {
                if (nv.getMaNV().equals(maNV)) {
                    setForm(nv);
                    updateStatus(false);
                    break;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi hiển thị chi tiết");
        }
    }
    
    private void setForm(NhanVien nv) {
        txtMaNV.setText(nv.getMaNV());
        txtMatKhau.setText(nv.getMatKhau());
        txtTenNV.setText(nv.getTenNV());
        cboVaiTro.setSelectedIndex(nv.isVaiTro() -1);
        chkTrangThai.setSelected(nv.isTrangThai());
        
        cboGioiTinh.setSelectedItem(nv.getGioiTinh());
        txtNgaySinh.setText(nv.getNgaySinh() != null ? nv.getNgaySinh().toString() : "");        
        txtSDT.setText(nv.getSdt());
        txtDiaChi.setText(nv.getDiaChi());
    }

    private NhanVien getForm() {
        NhanVien nv = new NhanVien();
        nv.setMaNV(txtMaNV.getText().trim());
        nv.setMatKhau(txtMatKhau.getText());
        nv.setTenNV(txtTenNV.getText().trim());
        int vaiTroIndex = cboVaiTro.getSelectedIndex() + 1; 
        nv.setVaiTro(vaiTroIndex);
        nv.setMaLHLV(vaiTroIndex);
        nv.setTrangThai(chkTrangThai.isSelected());
        
        nv.setGioiTinh(cboGioiTinh.getSelectedItem().toString());
        nv.setSdt(txtSDT.getText().trim());
        nv.setDiaChi(txtDiaChi.getText().trim());
        nv.setEmail(nv.getMaNV() + "@gmail.com");
        
        try {
            String ngaySinhStr = txtNgaySinh.getText().trim();
            if (ngaySinhStr.isEmpty() || ngaySinhStr.equalsIgnoreCase("yyyy-mm-dd")) {
                } else {
                nv.setNgaySinh(java.sql.Date.valueOf(ngaySinhStr));
            }
        } catch (Exception e) {
            nv.setNgaySinh(null);
        }
        
        return nv;
    }
    
    private void clearForm() {
        txtMaNV.setText("");
        txtMatKhau.setText("");
        txtTenNV.setText("");
        txtNgaySinh.setText("yyyy-mm-dd");
        txtSDT.setText("");
        txtDiaChi.setText("");
        cboVaiTro.setSelectedIndex(1);
        cboGioiTinh.setSelectedIndex(0);
        chkTrangThai.setSelected(true);
        this.row = -1;
        updateStatus(true);
    }

    private void updateStatus(boolean insertable) {
        txtMaNV.setEditable(insertable);
        btnThem.setEnabled(insertable);
        btnSua.setEnabled(!insertable);
        btnXoa.setEnabled(!insertable);
    }

    private void insert() {
        NhanVien nv = getForm();
        if(nv.getMaNV().isEmpty() || nv.getTenNV().isEmpty()){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        try {
            dao.insert(nv);
            fillTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Thêm mới thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên đã tồn tại!");
        }
    }

    private void update() {
        NhanVien nv = getForm();
        try {
            dao.update(nv);
            fillTable();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
        }
    }

    private void delete() {
        String maNV = txtMaNV.getText();
        if (JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa nhân viên này?") == JOptionPane.YES_OPTION) {
            try {
                dao.delete(maNV);
                fillTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Xóa thành công");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Xóa thất bại");
            }
        }
    }

    private void search() {
        String keyword = txtMaNV.getText().trim();
        if(keyword.isEmpty()) keyword = txtTenNV.getText().trim();
        
        DefaultTableModel model = (DefaultTableModel) tblNhanVien.getModel();
        model.setRowCount(0);
        try {
            List<NhanVien> list = dao.selectAll(); 
            for (NhanVien nv : list) {
                if(nv.getMaNV().contains(keyword) || nv.getTenNV().contains(keyword)){
                    model.addRow(new Object[]{
                        nv.getMaNV(),
                        nv.getMatKhau(),
                        nv.getTenNV(),
                        getVaiTroString(nv.getVaiTro()),
                        nv.isTrangThai() ? "Đang làm việc" : "Đã nghỉ"
                    });
                }
            }
        } catch (Exception e) {
            fillTable();
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        inputPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtMatKhau = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTenNV = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cboVaiTro = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        cboGioiTinh = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtNgaySinh = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtDiaChi = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        chkTrangThai = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnTimKiem = new javax.swing.JButton();
        btnOpenSalary = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout(10, 10));

        inputPanel.setLayout(new java.awt.GridLayout(3, 4));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Mã Nhân Viên:");
        jLabel1.setPreferredSize(new java.awt.Dimension(47, 16));
        inputPanel.add(jLabel1);
        inputPanel.add(txtMaNV);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Mật Khẩu:");
        inputPanel.add(jLabel2);
        inputPanel.add(txtMatKhau);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Họ và Tên");
        inputPanel.add(jLabel3);
        inputPanel.add(txtTenNV);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Vai Trò:");
        inputPanel.add(jLabel4);

        cboVaiTro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Quản lý", "Nhân viên (Part time)", "Nhân viên (Full time)", "Trưởng ca (Part time)", "Trưởng ca (Full time)" }));
        inputPanel.add(cboVaiTro);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Giới Tính:");
        inputPanel.add(jLabel6);

        cboGioiTinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ", " " }));
        inputPanel.add(cboGioiTinh);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Ngày Sinh:");
        inputPanel.add(jLabel7);
        inputPanel.add(txtNgaySinh);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("SĐT:");
        inputPanel.add(jLabel8);
        inputPanel.add(txtSDT);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Địa Chỉ:");
        inputPanel.add(jLabel9);
        inputPanel.add(txtDiaChi);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Trạng Thái:");
        inputPanel.add(jLabel5);

        chkTrangThai.setText("Đang làm việc");
        inputPanel.add(chkTrangThai);

        add(inputPanel, java.awt.BorderLayout.PAGE_START);

        jPanel2.setLayout(new java.awt.GridLayout(5, 1));

        btnThem.setText("Thêm");
        jPanel2.add(btnThem);

        btnSua.setText("Sửa");
        jPanel2.add(btnSua);

        btnXoa.setText("Xóa");
        jPanel2.add(btnXoa);

        btnTimKiem.setText("Tìm Kiếm");
        jPanel2.add(btnTimKiem);

        btnOpenSalary.setText("Bảng Lương & Chấm Công");
        btnOpenSalary.addActionListener(this::btnOpenSalaryActionPerformed);
        jPanel2.add(btnOpenSalary);

        add(jPanel2, java.awt.BorderLayout.LINE_START);

        tblNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã NV", "Mật Khẩu", "Họ tên", "Ngày Sinh", "SĐT", "Địa Chỉ", "Email", "Vai Trò", "Ngày Bắt Đầu Làm", "Trạng Thái"
            }
        ));
        jScrollPane1.setViewportView(tblNhanVien);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnOpenSalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenSalaryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnOpenSalaryActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOpenSalary;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboGioiTinh;
    private javax.swing.JComboBox<String> cboVaiTro;
    private javax.swing.JCheckBox chkTrangThai;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblNhanVien;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtMatKhau;
    private javax.swing.JTextField txtNgaySinh;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTenNV;
    // End of variables declaration//GEN-END:variables
}
