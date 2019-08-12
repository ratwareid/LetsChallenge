package com.ratwareid.letschallenge.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.ratwareid.letschallenge.R;
import com.ratwareid.letschallenge.activity.TambahJenis;
import com.ratwareid.letschallenge.adapter.JenisAdapter;
import com.ratwareid.letschallenge.model.Jenis;

import java.util.ArrayList;

public class AccountFragment extends Fragment implements View.OnClickListener {

    private MaterialButton btnTJ;
    private Button btnper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view) {

        btnTJ = view.findViewById(R.id.btn_TJ);
        btnTJ.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnTJ)){
            Intent intent = new Intent(getActivity(), TambahJenis.class);
            startActivity(intent);
        }
    }
}