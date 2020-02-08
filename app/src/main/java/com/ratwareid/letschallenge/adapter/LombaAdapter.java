package com.ratwareid.letschallenge.adapter;

//***********************************//
// Created by Jerry Erlangga         //
// My Repo: www.github.com/ratwareid //
// Email : jerryerlangga82@gmail.com //
//***********************************//


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ratwareid.letschallenge.R;
import com.ratwareid.letschallenge.activity.HomeActivity;
import com.ratwareid.letschallenge.activity.ListLombaActivity;
import com.ratwareid.letschallenge.activity.ListLombaDetailActivity;
import com.ratwareid.letschallenge.model.Jenis;
import com.ratwareid.letschallenge.model.Lomba;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class LombaAdapter extends RecyclerView.Adapter<LombaAdapter.ViewHolder> {

    private ArrayList<Lomba> mLomba;
    private Context mContext;
    private Activity activity;

    public LombaAdapter(Context context, ArrayList<Lomba> lomba, Activity activity) {
        this.mLomba = lomba;
        this.mContext = context;
        this.activity = activity;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Member Variables for the TextViews
        private RelativeLayout imgLomba;
        private TextView judullomba, tanggallomba, lokasilomba, textPrice;

        ViewHolder(View itemView) {
            super(itemView);

            //Initialize the views
            imgLomba = itemView.findViewById(R.id.CIV_lomba);
            judullomba = itemView.findViewById(R.id.TV_judullomba);
            tanggallomba = itemView.findViewById(R.id.TV_tanggallomba);
            lokasilomba = itemView.findViewById(R.id.TV_lokasilomba);
            textPrice = itemView.findViewById(R.id.textPrice);

            itemView.setOnClickListener(this);
        }

        void bindTo(Lomba lomba) {
            //Populate the textviews with data
            judullomba.setText(lomba.getNama_lomba());
            tanggallomba.setText(lomba.getTanggal_lomba());
            lokasilomba.setText(lomba.getAlamat_lomba());
            if (lomba.getBiaya_pendaftaran() != null) {
                String idrFormat = NumberFormat.getCurrencyInstance(new Locale("id", "id")).format(Long.valueOf(lomba.getBiaya_pendaftaran()));

                textPrice.setText(idrFormat);
            } else {
                textPrice.setText("Gratis");
            }

            String imgdecoded = lomba.getLogo_lomba();
            byte[] decodedString = Base64.decode(imgdecoded, Base64.DEFAULT);
            imgLomba.setBackground(new BitmapDrawable(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length)));
        }

        @Override
        public void onClick(View view) {
            Lomba lomba = mLomba.get(getAdapterPosition());
            //TODO::Event ketika di klik
            Intent myIntent = new Intent(activity, ListLombaDetailActivity.class);
            myIntent.putExtra("jenis_kode", lomba.getJenis_lomba());
            myIntent.putExtra("key", lomba.getKey());
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity);
            activity.startActivity(myIntent, options.toBundle());
        }
    }

    @Override
    public LombaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        return new LombaAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_lomba, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LombaAdapter.ViewHolder holder, int position) {
        Lomba lomba = mLomba.get(position);
        //Populate the textviews with data
        holder.bindTo(lomba);
    }

    @Override
    public int getItemCount() {
        return mLomba.size();
    }
}
