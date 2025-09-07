package edu.uph.m23si2.pertamaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import edu.uph.m23si2.pertamaapp.api.ApiResponseKabupaten;
import edu.uph.m23si2.pertamaapp.model.KRS;
import edu.uph.m23si2.pertamaapp.model.KRS_detail;
import edu.uph.m23si2.pertamaapp.model.Kabupaten;
import edu.uph.m23si2.pertamaapp.model.KelasMatakuliah;
import edu.uph.m23si2.pertamaapp.model.Mahasiswa;
import edu.uph.m23si2.pertamaapp.model.Matakuliah;
import edu.uph.m23si2.pertamaapp.model.Prodi;
import edu.uph.m23si2.pertamaapp.model.Provinsi;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText edtNama, edtPassword;
    Spinner sprProvinsi, sprKabupaten;
    List<Provinsi> provinsiList = new ArrayList<>();
    List<Kabupaten> kabupatenList = new ArrayList<>();
    List<String> namaProvinsi = new ArrayList<>();
    List<String> namaKabupaten = new ArrayList<>();
    ArrayAdapter<String> provinsiAdapter;
    ArrayAdapter<String> kabupatenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("default.realm")
                .schemaVersion(1)
                .allowWritesOnUiThread(true) // sementara aktifkan untuk demo
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        initData();
        btnLogin = findViewById(R.id.btnLogin);
        edtNama = findViewById(R.id.edtNama);
        edtPassword = findViewById(R.id.edtPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDashboard();
            }
        });

        sprProvinsi = findViewById(R.id.sprProvinsi);
        provinsiAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,namaProvinsi);
        provinsiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sprProvinsi.setAdapter(provinsiAdapter);

        sprKabupaten = findViewById(R.id.sprKabupaten);
        kabupatenAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, namaKabupaten);
        kabupatenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sprKabupaten.setAdapter(kabupatenAdapter);

        //init retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://wilayah.id")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);


        //panggil API
        apiService.getProvinsi().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    provinsiList = response.body().getData();
                    namaProvinsi.clear();
                    for(Provinsi p: provinsiList){
                        if(p.getName()!=null){
                            Log.d("Provinsi", p.getName());
                            namaProvinsi.add(p.getName());
                        }
                    }

                    provinsiAdapter.notifyDataSetChanged();

                    sprProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Provinsi selected = provinsiList.get(position);
                            Log.d("Provinsi", selected.getCode() + " - " + selected.getName());

                            // panggil kabupaten berdasarkan provinsi yg dipilih
                            apiService.getKabupaten(selected.getCode()).enqueue(new Callback<ApiResponseKabupaten>() {
                                @Override
                                public void onResponse(Call<ApiResponseKabupaten> call, Response<ApiResponseKabupaten> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        kabupatenList = response.body().getData();
                                        namaKabupaten.clear();
                                        for (Kabupaten k : kabupatenList) {
                                            if (k.getName() != null) {
                                                namaKabupaten.add(k.getName());
                                            }
                                        }
                                        kabupatenAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponseKabupaten> call, Throwable t) {
                                    Toast.makeText(LoginActivity.this, "Gagal : " + t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Gagal :"+t.getMessage(),Toast.LENGTH_LONG);
            }
        });
    }

    public void initData() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(r -> {
            if (r.where(Prodi.class).findAll().isEmpty()) {
                // Prodi
                Number maxId = r.where(Mahasiswa.class).max("studentID");
                Prodi prodiSI = r.createObject(Prodi.class,0);
                prodiSI.setFakultas("Fakultas Teknologi Informasi");
                prodiSI.setNama("Sistem Informasi");

                // Matakuliah
                Matakuliah matMobile = r.createObject(Matakuliah.class, 0);
                matMobile.setNama("Pemrograman Mobile Lanjut");
                matMobile.setSks(3);
                matMobile.setProdi(prodiSI);

                Matakuliah matPBO = r.createObject(Matakuliah.class, 1);
                matPBO.setNama("Pemrograman Berorientasi Objek");
                matPBO.setSks(3);
                matPBO.setProdi(prodiSI);

                // Mahasiswa
                Mahasiswa mhs1 = r.createObject(Mahasiswa.class, 0015);
                mhs1.setNama("Michelle");
                mhs1.setEmail("03081230015@student.uph.edu");
                mhs1.setProdi("Sistem Informasi");
                mhs1.setHobi("Olahraga");
                mhs1.setJenisKelamin("Perempuan");

                // KelasMatakuliah
                KelasMatakuliah kelas1 = r.createObject(KelasMatakuliah.class, 2001);
                kelas1.setNamaKelas("PBO 23SI2");
                kelas1.setDosen("Pak Ade");
                kelas1.setMatakuliah(matPBO);

                // KRS
                KRS krs1 = r.createObject(KRS.class, 2956);
                krs1.setSemester("Ganjil");
                krs1.setTahunAjaran("2025/2026");
                krs1.setMahasiswa(mhs1);

                // KRS_detail
                KRS_detail detail1 = r.createObject(KRS_detail.class, 2956);
                detail1.setStatus("Aktif");
                detail1.setKrs(krs1);
                detail1.setKelasMatakuliah(kelas1);
            }
        });
        Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show();
    }

    public void toProfil(){
        Intent intent = new Intent(this,ProfilActivity.class);
        intent.putExtra("nama",edtNama.getText().toString());
        startActivity(intent);
    }
    public void toDashboard(){
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
    }
}