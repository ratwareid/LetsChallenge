package com.ratwareid.letschallenge.adapter;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ratwareid.letschallenge.R;
import com.ratwareid.letschallenge.activity.ListLombaActivity;
import com.ratwareid.letschallenge.model.Jenis;

import java.util.ArrayList;

public class JenisAdapter extends RecyclerView.Adapter<JenisAdapter.ViewHolder> {

    private ArrayList<Jenis> mJenis;
    private Context mContext;
    private Activity activity;

    public JenisAdapter(Context context, ArrayList<Jenis> jenis, Activity activity) {
        this.mJenis = jenis;
        this.mContext = context;
        this.activity = activity;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Member Variables for the TextViews
        private TextView nameJenis;
        private ImageView imgJenis;

        ViewHolder(View itemView) {
            super(itemView);

            //Initialize the views
            nameJenis =itemView.findViewById(R.id.nama_jenis);
            imgJenis = itemView.findViewById(R.id.img_jenis);
            itemView.setOnClickListener(this);
        }

        void bindTo(Jenis jenis){
            //Populate the textviews with data
            nameJenis.setText(jenis.getNamajenis());
            String imgdecoded = jenis.getImgjenis();
            byte[] decodedString = Base64.decode(imgdecoded, Base64.DEFAULT);
            imgJenis.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
        }

        @Override
        public void onClick(View view) {
            Jenis jenis = mJenis.get(getAdapterPosition());
            //TODO::Event ketika di klik
            Intent myIntent = new Intent(activity, ListLombaActivity.class);
            myIntent.putExtra("jenis_kode",jenis.getKodejenis());
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity);
            activity.startActivity(myIntent,options.toBundle());
        }
    }

    @Override
    public JenisAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        return new JenisAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_jenis, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull JenisAdapter.ViewHolder holder, int position) {
        Jenis jenis = mJenis.get(position);
        //Populate the textviews with data
        holder.bindTo(jenis);
    }

    @Override
    public int getItemCount() {
        return mJenis.size();
    }
}
