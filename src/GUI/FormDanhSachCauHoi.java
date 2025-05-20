package GUI;

import DAO.CauHoiDAO;
import Model.CauHoi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormDanhSachCauHoi extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public FormDanhSachCauHoi() {
        setTitle("Danh sách câu hỏi");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{
            "ID", "Nội dung", "Đáp án đúng", "Sai 1", "Sai 2", "Sai 3", "Loại", "Âm thanh"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // ID không sửa
            }
        };

        table = new JTable(model);
        table.setRowHeight(40);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        loadData();

        JButton btnLuuThayDoi = new JButton("💾 Lưu thay đổi");
        JButton btnXoa = new JButton("🗑 Xoá");
        JButton btnQuayLai = new JButton("⬅ Quay lại");

        JPanel panelButtons = new JPanel();
        panelButtons.add(btnLuuThayDoi);
        panelButtons.add(btnXoa);
        panelButtons.add(btnQuayLai);
        add(panelButtons, BorderLayout.SOUTH);

        btnLuuThayDoi.addActionListener(e -> luuThayDoi());
        btnXoa.addActionListener(e -> xoaCauHoi());
        btnQuayLai.addActionListener(e -> {
            new TrangChu();
            dispose();
        });

        setVisible(true);
    }

    private void loadData() {
        model.setRowCount(0); // clear
        List<CauHoi> ds = CauHoiDAO.layTatCaCauHoi();
        for (CauHoi ch : ds) {
            model.addRow(new Object[]{
                ch.getId(), ch.getNoiDung(), ch.getDapAnDung(),
                ch.getDapAnSai1(), ch.getDapAnSai2(), ch.getDapAnSai3(),
                ch.getLoaiCauHoi(), ch.getDuongDanAmThanh()
            });
        }
    }

    private void luuThayDoi() {
        int rowCount = model.getRowCount();
        int dem = 0;
        for (int i = 0; i < rowCount; i++) {
            try {
                int id = Integer.parseInt(model.getValueAt(i, 0).toString());
                String nd = model.getValueAt(i, 1).toString();
                String dung = model.getValueAt(i, 2).toString();
                String sai1 = model.getValueAt(i, 3).toString();
                String sai2 = model.getValueAt(i, 4).toString();
                String sai3 = model.getValueAt(i, 5).toString();
                String loai = model.getValueAt(i, 6).toString();
                String amthanh = model.getValueAt(i, 7).toString();

                CauHoi ch = new CauHoi(id, nd, dung, sai1, sai2, sai3, loai, amthanh);
                if (CauHoiDAO.capNhatCauHoi(ch)) dem++;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(this, "Đã lưu " + dem + " câu hỏi.");
    }

    private void xoaCauHoi() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng để xoá.");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn xoá câu hỏi này?", "Xác nhận xoá",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (CauHoiDAO.xoaCauHoi(id)) {
                model.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Đã xoá câu hỏi.");
            } else {
                JOptionPane.showMessageDialog(this, "Xoá thất bại.");
            }
        }
    }
}
