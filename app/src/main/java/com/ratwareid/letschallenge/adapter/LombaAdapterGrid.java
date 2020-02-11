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
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ratwareid.letschallenge.R;
import com.ratwareid.letschallenge.activity.ListLombaDetailActivity;
import com.ratwareid.letschallenge.model.Lomba;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class LombaAdapterGrid extends RecyclerView.Adapter<LombaAdapterGrid.ViewHolder> {

    private ArrayList<Lomba> mLomba;
    private Context mContext;
    private Activity activity;

    public LombaAdapterGrid(Context context, ArrayList<Lomba> lomba, Activity activity) {
        this.mLomba = lomba;
        this.mContext = context;
        this.activity = activity;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Member Variables for the TextViews
        private ImageView imgLomba;
        private TextView titleLomba;

        ViewHolder(View itemView) {
            super(itemView);
            imgLomba = itemView.findViewById(R.id.iv_lomba);
            titleLomba = itemView.findViewById(R.id.tv_title);
            itemView.setOnClickListener(this);
        }

        void bindTo(Lomba lomba){
            String imgdecoded = lomba.getLogo_lomba();
            byte[] decodedString = Base64.decode(imgdecoded, Base64.DEFAULT);
            imgLomba.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
            titleLomba.setText(lomba.getNama_lomba());
        }

        @Override
        public void onClick(View view) {
            Lomba lomba = mLomba.get(getAdapterPosition());
            Intent myIntent = new Intent(activity, ListLombaDetailActivity.class);
            myIntent.putExtra("jenis_kode",lomba.getJenis_lomba());
            myIntent.putExtra("key",lomba.getKey());
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity);
            activity.startActivity(myIntent,options.toBundle());
        }
    }

    @Override
    public LombaAdapterGrid.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        return new LombaAdapterGrid.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_lomba_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LombaAdapterGrid.ViewHolder holder, int position) {
        Lomba lomba = mLomba.get(position);
        holder.bindTo(lomba);
    }

    @Override
    public int getItemCount() {
        return mLomba.size();
    }
}
