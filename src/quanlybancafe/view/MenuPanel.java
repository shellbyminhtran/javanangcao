/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package quanlybancafe.view;

import java.awt.HeadlessException;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import quanlybancafe.dao.DanhMucDAO;
import quanlybancafe.dao.SanPhamDAO;
import quanlybancafe.dao.ThongKeDAO;
import quanlybancafe.helper.JdbcHelper;
import quanlybancafe.model.DanhMuc;
import quanlybancafe.model.SanPham;

/**
 *
 * @author minhs
 */
public class MenuPanel extends javax.swing.JPanel {

    private final DanhMucDAO dmDao = new DanhMucDAO();
    private final SanPhamDAO spDao = new SanPhamDAO();
    private List<DanhMuc> danhMucList;
    private int rowDanhMuc = -1;
    private quanlybancafe.model.NhanVien nvHienTai;
    
    /**
     * Creates new form MenuPanel
     */
    public MenuPanel(quanlybancafe.model.NhanVien nv) {
        initComponents();
        this.nvHienTai = nv;
        initLogic();
    }
    
    public MenuPanel() {
        initComponents();
        initLogic();
    }

    private void initLogic() {
        loadDataToTableDanhMuc();
        loadDataToCboDanhMuc();
        loadDataToTableSanPham();
        
        if (nvHienTai != null && nvHienTai.getVaiTro() != 1) {
            mainTabbedPane.remove(jPanel7);
            
            btnTongKet.setEnabled(false);
            btnTongKet.setToolTipText("Only for SM (Quản lý)");
            
            jButton1.setEnabled(false);
            jButton2.setEnabled(false);
        }
        
        if (cboLocDanhMuc != null) {
            cboLocDanhMuc.addActionListener(e -> locSanPhamTheoDanhMuc());
        }
        
        jButton1.addActionListener(e -> insertSanPham());
        jButton2.addActionListener(e -> updateSanPham());
        jButton4.addActionListener(e -> clearFormSanPham());
        jButton5.addActionListener(e -> insertDanhMuc());
        jButton6.addActionListener(e -> updateDanhMuc());
        jButton7.addActionListener(e -> deleteDanhMuc());
        jButton8.addActionListener(e -> clearFormDanhMuc());
        jButton9.addActionListener(e -> filterSanPham());
        btnTongKet.addActionListener(e -> thucHienTongKet());
        
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = jTable1.getSelectedRow();
                if (row >= 0) {
                    String maSP = jTable1.getValueAt(row, 0).toString();
                    fillFormSanPham(maSP);
                }
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rowDanhMuc = jTable2.getSelectedRow();
                if (rowDanhMuc >= 0) {
                    fillFormDanhMuc(rowDanhMuc);
                }
            }
        });
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tblHoaDon.getSelectedRow();
                if (row >= 0) {
                    String maHD = tblHoaDon.getValueAt(row, 0).toString();
                    fillTableChiTiet(maHD); // Hiện món ăn tương ứng ở bảng dưới
                }
            }
        });
        btnSearch.addActionListener(e -> {
            String maHD = txtSearch.getText().trim();
            if(maHD.isEmpty()) {
                fillTableHoaDon();
            } else {
                searchHoaDon(maHD);
            }
        });
        updateTienChoChot();
        
        mainTabbedPane.addChangeListener(e -> {
            int index = mainTabbedPane.getSelectedIndex();
            if (index == 2) {
                updateTienChoChot();
                fillTableHoaDon();
            }
            if (index == 3) {
                fillThongKe();
            }
        });
        
        fillTableHoaDon();
        updateStatusDanhMuc();
    }
      
    private void loadDataToTableDanhMuc() {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);
        try {
            danhMucList = dmDao.selectAll();
            for (DanhMuc dm : danhMucList) {
                model.addRow(new Object[]{dm.getMaDM(), dm.getTenDM()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh mục");
        }
    }
    
    private void loadDataToCboDanhMuc() {
        jComboBox1.removeAllItems();
        
        if (cboLocDanhMuc != null) {
            cboLocDanhMuc.removeAllItems();
            cboLocDanhMuc.addItem("Tất cả danh mục");
        }

        if (danhMucList != null) {
            for (DanhMuc dm : danhMucList) {
                jComboBox1.addItem(dm.getTenDM());
                
                if (cboLocDanhMuc != null) {
                    cboLocDanhMuc.addItem(dm.getTenDM());
                }
            }
        }
    }
    
    private void fillFormDanhMuc(int index) {
        DanhMuc dm = danhMucList.get(index);
        jTextField4.setText(String.valueOf(dm.getMaDM()));
        jTextField5.setText(dm.getTenDM());
        updateStatusDanhMuc();
    }
    
    private DanhMuc getFormDanhMuc() {
        DanhMuc dm = new DanhMuc();
        if (!jTextField4.getText().isEmpty()) {
            dm.setMaDM(Integer.parseInt(jTextField4.getText()));
        }
        dm.setTenDM(jTextField5.getText().trim());
        return dm;
    }
    
    private void clearFormDanhMuc() {
        jTextField4.setText("");
        jTextField5.setText("");
        rowDanhMuc = -1;
        updateStatusDanhMuc();
    }
    
    private void insertDanhMuc() {
        String tenDM = jTextField5.getText().trim();
        if (tenDM.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên danh mục");
            return;
        }
        try {
            DanhMuc dm = new DanhMuc();
            dm.setTenDM(tenDM);
            dmDao.insert(dm);
            loadDataToTableDanhMuc();
            loadDataToCboDanhMuc();
            clearFormDanhMuc();
            JOptionPane.showMessageDialog(this, "Thêm thành công");
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }
    
    private void updateStatusDanhMuc() {
        boolean isSelected = (rowDanhMuc >= 0);
        jButton5.setEnabled(!isSelected);
        jButton6.setEnabled(isSelected);
        jButton7.setEnabled(isSelected);
    }
    
    private void updateDanhMuc() {
        DanhMuc dm = getFormDanhMuc();
        try {
            dmDao.update(dm);
            loadDataToTableDanhMuc();
            loadDataToCboDanhMuc();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công");
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }
    
    private void deleteDanhMuc() {
        int maDM = Integer.parseInt(jTextField4.getText());
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn xóa danh mục này không?") == JOptionPane.YES_OPTION) {
            try {
                dmDao.delete(maDM);
                loadDataToTableDanhMuc();
                loadDataToCboDanhMuc();
                clearFormDanhMuc();
                JOptionPane.showMessageDialog(this, "Xóa thành công");
            } catch (HeadlessException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }
    
    private void loadDataToTableSanPham() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        List<SanPham> list = spDao.selectAll();
        for (SanPham sp : list) {
            model.addRow(new Object[] {
                sp.getMaSP(),
                sp.getTenSP(),
                sp.getGiaBan(),
                sp.getMaDM(),
                sp.isTrangThai() ? "Đang bán" : "Ngừng bán"
            });
        }
    }
    
    private void insertSanPham() {
        try {
            if(jTextField1.getText().isEmpty() || jTextField2.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Mã và Tên Sản Phẩm");
                return;
            }
            SanPham sp = new SanPham();
            sp.setMaSP(jTextField1.getText());
            sp.setTenSP(jTextField2.getText());
            sp.setGiaBan(Double.parseDouble(jTextField3.getText()));
            
            int index = jComboBox1.getSelectedIndex();
            if (index >= 0 && danhMucList != null) {
                sp.setMaDM(danhMucList.get(index).getMaDM());
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục");
                return;
            }
            sp. setTrangThai(jCheckBox1.isSelected());
            
            spDao.insert(sp);
            locSanPhamTheoDanhMuc();
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công");
            clearFormSanPham();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá bán phải là số");
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void clearFormSanPham() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jCheckBox1.setSelected(true);
        jTextField1.setEditable(true);
    }
    
    private void fillFormSanPham(String maSP) {
        List<SanPham> list = spDao.selectAll();
        SanPham sp = null;
        
        for (SanPham item : list) {
            if (item.getMaSP().equals(maSP)) {
                sp = item;
                break;
            }
        }
        
        if (sp == null) return;
        
        jTextField1.setText(sp.getMaSP());
        jTextField1.setEditable(false);
        jTextField2.setText(sp.getTenSP());
        jTextField3.setText(String.valueOf(sp.getGiaBan()));
        
        if (danhMucList != null) {
            for (int i = 0; i < danhMucList.size(); i++) {
                if (danhMucList.get(i).getMaDM() == sp.getMaDM()) {
                    jComboBox1.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        jCheckBox1.setSelected(sp.isTrangThai());
    }
    
    private void updateSanPham() {
        try {
            SanPham sp = new SanPham();
            sp.setMaSP(jTextField1.getText());
            sp.setTenSP(jTextField2.getText());
            sp.setGiaBan(Double.parseDouble(jTextField3.getText()));
            int index = jComboBox1.getSelectedIndex();
            sp.setMaDM(danhMucList.get(index).getMaDM());
            sp. setTrangThai(jCheckBox1.isSelected());
            
            spDao.update(sp);
            locSanPhamTheoDanhMuc();
            JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công");
        } catch (HeadlessException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật");
        } 
    }    
    
    private void filterSanPham() {
        String maSearch = jTextField1.getText().trim();
        String tenSearch = jTextField2.getText().trim();
        
        List<SanPham> list = spDao.selectByFilter(maSearch, tenSearch);
        fillTableSanPham(list);
        
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm nào khớp với thông tin");
        }
    }
    
    private void fillTableSanPham(List<SanPham> list) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        for (SanPham sp : list) {
            model.addRow(new Object[] {
                sp.getMaSP(),
                sp.getTenSP(),
                sp.getGiaBan(),
                sp.getMaDM(),
                sp.isTrangThai() ? "Đang bán" : "Ngừng bán"
            });
        }
    }
    
    private void locSanPhamTheoDanhMuc() {
        if (cboLocDanhMuc == null || danhMucList == null) return;
        
        int index = cboLocDanhMuc.getSelectedIndex();
        if (index <= 0) {
            loadDataToTableSanPham();
        } else {
            int maDMLoc = danhMucList.get(index -1).getMaDM();
            
            List<SanPham> allSp = spDao.selectAll();
            List<SanPham> filteredList = new java.util.ArrayList<>();
            
            for (SanPham sp : allSp) {
                if (sp.getMaDM() == maDMLoc) {
                    filteredList.add(sp);
                }
            }
            fillTableSanPham(filteredList);
        }
    }
    
    private void fillTableHoaDon() {
        DefaultTableModel model = (DefaultTableModel) tblHoaDon.getModel();
        model.setRowCount(0);
        // JOIN thêm bảng KHACH_HANG để lấy TenKH (nếu cần)
        String sql = "SELECT h.MaHD, h.MaNV, h.NgayLap, h.TongTien, ISNULL(k.TenKH, N'Khách vãng lai') as TenKH " +
                     "FROM HOA_DON h LEFT JOIN KHACH_HANG k ON h.MaKH = k.MaKH ORDER BY h.NgayLap DESC";
        try (java.sql.Connection con = JdbcHelper.getConnection();
             java.sql.ResultSet rs = JdbcHelper.executeQuery(con, sql)) {
            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getString("MaHD"),
                    rs.getString("MaNV"),
                    rs.getTimestamp("NgayLap"),
                    rs.getBigDecimal("TongTien")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void filterHoaDon() {
        String maHD = txtSearch.getText().trim();
        String tuNgay = txtTuNgay.getText().trim();
        String denNgay = txtDenNgay.getText().trim();

        DefaultTableModel model = (DefaultTableModel) tblHoaDon.getModel();
        model.setRowCount(0);

        // Xây dựng câu lệnh SQL động
        StringBuilder sql = new StringBuilder("SELECT MaHD, MaNV, NgayLap, TongTien FROM HOA_DON WHERE 1=1 ");

        if (!maHD.isEmpty()) sql.append(" AND MaHD LIKE '%").append(maHD).append("%'");
        if (!tuNgay.isEmpty()) sql.append(" AND NgayLap >= '").append(tuNgay).append("'");
        if (!denNgay.isEmpty()) sql.append(" AND NgayLap <= '").append(denNgay).append(" 23:59:59'");

        sql.append(" ORDER BY NgayLap DESC");

        try (java.sql.Connection con = JdbcHelper.getConnection();
             java.sql.ResultSet rs = JdbcHelper.executeQuery(con, sql.toString())) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString(1), rs.getString(2), rs.getTimestamp(3), rs.getBigDecimal(4)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void fillTableChiTiet(String maHD) {
        DefaultTableModel model = (DefaultTableModel) tblChiTiet.getModel();
        model.setRowCount(0);
        String sql = "SELECT SP.TenSP, CT.SoLuong, CT.DonGia, CT.ThanhTien " +
                     "FROM Chi_Tiet_Hoa_Don CT " +
                     "JOIN SAN_PHAM SP ON CT.MaSP = SP.MaSP " +
                     "WHERE CT.MaHD = ?";
        try (java.sql.Connection con = JdbcHelper.getConnection();
             java.sql.ResultSet rs = JdbcHelper.executeQuery(con, sql, maHD)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString(1),
                    rs.getInt(2),
                    rs.getBigDecimal(3),
                    rs.getBigDecimal(4)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tải chi tiết hóa đơn!");
        }
    }
    
    private void searchHoaDon(String maHD) {
        DefaultTableModel model = (DefaultTableModel) tblHoaDon.getModel();
        model.setRowCount(0);
        String sql = "SELECT MaHD, MaNV, NgayLap, TongTien FROM HOA_DON WHERE MaHD LIKE ?";
        try (java.sql.Connection con = JdbcHelper.getConnection();
             java.sql.ResultSet rs = JdbcHelper.executeQuery(con, sql, "%" + maHD + "%")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString(1), rs.getString(2), rs.getTimestamp(3), rs.getBigDecimal(4)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void fillThongKe() {
        try (java.sql.Connection con = JdbcHelper.getConnection()) {
            try (java.sql.ResultSet rsDT = JdbcHelper.executeQuery(con, ThongKeDAO.SQL_DOANH_THU)) {
                if (rsDT.next()) {
                    double dt = rsDT.getDouble(1);
                    lblDoanhThuNgay.setText(String.format("<html><center>DOANH THU HÔM NAY<br><b style='color:grey; font-size:16px;'>%,.0f VNĐ</b></center></html>", dt));
                }
            }

            try (java.sql.ResultSet rsHD = JdbcHelper.executeQuery(con, ThongKeDAO.SQL_TONG_HD)) {
                if (rsHD.next()) {
                    int soDon = rsHD.getInt(1);
                    lblTongHD.setText(String.format("<html><center>TỔNG ĐƠN HÀNG<br><b style='color:grey; font-size:16px;'>%d Đơn</b></center></html>", soDon));
                }
            }

            try (java.sql.ResultSet rsBest = JdbcHelper.executeQuery(con, ThongKeDAO.SQL_BEST_SELLER)) {
                String spBest = rsBest.next() ? rsBest.getString(1) : "(Chưa có)";
                lblBestSeller.setText(String.format("<html><center>MÓN BÁN CHẠY NHẤT<br><b style='color:grey; font-size:15px;'>%s</b></center></html>", spBest));
            }

            DefaultTableModel model = (DefaultTableModel) tblThongKe.getModel();
            model.setRowCount(0);
            try (java.sql.ResultSet rsTable = JdbcHelper.executeQuery(con, ThongKeDAO.SQL_CHI_TIET_THANG)) {
                while (rsTable.next()) {
                    model.addRow(new Object[]{
                        rsTable.getDate("Ngay"), 
                        rsTable.getInt("SoDon"), 
                        String.format("%,.0f", rsTable.getDouble("DoanhThu"))
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật dữ liệu thống kê!");
        }
    }
    
    private void updateTienChoChot() {
        try (java.sql.Connection con = JdbcHelper.getConnection();
         ) {
            System.out.println("SQL_TIEN_CHO_CHOT be running... " + ThongKeDAO.SQL_TIEN_CHO_CHOT);
            java.sql.ResultSet rs = JdbcHelper.executeQuery(con, ThongKeDAO.SQL_TIEN_CHO_CHOT);
            if (rs.next()) {
                double total = rs.getDouble(1);
                lblTienChoChot.setText(String.format("Tiền chờ chốt: %,.0f VND", total));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void thucHienTongKet() {
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Xác nhận chốt sổ toàn bộ hóa đơn và chuyển vào báo cáo doanh thu?", 
                "Tổng kết ca làm việc", 
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "UPDATE HOA_DON SET DaTongKet = 1, TrangThaiThanhToan = N'Đã tổng kết' WHERE DaTongKet = 0";

            try {
                String tenSM = (nvHienTai != null) ? nvHienTai.getTenNV() : "Quản lý hệ thống";
                String tienChot = lblTienChoChot.getText();
                JdbcHelper.executeUpdate(sql);

                String report = "==========================================\n" +
                                "          BÁO CÁO TỔNG KẾT DOANH THU      \n" +
                                "==========================================\n" +
                                "Thời gian chốt : " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date()) + "\n" +
                                "Người thực hiện: " + tenSM + "\n" +
                                "Chi tiết       : " + tienChot + "\n" +
                                "Trạng thái     : Đã xác nhận & Lưu hệ thống\n" +
                                "==========================================\n";
                exportReportToFile(report);

                updateTienChoChot();
                fillTableHoaDon();

                if (mainTabbedPane.getTabCount() > 3) {
                    mainTabbedPane.setSelectedIndex(3); 
                    fillThongKe();
                }    

                JOptionPane.showMessageDialog(this, "Đã hoàn tất tổng kết và lưu vào lịch sử hệ thống!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi thực hiện: " + e.getMessage());
            }
        }
    }
    
    private void exportReportToFile(String content) {
        try {
            java.io.File folder = new java.io.File("report");
            if (!folder.exists()) {
                folder.mkdir();
            }
            
            String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String fileName = "report/BaoCao_" + timeStamp + ".txt";
            
            try (java.io.FileWriter write = new java.io.FileWriter(fileName)) {
                write.write(content);
                System.out.println("Đã xuất báo cáo tại: " + fileName);
            }
            
            java.awt.Desktop.getDesktop().open(new java.io.File(fileName));
        } catch (Exception e) {
            System.err.printf("Lỗi xuất file bá cáo: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Không thể xuất file báo cáo");
        }
    }
    
    private void exportToExcel(JTable table) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn nơi lưu file báo cáo");
        // Mặc định tên file có kèm ngày giờ
        String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmm").format(new java.util.Date());
        chooser.setSelectedFile(new java.io.File("BaoCaoThongKe_" + timeStamp + ".csv"));

        int userSelection = chooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = chooser.getSelectedFile();

            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(
                    new java.io.OutputStreamWriter(new java.io.FileOutputStream(fileToSave), "UTF-8"))) {

                bw.write('\ufeff');

                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int columnCount = model.getColumnCount();

                for (int i = 0; i < columnCount; i++) {
                    bw.write(model.getColumnName(i));
                    if (i < columnCount - 1) bw.write(";");
                }
                bw.newLine();

                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < columnCount; j++) {
                        Object value = model.getValueAt(i, j);
                        String valStr = (value != null) ? value.toString().replace(",", "") : "";
                        bw.write(valStr);
                        if (j < columnCount - 1) bw.write(";");
                    }
                    bw.newLine();
                }

                JOptionPane.showMessageDialog(this, "Xuất file thành công!");
                //Auto mo file
                java.awt.Desktop.getDesktop().open(fileToSave);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất file: " + e.getMessage());
                e.printStackTrace();
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

        mainTabbedPane = new javax.swing.JTabbedPane();
        productPane = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        cboLocDanhMuc = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        categoryPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        txtTuNgay = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtDenNgay = new javax.swing.JTextField();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblChiTiet = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        btnTongKet = new javax.swing.JButton();
        lblTienChoChot = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        pnlDashboard = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        lblDoanhThuNgay = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        lblTongHD = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        lblBestSeller = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblThongKe = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jButton10 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        productPane.setName("productPane"); // NOI18N
        productPane.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridLayout(3, 4, 1, 1));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Mã SP:");
        jPanel1.add(jLabel1);

        jTextField1.setName("txtMaSP"); // NOI18N
        jPanel1.add(jTextField1);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Tên SP:");
        jPanel1.add(jLabel2);

        jTextField2.setName("txtTenSP"); // NOI18N
        jPanel1.add(jTextField2);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Giá Bán:");
        jPanel1.add(jLabel3);

        jTextField3.setName("txtGiaBan"); // NOI18N
        jPanel1.add(jTextField3);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Danh Mục:");
        jPanel1.add(jLabel4);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setName("cboDanhMuc"); // NOI18N
        jPanel1.add(jComboBox1);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Trạng Thái:");
        jPanel1.add(jLabel5);

        jCheckBox1.setText("Đang bán");
        jCheckBox1.setName("chkTrangThai"); // NOI18N
        jPanel1.add(jCheckBox1);

        productPane.add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setLayout(new java.awt.GridLayout(5, 1));

        jButton1.setText("Thêm");
        jButton1.setName("btnThemSp"); // NOI18N
        jPanel2.add(jButton1);

        jButton2.setText("Sửa");
        jButton2.setName("btnSuaSP"); // NOI18N
        jPanel2.add(jButton2);

        jButton4.setText("Mới");
        jButton4.setName("btnMoiSP"); // NOI18N
        jPanel2.add(jButton4);

        jButton9.setText("Tìm Kiếm");
        jPanel2.add(jButton9);

        cboLocDanhMuc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel2.add(cboLocDanhMuc);

        productPane.add(jPanel2, java.awt.BorderLayout.LINE_START);

        jScrollPane1.setName("tblSanPham"); // NOI18N

        jTable1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTable1.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã SP", "Tên SP", "Giá Bán", "Danh Mục", "Trạng Thái"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        productPane.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        mainTabbedPane.addTab("Quản Lý Sản Phẩm", productPane);

        categoryPanel.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.GridLayout(1, 4));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Mã DM:");
        jPanel3.add(jLabel6);

        jTextField4.setEditable(false);
        jTextField4.setName("txtMaDM"); // NOI18N
        jPanel3.add(jTextField4);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Tên DM:");
        jPanel3.add(jLabel7);

        jTextField5.setName("txtTenDM"); // NOI18N
        jPanel3.add(jTextField5);

        categoryPanel.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel4.setLayout(new java.awt.GridLayout(4, 1));

        jButton5.setText("Thêm");
        jButton5.setName("btnThemDM"); // NOI18N
        jPanel4.add(jButton5);

        jButton6.setText("Sửa");
        jButton6.setName("btnSuaDM"); // NOI18N
        jPanel4.add(jButton6);

        jButton7.setText("Xóa");
        jButton7.setName("btnXoaDM"); // NOI18N
        jPanel4.add(jButton7);

        jButton8.setText("Mới");
        jButton8.setName("btnMoiDM"); // NOI18N
        jPanel4.add(jButton8);

        categoryPanel.add(jPanel4, java.awt.BorderLayout.LINE_START);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Mã Danh Mục", "Tên Danh Mục"
            }
        ));
        jTable2.setName("tblDanhMuc"); // NOI18N
        jScrollPane2.setViewportView(jTable2);

        categoryPanel.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        mainTabbedPane.addTab("Quản Lý Danh Mục", categoryPanel);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel6.setPreferredSize(new java.awt.Dimension(100, 50));
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel8.setText("Tìm mã Hóa Đơn:");
        jPanel6.add(jLabel8);

        txtSearch.setColumns(15);
        jPanel6.add(txtSearch);

        btnSearch.setText("Tìm");
        jPanel6.add(btnSearch);

        jLabel13.setText("Từ:");
        jPanel6.add(jLabel13);

        txtTuNgay.setColumns(10);
        txtTuNgay.setToolTipText("");
        jPanel6.add(txtTuNgay);

        jLabel12.setText("Đến:");
        jPanel6.add(jLabel12);

        txtDenNgay.setColumns(10);
        jPanel6.add(txtDenNgay);

        jPanel5.add(jPanel6, java.awt.BorderLayout.PAGE_START);

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã HD", "Mã NV", "Ngày lập", "Tổng tiền"
            }
        ));
        jScrollPane3.setViewportView(tblHoaDon);

        jSplitPane1.setTopComponent(jScrollPane3);

        tblChiTiet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"
            }
        ));
        jScrollPane4.setViewportView(tblChiTiet);

        jSplitPane1.setRightComponent(jScrollPane4);

        jPanel5.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        btnTongKet.setText("XÁC NHẬN TỔNG KẾT");
        jPanel13.add(btnTongKet);

        lblTienChoChot.setText("Tiền chờ chốt: 0 VND");
        jPanel13.add(lblTienChoChot);

        jPanel5.add(jPanel13, java.awt.BorderLayout.PAGE_END);

        mainTabbedPane.addTab("Hóa Đơn", jPanel5);

        jPanel7.setLayout(new java.awt.BorderLayout());

        pnlDashboard.setLayout(new java.awt.GridLayout(1, 3, 20, 20));

        jPanel10.setBackground(new java.awt.Color(204, 255, 204));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lblDoanhThuNgay.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblDoanhThuNgay.setForeground(new java.awt.Color(0, 153, 51));
        lblDoanhThuNgay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDoanhThuNgay.setText("Doanh thu hôm nay: ");
        jPanel10.add(lblDoanhThuNgay);

        pnlDashboard.add(jPanel10);

        jPanel11.setBackground(new java.awt.Color(204, 229, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lblTongHD.setBackground(new java.awt.Color(204, 229, 255));
        lblTongHD.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblTongHD.setForeground(new java.awt.Color(0, 153, 51));
        lblTongHD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTongHD.setText("Tổng hóa đơn: ");
        jPanel11.add(lblTongHD);

        pnlDashboard.add(jPanel11);

        jPanel8.setBackground(new java.awt.Color(255, 255, 204));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lblBestSeller.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblBestSeller.setForeground(new java.awt.Color(0, 153, 51));
        lblBestSeller.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBestSeller.setText("Món bán chạy nhất: ");
        jPanel8.add(lblBestSeller);

        pnlDashboard.add(jPanel8);

        jPanel7.add(pnlDashboard, java.awt.BorderLayout.PAGE_START);

        tblThongKe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ngày", "Số đơn", "Doanh thu"
            }
        ));
        tblThongKe.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblThongKe.setRowHeight(25);
        jScrollPane5.setViewportView(tblThongKe);

        jPanel7.add(jScrollPane5, java.awt.BorderLayout.CENTER);

        jButton10.setBackground(new java.awt.Color(51, 204, 0));
        jButton10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton10.setText("Xuất Báo Cáo EXCEL");
        jButton10.addActionListener(this::jButton10ActionPerformed);
        jPanel9.add(jButton10);

        jPanel7.add(jPanel9, java.awt.BorderLayout.PAGE_END);

        mainTabbedPane.addTab("Thống Kê", jPanel7);

        add(mainTabbedPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        if (tblThongKe.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để xuất");
            return;
        }
        exportToExcel(tblThongKe);
    }//GEN-LAST:event_jButton10ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnTongKet;
    private javax.swing.JPanel categoryPanel;
    private javax.swing.JComboBox<String> cboLocDanhMuc;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JLabel lblBestSeller;
    private javax.swing.JLabel lblDoanhThuNgay;
    private javax.swing.JLabel lblTienChoChot;
    private javax.swing.JLabel lblTongHD;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JPanel pnlDashboard;
    private javax.swing.JPanel productPane;
    private javax.swing.JTable tblChiTiet;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTable tblThongKe;
    private javax.swing.JTextField txtDenNgay;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTuNgay;
    // End of variables declaration//GEN-END:variables

}
