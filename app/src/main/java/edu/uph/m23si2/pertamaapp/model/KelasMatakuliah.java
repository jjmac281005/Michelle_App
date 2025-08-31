package edu.uph.m23si2.pertamaapp.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class KelasMatakuliah extends RealmObject {
    @PrimaryKey
    private int kelasID;
    private String namaKelas;
    private String dosen;
    private Matakuliah matakuliah; // relasi ke Matakuliah
    private RealmList<KRS_detail> krsDetails; // one-to-many ke KRS_detail

    public KelasMatakuliah(){}

    public KelasMatakuliah(int kelasID, String namaKelas, String dosen, Matakuliah matakuliah) {
        this.kelasID = kelasID;
        this.namaKelas = namaKelas;
        this.dosen = dosen;
        this.matakuliah = matakuliah;
    }

    public int getKelasID() {
        return kelasID;
    }

    public void setKelasID(int kelasID) {
        this.kelasID = kelasID;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public String getDosen() {
        return dosen;
    }

    public void setDosen(String dosen) {
        this.dosen = dosen;
    }

    public Matakuliah getMatakuliah() {
        return matakuliah;
    }

    public void setMatakuliah(Matakuliah matakuliah) {
        this.matakuliah = matakuliah;
    }

    public RealmList<KRS_detail> getKrsDetails() {
        return krsDetails;
    }

    public void setKrsDetails(RealmList<KRS_detail> krsDetails) {
        this.krsDetails = krsDetails;
    }
}