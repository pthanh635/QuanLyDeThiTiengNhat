package GUI;

import DAO.CauHoiDAO;
import Model.CauHoi;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FormNhapCauHoi extends JFrame {
    private JTextArea txtNoiDung;
    private JTextField txtDapAnDung, txtDapAnSai1, txtDapAnSai2, txtDapAnSai3, txtAmThanh;
    private JComboBox<String> comboLoai, comboLoaiAI;
    private JButton btnChonFile, btnNghe, btnLuu, btnQuayLai, btnGoiYAI;

    public FormNhapCauHoi() {
        setTitle("Nhập Câu Hỏi");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        addLabelAndComponent("Nội dung:", txtNoiDung = new JTextArea(4, 20), gbc, row++);
        addLabelAndComponent("Đáp án đúng:", txtDapAnDung = new JTextField(), gbc, row++);
        addLabelAndComponent("Đáp án sai 1:", txtDapAnSai1 = new JTextField(), gbc, row++);
        addLabelAndComponent("Đáp án sai 2:", txtDapAnSai2 = new JTextField(), gbc, row++);
        addLabelAndComponent("Đáp án sai 3:", txtDapAnSai3 = new JTextField(), gbc, row++);
        addLabelAndComponent("Loại câu hỏi:", comboLoai = new JComboBox<>(new String[]{"text", "nghe"}), gbc, row++);
        addLabelAndComponent("File âm thanh:", txtAmThanh = new JTextField(), gbc, row++);
        txtAmThanh.setEditable(false);

        btnChonFile = new JButton("Chọn file");
        btnNghe = new JButton("🔊 Nghe thử");

        gbc.gridx = 1; gbc.gridy = row;
        add(btnChonFile, gbc);
        gbc.gridx = 2;
        add(btnNghe, gbc);
        row++;

        addLabelAndComponent("Gợi ý AI theo loại:", comboLoaiAI = new JComboBox<>(new String[]{"text", "nghe"}), gbc, row++);
        btnGoiYAI = new JButton("🤖 Gợi ý câu hỏi bằng AI");
        gbc.gridx = 1; gbc.gridy = row++;
        add(btnGoiYAI, gbc);

        btnLuu = new JButton("Lưu câu hỏi");
        gbc.gridx = 1; gbc.gridy = row++;
        add(btnLuu, gbc);

        btnQuayLai = new JButton("⬅ Quay lại trang chủ");
        gbc.gridx = 1; gbc.gridy = row++;
        add(btnQuayLai, gbc);

        btnChonFile.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                txtAmThanh.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        btnNghe.addActionListener(e -> {
            String path = txtAmThanh.getText().trim();
            if (path.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Chưa có file âm thanh để phát.");
            } else {
                AI.AudioPlayerUtil.playAudio(path);
            }
        });

        btnLuu.addActionListener(e -> luuCauHoi());
        btnGoiYAI.addActionListener(e -> goiYCauHoiAI());
        btnQuayLai.addActionListener(e -> {
            new TrangChu();
            dispose();
        });

        setVisible(true);
    }

    private void addLabelAndComponent(String label, JComponent comp, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        if (comp instanceof JTextArea) {
            add(new JScrollPane(comp), gbc);
        } else {
            add(comp, gbc);
        }
        gbc.gridwidth = 1;
    }

    private void luuCauHoi() {
        String noiDung = txtNoiDung.getText().trim();
        String dung = txtDapAnDung.getText().trim();
        String sai1 = txtDapAnSai1.getText().trim();
        String sai2 = txtDapAnSai2.getText().trim();
        String sai3 = txtDapAnSai3.getText().trim();
        String loai = comboLoai.getSelectedItem().toString();
        String fileAmThanh = txtAmThanh.getText().trim();

        if (noiDung.isEmpty() || dung.isEmpty() || sai1.isEmpty() || sai2.isEmpty() || sai3.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ các trường.");
            return;
        }

        if (loai.equals("nghe") && fileAmThanh.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn file âm thanh cho câu hỏi nghe.");
            return;
        }

        CauHoi ch = new CauHoi(noiDung, dung, sai1, sai2, sai3, loai, fileAmThanh);
        if (CauHoiDAO.themCauHoi(ch)) {
            JOptionPane.showMessageDialog(this, "Đã lưu câu hỏi thành công!");
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Lưu thất bại.");
        }
    }

    private void goiYCauHoiAI() {
        String chuDe = JOptionPane.showInputDialog(this, "Nhập chủ đề câu hỏi:");
        if (chuDe == null || chuDe.trim().isEmpty()) return;

        String loaiAI = comboLoaiAI.getSelectedItem().toString();
        btnGoiYAI.setEnabled(false);
        btnGoiYAI.setText("Đang gợi ý...");

        new Thread(() -> {
            try {
                String ketQua = AI.OpenAIUtil.goiYDeThiTuChuDe(chuDe, loaiAI);
                int start = ketQua.indexOf("{");
                int end = ketQua.lastIndexOf("}") + 1;
                if (start == -1 || end == -1) throw new IOException("Không tìm thấy JSON trong phản hồi.");
                String jsonClean = ketQua.substring(start, end);

                JSONObject obj = new JSONObject(jsonClean);
                String cauHoi = obj.getString("cauHoi");
                String dung = obj.getString("dung");
                String sai1 = obj.getString("sai1");
                String sai2 = obj.getString("sai2");
                String sai3 = obj.getString("sai3");

                String audioPath = "";
                if (loaiAI.equals("nghe")) {
                    String filePath = "audio/" + System.currentTimeMillis() + ".mp3";
                    new File("audio").mkdirs();
                    audioPath = AI.TextToSpeechUtil.taoFileAmThanhTuVanBan(cauHoi, filePath);
                }

                String finalAudioPath = audioPath;
                SwingUtilities.invokeLater(() -> {
                    txtNoiDung.setText(cauHoi);
                    txtDapAnDung.setText(dung);
                    txtDapAnSai1.setText(sai1);
                    txtDapAnSai2.setText(sai2);
                    txtDapAnSai3.setText(sai3);
                    comboLoai.setSelectedItem(loaiAI);
                    txtAmThanh.setText(finalAudioPath);

                    btnGoiYAI.setEnabled(true);
                    btnGoiYAI.setText("🤖 Gợi ý câu hỏi bằng AI");
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Lỗi khi gọi AI: " + ex.getMessage());
                    btnGoiYAI.setEnabled(true);
                    btnGoiYAI.setText("🤖 Gợi ý câu hỏi bằng AI");
                });
            }
        }).start();
    }

    private void clearForm() {
        txtNoiDung.setText("");
        txtDapAnDung.setText("");
        txtDapAnSai1.setText("");
        txtDapAnSai2.setText("");
        txtDapAnSai3.setText("");
        txtAmThanh.setText("");
        comboLoai.setSelectedIndex(0);
    }
}