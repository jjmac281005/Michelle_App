package edu.uph.m23si2.pertamaapp.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class KRS extends RealmObject {
    @PrimaryKey
    private int krsID;
    private String semester;
    private String tahunAjaran;
    private Mahasiswa mahasiswa; // relasi ke Mahasiswa
    private RealmList<KRS_detail> krsDetails; // one-to-many

    public KRS(){}

    public KRS(int krsID, String semester, String tahunAjaran, Mahasiswa mahasiswa) {
        this.krsID = krsID;
        this.semester = semester;
        this.tahunAjaran = tahunAjaran;
        this.mahasiswa = mahasiswa;
    }

    public int getKrsID() {
        return krsID;
    }

    public void setKrsID(int krsID) {
        this.krsID = krsID;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getTahunAjaran() {
        return tahunAjaran;
    }

    public void setTahunAjaran(String tahunAjaran) {
        this.tahunAjaran = tahunAjaran;
    }

    public Mahasiswa getMahasiswa() {
        return mahasiswa;
    }

    public void setMahasiswa(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
    }

    public RealmList<KRS_detail> getKrsDetails() {
        return krsDetails;
    }

    public void setKrsDetails(RealmList<KRS_detail> krsDetails) {
        this.krsDetails = krsDetails;
    }
}
