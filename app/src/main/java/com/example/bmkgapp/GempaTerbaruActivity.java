package com.example.bmkgapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.bmkgapp.adapter.GempaAdapter;
import com.example.bmkgapp.model.GempaModel;
import com.example.bmkgapp.network.ApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GempaTerbaruActivity extends AppCompatActivity {

    RecyclerView rvGempaTerbaru;
    ArrayList<GempaModel> listGempa;
    GempaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gempa_terbaru);

        Toolbar toolbar = findViewById(R.id.toolbarTerbaru);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Gempa Terbaru");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvGempaTerbaru = findViewById(R.id.rvGempaTerbaru);
        rvGempaTerbaru.setLayoutManager(new LinearLayoutManager(this));

        listGempa = new ArrayList<>();

        ambilData();
    }

    private void ambilData() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Memuat data...");
        dialog.setCancelable(false);
        dialog.show();

        // FIXED: ganti URL ke gempaterkini.json
        String url = "https://data.bmkg.go.id/DataMKG/TEWS/gempaterkini.json";

        StringRequest req = new StringRequest(Request.Method.GET, url, response -> {
            dialog.dismiss();

            try {
                JSONObject obj = new JSONObject(response);
                JSONArray arr = obj.getJSONObject("Infogempa").getJSONArray("gempa");

                listGempa.clear();

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject g = arr.getJSONObject(i);

                    listGempa.add(new GempaModel(
                            g.getString("Tanggal"),
                            g.getString("Jam"),
                            g.getString("Magnitude"),
                            g.getString("Kedalaman"),
                            g.getString("Wilayah"),
                            g.getString("Coordinates"),
                            g.optString("Shakemap", "")
                    ));
                }

                adapter = new GempaAdapter(this, listGempa);
                rvGempaTerbaru.setAdapter(adapter);

            } catch (Exception e) {
                Toast.makeText(this, "Error: JSON berubah!", Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            dialog.dismiss();
            Toast.makeText(this, "Gagal koneksi server BMKG!", Toast.LENGTH_SHORT).show();
        });

        ApiClient.getInstance(this).addToRequestQueue(req);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

