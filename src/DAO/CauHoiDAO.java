package DAO;

import Model.CauHoi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CauHoiDAO {

    public static boolean themCauHoi(CauHoi ch) {
        String sql = "INSERT INTO cau_hoi (noi_dung, dap_an_dung, dap_an_sai1, dap_an_sai2, dap_an_sai3, loai_cau_hoi, duong_dan_am_thanh) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ch.getNoiDung());
            ps.setString(2, ch.getDapAnDung());
            ps.setString(3, ch.getDapAnSai1());
            ps.setString(4, ch.getDapAnSai2());
            ps.setString(5, ch.getDapAnSai3());
            ps.setString(6, ch.getLoaiCauHoi());
            ps.setString(7, ch.getDuongDanAmThanh());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy danh sách tất cả câu hỏi
    public static List<CauHoi> layTatCaCauHoi() {
        List<CauHoi> list = new ArrayList<>();
        String sql = "SELECT * FROM cau_hoi";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CauHoi ch = new CauHoi(
                    rs.getInt("id"),
                    rs.getString("noi_dung"),
                    rs.getString("dap_an_dung"),
                    rs.getString("dap_an_sai1"),
                    rs.getString("dap_an_sai2"),
                    rs.getString("dap_an_sai3"),
                    rs.getString("loai_cau_hoi"),
                    rs.getString("duong_dan_am_thanh")
                );
                list.add(ch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Xóa câu hỏi theo ID
    public static boolean xoaCauHoi(int id) {
        String sql = "DELETE FROM cau_hoi WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean capNhatCauHoi(CauHoi ch) {
        String sql = "UPDATE cau_hoi SET noi_dung=?, dap_an_dung=?, dap_an_sai1=?, dap_an_sai2=?, dap_an_sai3=?, loai_cau_hoi=?, duong_dan_am_thanh=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ch.getNoiDung());
            ps.setString(2, ch.getDapAnDung());
            ps.setString(3, ch.getDapAnSai1());
            ps.setString(4, ch.getDapAnSai2());
            ps.setString(5, ch.getDapAnSai3());
            ps.setString(6, ch.getLoaiCauHoi());
            ps.setString(7, ch.getDuongDanAmThanh());
            ps.setInt(8, ch.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
