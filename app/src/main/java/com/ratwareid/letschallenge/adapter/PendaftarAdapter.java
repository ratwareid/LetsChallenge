package com.ratwareid.letschallenge.adapter;

//***********************************//
// Created by Jerry Erlangga         //
// My Repo: www.github.com/ratwareid //
// Email : jerryerlangga82@gmail.com //
//***********************************//


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ratwareid.letschallenge.R;
import com.ratwareid.letschallenge.model.Pendaftar;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PendaftarAdapter extends RecyclerView.Adapter<PendaftarAdapter.ViewHolder> {

    private ArrayList<Pendaftar> mPendaftar;
    private Context mContext;
    private Activity activity;

    public PendaftarAdapter(Context context, ArrayList<Pendaftar> mPendaftar, Activity activity) {
        this.mPendaftar = mPendaftar;
        this.mContext = context;
        this.activity = activity;
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        //Member Variables for the TextViews
        private CircleImageView tandahadir;
        private TextView emailpendaftar;

        ViewHolder(View itemView) {
            super(itemView);

            //Initialize the views
            emailpendaftar =itemView.findViewById(R.id.emailpendaftar);
            tandahadir = itemView.findViewById(R.id.tandahadir);
        }

        void bindTo(Pendaftar pendaftar){
            //Populate the textviews with data
            emailpendaftar.setText(pendaftar.getEmail());
            if (pendaftar.getfHadir() !=null && pendaftar.getfHadir().equals("Y")){
                tandahadir.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public PendaftarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        return new PendaftarAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recyler_pendaftar, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PendaftarAdapter.ViewHolder holder, int position) {
        Pendaftar pendaftar = mPendaftar.get(position);
        //Populate the textviews with data
        holder.bindTo(pendaftar);
    }

    @Override
    public int getItemCount() {
        return mPendaftar.size();
    }


}
