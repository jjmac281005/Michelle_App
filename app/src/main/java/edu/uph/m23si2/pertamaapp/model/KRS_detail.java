package edu.uph.m23si2.pertamaapp.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class KRS_detail extends RealmObject {
    @PrimaryKey
    private int detailID;
    private String status; // misalnya: aktif, drop
    private KRS krs; // relasi ke KRS
    private KelasMatakuliah kelasMatakuliah; // relasi ke kelas

    public KRS_detail() {
    }
    public KRS_detail(int detailID, String status, KRS krs, KelasMatakuliah kelasMatakuliah) {
        this.detailID = detailID;
        this.status = status;
        this.krs = krs;
        this.kelasMatakuliah = kelasMatakuliah;
    }

    public int getDetailID() {
        return detailID;
    }

    public void setDetailID(int detailID) {
        this.detailID = detailID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public KRS getKrs() {
        return krs;
    }

    public void setKrs(KRS krs) {
        this.krs = krs;
    }

    public KelasMatakuliah getKelasMatakuliah() {
        return kelasMatakuliah;
    }

    public void setKelasMatakuliah(KelasMatakuliah kelasMatakuliah) {
        this.kelasMatakuliah = kelasMatakuliah;
    }
}
