package com.example.bmkgapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class DetailGempaActivity extends AppCompatActivity {

    TextView txtTanggalDetail, txtJamDetail, txtWilayahDetail,
            txtMagnitudeDetail, txtKedalamanDetail, txtKoordinatDetail;

    ImageView imgShakemapDetail;
    ShimmerFrameLayout shimmerLayout;
    Button btnLihatPetaDetail;

    String lintang = "", bujur = "", wilayah = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_gempa);

        // ==== Toolbar ====
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detail Gempa");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // ==== UI ====
        shimmerLayout = findViewById(R.id.shimmerLayout);
        imgShakemapDetail = findViewById(R.id.imgShakemapDetail);

        txtTanggalDetail = findViewById(R.id.txtTanggalDetail);
        txtJamDetail = findViewById(R.id.txtJamDetail);
        txtWilayahDetail = findViewById(R.id.txtWilayahDetail);
        txtMagnitudeDetail = findViewById(R.id.txtMagnitudeDetail);
        txtKedalamanDetail = findViewById(R.id.txtKedalamanDetail);
        txtKoordinatDetail = findViewById(R.id.txtKoordinatDetail);

        btnLihatPetaDetail = findViewById(R.id.btnLihatPetaDetail);

        // ==== Ambil data dari Intent ====
        String tanggal = getIntent().getStringExtra("tanggal");
        String jam = getIntent().getStringExtra("jam");
        wilayah = getIntent().getStringExtra("wilayah");
        String magnitude = getIntent().getStringExtra("magnitude");
        String kedalaman = getIntent().getStringExtra("kedalaman");
        String koordinat = getIntent().getStringExtra("coordinates");
        String shakemap = getIntent().getStringExtra("shakemap");

        // Set teks
        txtTanggalDetail.setText("Tanggal: " + tanggal);
        txtJamDetail.setText("Jam: " + jam);
        txtWilayahDetail.setText("Wilayah: " + wilayah);
        txtMagnitudeDetail.setText("Magnitudo: " + magnitude);
        txtKedalamanDetail.setText("Kedalaman: " + kedalaman);
        txtKoordinatDetail.setText("Koordinat: " + koordinat);

        // Ambil koordinat untuk maps
        if (koordinat != null && koordinat.contains(",")) {
            String[] split = koordinat.split(",");
            if (split.length == 2) {
                lintang = split[0].trim();
                bujur = split[1].trim();
            }
        }

        // ==== TOMBOL LIHAT PETA ====
        btnLihatPetaDetail.setOnClickListener(v -> {
            if (!lintang.isEmpty()) {
                String url = "geo:" + lintang + "," + bujur + "?q=" + lintang + "," + bujur + "(" + Uri.encode(wilayah) + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

        // ==== Load ShakeMap ====
        String url = "https://data.bmkg.go.id/DataMKG/TEWS/" + shakemap;

        shimmerLayout.startShimmer();

        Picasso.get()
                .load(url)
                .placeholder(R.drawable.placeholder_shakemap)
                .error(R.drawable.placeholder_shakemap)
                .into(imgShakemapDetail, new Callback() {
                    @Override
                    public void onSuccess() {
                        shimmerLayout.stopShimmer();
                        shimmerLayout.hideShimmer();
                    }

                    @Override
                    public void onError(Exception e) {
                        shimmerLayout.stopShimmer();
                        shimmerLayout.hideShimmer();
                        imgShakemapDetail.setImageResource(R.drawable.placeholder_shakemap);
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}





