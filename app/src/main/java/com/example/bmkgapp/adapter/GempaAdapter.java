package com.example.bmkgapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bmkgapp.DetailGempaActivity;
import com.example.bmkgapp.R;
import com.example.bmkgapp.model.GempaModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GempaAdapter extends RecyclerView.Adapter<GempaAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GempaModel> data;

    public GempaAdapter(Context context, ArrayList<GempaModel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public GempaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_gempa, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GempaAdapter.ViewHolder holder, int position) {
        GempaModel model = data.get(position);

        holder.itemTanggal.setText(model.getTanggal());
        holder.itemWilayah.setText(model.getWilayah());
        holder.itemMagnitude.setText("M " + model.getMagnitude());

        String url = "https://data.bmkg.go.id/DataMKG/TEWS/" + model.getShakemap();

        Picasso.get()
                .load(url)
                .placeholder(R.drawable.placeholder_shakemap)
                .error(R.drawable.placeholder_shakemap)
                .into(holder.imgShakemapThumb);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailGempaActivity.class);
            intent.putExtra("tanggal", model.getTanggal());
            intent.putExtra("jam", model.getJam());
            intent.putExtra("wilayah", model.getWilayah());
            intent.putExtra("magnitude", model.getMagnitude());
            intent.putExtra("kedalaman", model.getKedalaman());
            intent.putExtra("coordinates", model.getCoordinates());
            intent.putExtra("shakemap", model.getShakemap());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemTanggal, itemWilayah, itemMagnitude;
        ImageView imgShakemapThumb;

        public ViewHolder(View itemView) {
            super(itemView);

            itemTanggal = itemView.findViewById(R.id.itemTanggal);
            itemWilayah = itemView.findViewById(R.id.itemWilayah);
            itemMagnitude = itemView.findViewById(R.id.itemMagnitude);
            imgShakemapThumb = itemView.findViewById(R.id.imgShakemapThumb);
        }
    }
}

