package GUI;

import javax.swing.*;
import java.awt.*;

public class TrangChu extends JFrame {

    private JButton btnNhapCauHoi, btnXemDanhSach, btnXuatDeThi, btnThoat;

    public TrangChu() {
        setTitle("Quản lý ngân hàng đề thi tiếng Nhật");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1, 10, 10));

        // Tiêu đề
        JLabel lblTitle = new JLabel("📘 Quản lý ngân hàng đề thi tiếng Nhật", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitle);

        // Các nút chức năng
        btnNhapCauHoi = new JButton("➕ Nhập câu hỏi mới");
        btnXemDanhSach = new JButton("📋 Xem danh sách câu hỏi");
        btnXuatDeThi = new JButton("🖨️ Xuất đề thi");
        btnThoat = new JButton("❌ Thoát");

        // Sự kiện nút
        btnNhapCauHoi.addActionListener(e -> {
            new FormNhapCauHoi();
            dispose();
        });

        btnXemDanhSach.addActionListener(e -> {
            new FormDanhSachCauHoi(); // Cần tạo lớp này
            dispose();
        });

        btnXuatDeThi.addActionListener(e -> {
            new FormXuatDeThi(); // Cần tạo lớp này
            dispose();
        });

        btnThoat.addActionListener(e -> System.exit(0));

        add(btnNhapCauHoi);
        add(btnXemDanhSach);
        add(btnXuatDeThi);
        add(btnThoat);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TrangChu::new);
    }
}
