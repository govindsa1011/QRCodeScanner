package com.ranjeet.qrcodescannerlibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ranjeet.qrcodescanner.QrCodeActivity;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private TextView txtExcellent, txtMedium, txtPoor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imgQrCode = findViewById(R.id.imgQrCode);

        txtExcellent = findViewById(R.id.txtExcellent);
        txtMedium = findViewById(R.id.txtMedium);
        txtPoor = findViewById(R.id.txtPoor);


        txtExcellent.setText("Excellent\n"+SharedPrefUtils.getIntData(this, "Excellent"));

        txtMedium.setText("Medium\n"+SharedPrefUtils.getIntData(this, "Medium"));

        txtPoor.setText("Poor\n"+SharedPrefUtils.getIntData(this, "Poor"));

        imgQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start the qr scan activity
                Intent i = new Intent(MainActivity.this, QrCodeActivity.class);
                startActivityForResult(i, REQUEST_CODE_QR_SCAN);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            String LOGTAG = "QRCScanner-MainActivity";
            Log.d(LOGTAG, "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.ranjeet.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.ranjeet.qrcodescanner.got_qr_scan_relult");
            int count = SharedPrefUtils.getIntData(this, result);
            count = count + 1;
            SharedPrefUtils.saveData(this, result, count);

            if (result.equals("Excellent")) {
                txtExcellent.setText("Excellent\n" + count);
            } else if (result.equals("Medium")) {
                txtMedium.setText("Medium\n" + count);
            } else {
                txtPoor.setText("Poor\n" + count);
            }
        }
    }
}
