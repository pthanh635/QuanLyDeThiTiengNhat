package GUI;

import DAO.CauHoiDAO;
import Model.CauHoi;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.List;

public class FormXuatDeThi extends JFrame {

    private JButton btnXuatPDF, btnQuayLai;
    private JSpinner spinnerSoCau;

    public FormXuatDeThi() {
        setTitle("Xuất đề thi");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10));

        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("Số câu trong đề:"));
        spinnerSoCau = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        panel1.add(spinnerSoCau);
        add(panel1);

        btnXuatPDF = new JButton("🖨️ Xuất đề thi PDF");
        btnXuatPDF.addActionListener(this::xuLyXuatDeThi);
        add(btnXuatPDF);

        btnQuayLai = new JButton("⬅ Quay lại");
        btnQuayLai.addActionListener(e -> {
            new TrangChu();
            dispose();
        });
        add(btnQuayLai);

        setVisible(true);
    }

    private void xuLyXuatDeThi(ActionEvent e) {
        int soCau = (int) spinnerSoCau.getValue();
        List<CauHoi> ds = CauHoiDAO.layTatCaCauHoi();

        if (ds.size() < soCau) {
            JOptionPane.showMessageDialog(this, "Ngân hàng chỉ có " + ds.size() + " câu hỏi.");
            return;
        }

        Collections.shuffle(ds);
        List<CauHoi> cauHoiDuocChon = ds.subList(0, soCau);

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn nơi lưu đề thi");
        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = chooser.getSelectedFile().getAbsolutePath();
            String fileDe = filePath + "_DeThi.pdf";
            String fileDapAn = filePath + "_DapAn.pdf";

            boolean ok = xuatPDF(cauHoiDuocChon, fileDe, fileDapAn);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Xuất đề và đáp án thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất file.");
            }
        }
    }

    private boolean xuatPDF(List<CauHoi> danhSach, String fileDe, String fileDapAn) {
        try {
            Document docDe = new Document();
            Document docDapAn = new Document();
            PdfWriter.getInstance(docDe, new FileOutputStream(fileDe));
            PdfWriter.getInstance(docDapAn, new FileOutputStream(fileDapAn));

            docDe.open();
            docDapAn.open();

            BaseFont bf = BaseFont.createFont("src/Font/NotoSansJP-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(bf, 12, Font.NORMAL);

            int stt = 1;

            for (CauHoi ch : danhSach) {
                String noiDungHienThi;
                if ("nghe".equalsIgnoreCase(ch.getLoaiCauHoi())) {
                    File f = new File(ch.getDuongDanAmThanh());
                    String fileName = f.getName();
                    noiDungHienThi = "Câu " + stt + " (nghe): Nghe file audio/" + fileName;
                    docDe.add(new Paragraph(noiDungHienThi, font));
                    docDe.add(new Paragraph("(Học sinh nghe audio trước khi chọn đáp án)", font));
                } else {
                    noiDungHienThi = "Câu " + stt + ": " + ch.getNoiDung();
                    docDe.add(new Paragraph(noiDungHienThi, font));
                }

                // Xử lý đáp án
                Map<String, String> dapAns = new LinkedHashMap<>();
                dapAns.put("A", ch.getDapAnDung());
                dapAns.put("B", ch.getDapAnSai1());
                dapAns.put("C", ch.getDapAnSai2());
                dapAns.put("D", ch.getDapAnSai3());

                List<Map.Entry<String, String>> list = new ArrayList<>(dapAns.entrySet());
                Collections.shuffle(list);

                char label = 'A';
                String dapAnDungLabel = "";

                for (Map.Entry<String, String> entry : list) {
                    String text = label + ". " + entry.getValue();
                    docDe.add(new Paragraph(text, font));

                    if (entry.getValue().equals(ch.getDapAnDung())) {
                        dapAnDungLabel = String.valueOf(label);
                    }

                    label++;
                }

                docDe.add(new Paragraph("\n"));
                docDapAn.add(new Paragraph("Câu " + stt + ":" + dapAnDungLabel, font));
                stt++;
            }

            docDe.close();
            docDapAn.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
