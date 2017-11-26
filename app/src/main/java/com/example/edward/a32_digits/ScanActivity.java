package com.example.edward.a32_digits;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.znq.zbarcode.CaptureActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ScanActivity extends AppCompatActivity {

//    private Button mCaptureButton;
    private TextView mBarCodeView;
    private TextView mCigaretteNameView;
    private TextView mWholeSalePriceView;
    private TextView mSuggestedPriceView;
    private TextView mManufacturerView;
    private TextView mSellInBeijingView;
    private CheckBox mCheckBox1;
    private int QR_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Button mCaptureButton = findViewById(R.id.startCapture);
        mBarCodeView = findViewById(R.id.barCodeView);
        mCigaretteNameView = findViewById(R.id.cigaretteNameView);
        mWholeSalePriceView = findViewById(R.id.wholeSalePriceView);
        mSuggestedPriceView = findViewById(R.id.suggestedPriceView);
        mManufacturerView = findViewById(R.id.manufacturerView);
        mSellInBeijingView = findViewById(R.id.sellInBeijingView);
        mCheckBox1 = findViewById(R.id.checkBox1);

        mCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CaptureActivity.class);
                startActivityForResult(i, QR_CODE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_CODE && resultCode == RESULT_OK) {
            if(null == data) return;
            Bundle b = data.getExtras();
            try {

                String result = b.getString(CaptureActivity.EXTRA_STRING);
                Log.d("scannedCode", result);

                ArrayList<String> all = readExcel(result);
                mBarCodeView.setText(all.get(0));
                mCigaretteNameView.setText(all.get(1));
                mWholeSalePriceView.setText(all.get(2));
                mSuggestedPriceView.setText(all.get(3));
                mManufacturerView.setText(all.get(4));
                mSellInBeijingView.setText(all.get(5));

            } catch (NullPointerException e) {
                Log.e("capture", e.getMessage());
            }


        }
    }

    public ArrayList<String> readExcel(String barCode) {
        Integer row = 0;
        Boolean found = false;
        ArrayList<String> result = new ArrayList<>(Arrays.asList(barCode, "未知", "未知", "未知", "未知", "未知"));
        try {
            if (mCheckBox1.isChecked()) {
                Workbook wb = Workbook.getWorkbook(getAssets().open("barCodeDB/20171121全国卷烟在销名录.xls"));
                Sheet sheet = wb.getSheet(0);
                for(int i = 2; i < sheet.getRows(); i++) {
                    String tempBar = sheet.getCell(14, i).getContents().trim();
                    String tempBox = sheet.getCell(15, i).getContents().trim();
                    if (tempBar.equals(barCode) || tempBox.equals(barCode)) {
                        row = i;
                        found = true;
                        break;
                    }
                }
                if (found) {
                    result.add(1, sheet.getCell(2, row).getContents().trim());
                    result.add(2, sheet.getCell(12, row).getContents().trim());
                    result.add(3, sheet.getCell(13, row).getContents().trim());
                    result.add(4, sheet.getCell(3, row).getContents().trim());
                    result.add(5, sheet.getCell(21,row).getContents().trim());
                }
            }


        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
        } catch (BiffException e) {
            Log.e("BiffException", e.getMessage());
        }
        return result;
    }

    public void getAlertDialog(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ScanActivity.this);

        // set title
        alertDialogBuilder.setTitle("识别结果");

        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
}
