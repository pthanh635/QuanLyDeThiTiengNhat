package Model;

public class CauHoi {
    private int id;
    private String noiDung;
    private String dapAnDung;
    private String dapAnSai1;
    private String dapAnSai2;
    private String dapAnSai3;
    private String loaiCauHoi;
    private String duongDanAmThanh;

    public CauHoi(String noiDung, String dapAnDung, String dapAnSai1, String dapAnSai2, String dapAnSai3, String loaiCauHoi, String duongDanAmThanh) {
        this.noiDung = noiDung;
        this.dapAnDung = dapAnDung;
        this.dapAnSai1 = dapAnSai1;
        this.dapAnSai2 = dapAnSai2;
        this.dapAnSai3 = dapAnSai3;
        this.loaiCauHoi = loaiCauHoi;
        this.duongDanAmThanh = duongDanAmThanh;
    }

    // Constructor CÓ id (khi sửa hoặc đọc từ CSDL)
    public CauHoi(int id, String noiDung, String dapAnDung, String dapAnSai1, String dapAnSai2, String dapAnSai3, String loaiCauHoi, String duongDanAmThanh) {
        this.id = id;
        this.noiDung = noiDung;
        this.dapAnDung = dapAnDung;
        this.dapAnSai1 = dapAnSai1;
        this.dapAnSai2 = dapAnSai2;
        this.dapAnSai3 = dapAnSai3;
        this.loaiCauHoi = loaiCauHoi;
        this.duongDanAmThanh = duongDanAmThanh;
    }

    // Getter & Setter
    public int getId() {
        return id;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public String getDapAnDung() {
        return dapAnDung;
    }

    public String getDapAnSai1() {
        return dapAnSai1;
    }

    public String getDapAnSai2() {
        return dapAnSai2;
    }

    public String getDapAnSai3() {
        return dapAnSai3;
    }

    public String getLoaiCauHoi() {
        return loaiCauHoi;
    }

    public String getDuongDanAmThanh() {
        return duongDanAmThanh;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public void setDapAnDung(String dapAnDung) {
        this.dapAnDung = dapAnDung;
    }

    public void setDapAnSai1(String dapAnSai1) {
        this.dapAnSai1 = dapAnSai1;
    }

    public void setDapAnSai2(String dapAnSai2) {
        this.dapAnSai2 = dapAnSai2;
    }

    public void setDapAnSai3(String dapAnSai3) {
        this.dapAnSai3 = dapAnSai3;
    }

    public void setLoaiCauHoi(String loaiCauHoi) {
        this.loaiCauHoi = loaiCauHoi;
    }

    public void setDuongDanAmThanh(String duongDanAmThanh) {
        this.duongDanAmThanh = duongDanAmThanh;
    }
}
