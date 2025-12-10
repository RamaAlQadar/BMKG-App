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

public class ListGempaActivity extends AppCompatActivity {

    RecyclerView rvGempa;
    ArrayList<GempaModel> listGempa;
    GempaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_gempa);

        Toolbar toolbar = findViewById(R.id.toolbarList);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Gempa 5+ Magnitudo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvGempa = findViewById(R.id.rvGempa);
        rvGempa.setLayoutManager(new LinearLayoutManager(this));

        listGempa = new ArrayList<>();

        loadGempa5Plus();
    }

    private void loadGempa5Plus() {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Memuat data gempa...");
        dialog.setCancelable(false);
        dialog.show();

        String url = "https://data.bmkg.go.id/DataMKG/TEWS/gempaterkini.json";

        StringRequest req = new StringRequest(Request.Method.GET, url, response -> {
            dialog.dismiss();

            try {
                JSONObject obj = new JSONObject(response)
                        .getJSONObject("Infogempa");

                JSONArray arr = obj.getJSONArray("gempa");

                listGempa.clear();

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject g = arr.getJSONObject(i);

                    double mag = Double.parseDouble(g.getString("Magnitude"));
                    if (mag < 5) continue;

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
                rvGempa.setAdapter(adapter);

            } catch (Exception e) {
                Toast.makeText(this, "Gagal parsing JSON!", Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            dialog.dismiss();
            Toast.makeText(this, "Koneksi ke server gagal!", Toast.LENGTH_SHORT).show();
        });

        ApiClient.getInstance(this).addToRequestQueue(req);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}


