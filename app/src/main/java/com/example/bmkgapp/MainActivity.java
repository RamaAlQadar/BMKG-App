package com.example.bmkgapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.bmkgapp.network.ApiClient;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView txtTanggal, txtJam, txtWilayah, txtMagnitude, txtKedalaman, txtKoordinat;
    Button btnLihatPeta;
    ImageView imgShakemap;

    String lintang = "";
    String bujur = "";
    String wilayah = "";
    String shakemapPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ====== SETUP TOOLBAR ======
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ====== INIT UI ======
        txtTanggal = findViewById(R.id.txtTanggal);
        txtJam = findViewById(R.id.txtJam);
        txtWilayah = findViewById(R.id.txtWilayah);
        txtMagnitude = findViewById(R.id.txtMagnitude);
        txtKedalaman = findViewById(R.id.txtKedalaman);
        txtKoordinat = findViewById(R.id.txtKoordinat);
        imgShakemap = findViewById(R.id.imgShakemap);
        btnLihatPeta = findViewById(R.id.btnLihatPeta);

        // Load gempa autogempa.json
        ambilDataBeranda();

        // Tombol Lihat Peta
        btnLihatPeta.setOnClickListener(v -> {
            if (!lintang.isEmpty() && !bujur.isEmpty()) {
                String uri = "geo:" + lintang + "," + bujur + "?q=" + lintang + "," + bujur + "(" + Uri.encode(wilayah) + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    // fallback browser
                    String mapsUrl = "https://www.google.com/maps/search/?api=1&query=" + lintang + "," + bujur;
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl)));
                }
            } else {
                Toast.makeText(this, "Koordinat tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // =============================
    //   AMBIL AUTOGEMPA.JSON
    // =============================
    private void ambilDataBeranda() {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Memuat gempa terbaru...");
        dialog.setCancelable(false);
        dialog.show();

        String url = "https://data.bmkg.go.id/DataMKG/TEWS/autogempa.json";

        StringRequest req = new StringRequest(Request.Method.GET, url, response -> {
            dialog.dismiss();

            try {
                JSONObject obj = new JSONObject(response)
                        .getJSONObject("Infogempa")
                        .getJSONObject("gempa");

                txtTanggal.setText("Tanggal: " + obj.optString("Tanggal", "-"));
                txtJam.setText("Jam: " + obj.optString("Jam", "-"));
                txtWilayah.setText(obj.optString("Wilayah", "-"));
                txtMagnitude.setText("Magnitudo: " + obj.optString("Magnitude", "-"));
                txtKedalaman.setText("Kedalaman: " + obj.optString("Kedalaman", "-"));
                txtKoordinat.setText("Koordinat: " + obj.optString("Coordinates", "-"));

                wilayah = obj.optString("Wilayah", "");
                shakemapPath = obj.optString("Shakemap", "");

                // Ambil koordinat
                String coords = obj.optString("Coordinates", "");
                if (coords.contains(",")) {
                    String[] split = coords.split(",");
                    if (split.length == 2) {
                        lintang = split[0].trim();
                        bujur = split[1].trim();
                    }
                }

                // LOAD SHAKEMAP
                if (!shakemapPath.isEmpty()) {
                    String imgUrl = "https://data.bmkg.go.id/DataMKG/TEWS/" + shakemapPath;

                    Picasso.get()
                            .load(imgUrl)
                            .placeholder(R.drawable.placeholder_shakemap)
                            .error(R.drawable.placeholder_shakemap)
                            .into(imgShakemap);
                } else {
                    imgShakemap.setImageResource(R.drawable.placeholder_shakemap);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal parsing JSON!", Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            dialog.dismiss();
            Toast.makeText(this, "Gagal memuat data BMKG!", Toast.LENGTH_SHORT).show();
        });

        ApiClient.getInstance(this).addToRequestQueue(req);
    }

    // ======================================================
    //                  OVERFLOW MENU (TITIK TIGA)
    // ======================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;

        } else if (id == R.id.menu_list) {
            startActivity(new Intent(this, ListGempaActivity.class));
            return true;

        } else if (id == R.id.menu_latest) {
            startActivity(new Intent(this, GempaTerbaruActivity.class));
            return true;

        } else if (id == R.id.menu_exit) {
            finishAffinity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}






