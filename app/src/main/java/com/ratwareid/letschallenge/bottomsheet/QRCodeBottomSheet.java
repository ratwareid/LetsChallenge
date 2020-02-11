package com.ratwareid.letschallenge.bottomsheet;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ratwareid.letschallenge.ImageUtil;
import com.ratwareid.letschallenge.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QRCodeBottomSheet extends BottomSheetDialogFragment {

    ImageView imageBarcode;
    TextView textBarcode;
    Bundle bundle;

    String QrKey, namaLomba;

    public QRCodeBottomSheet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_qrcode_bottom_sheet, container, false);

        initWidgets(v);

        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.screenBrightness = 1F;
        getActivity().getWindow().setAttributes(layoutParams);

        return v;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        getActivity().getWindow().setAttributes(layoutParams);
    }

    private void initWidgets(View v) {
        imageBarcode = v.findViewById(R.id.imageBarcode);
        textBarcode = v.findViewById(R.id.textBarcode);

        bundle = getArguments();

        QrKey = bundle.getString("qrcode");
        namaLomba = bundle.getString("namalomba");

        Bitmap bitmap = ImageUtil.generateQRCODE(QrKey);
        imageBarcode.setImageBitmap(bitmap);

        textBarcode.setText("Barcode | " + namaLomba);
    }

}
