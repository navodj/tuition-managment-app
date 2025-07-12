package com.example.tuitioninfoapp.fragments.student;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tuitioninfoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MyAttendanceFragment extends Fragment {

    private ImageView qrImageView;
    private TextView titleText;

    public MyAttendanceFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_r_code_generator, container, false);

        qrImageView = view.findViewById(R.id.qr_image);
        titleText = view.findViewById(R.id.text_title);

        generateQRCode();

        return view;
    }

    private void generateQRCode() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            String name = user.getDisplayName() != null ? user.getDisplayName() : "Student";
            String content = uid; // or use JSON: "{\"uid\":\"" + uid + "\"}"

            try {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.encodeBitmap(content,
                        BarcodeFormat.QR_CODE, 500, 500);
                qrImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
