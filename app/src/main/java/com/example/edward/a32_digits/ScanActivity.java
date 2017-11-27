package com.example.edward.a32_digits;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.znq.zbarcode.CaptureActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ScanActivity extends AppCompatActivity {

//    private Button mCaptureButton;
    private String barCode;
    private ArrayList<String> result = new ArrayList<>(Arrays.asList("未知", "未知", "未知", "未知", "未知", "未知"));
    private TextView mBarCodeView;
    private TextView mCigaretteNameView;
    private TextView mWholeSalePriceView;
    private TextView mSuggestedPriceView;
    private TextView mManufacturerView;
    private TextView mSellInBeijingView;

    private RadioButton mRadioButton1;
    private RadioButton mRadioButton2;
    private RadioButton mRadioButton3;
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

        mRadioButton1 = findViewById(R.id.radioButton1);
        mRadioButton2 = findViewById(R.id.radioButton2);
        mRadioButton3 = findViewById(R.id.radioButton3);

        mCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CaptureActivity.class);
                startActivityForResult(i, QR_CODE);
            }
        });
        mRadioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(ScanActivity.this, "radio 1 clicked", Toast.LENGTH_SHORT).show();
                if (result.get(0).equals("未知")) {
                    getAlertDialog("请先扫描");
                } else {
                    setText(barCode);
                }

            }
        });
        mRadioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(ScanActivity.this, "radio 2 clicked", Toast.LENGTH_SHORT).show();
                if (result.get(0).equals("未知")) {
                    getAlertDialog("请先扫描");
                } else {
                    setText(barCode);
                }
            }
        });
        mRadioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(ScanActivity.this, "radio 3 clicked", Toast.LENGTH_SHORT).show();
                if (result.get(0).equals("未知")) {
                    getAlertDialog("请先扫描");
                } else {
                    setText(barCode);
                }
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
                barCode = b.getString(CaptureActivity.EXTRA_STRING);
                setText(barCode);
            } catch (NullPointerException e) {
                Log.e("capture", e.getMessage());
            }
        }
    }

    public ArrayList<String> readExcel(String barCode) {
        result.set(0, barCode);
        try {
            if (mRadioButton1.isChecked()) {
                Workbook wb = Workbook.getWorkbook(getAssets().open("barCodeDB/20171121全国卷烟在销名录.xls"));
                Sheet sheet = wb.getSheet(0);
                for(int i = 2; i < sheet.getRows(); i++) {
                    String tempBar = sheet.getCell(14, i).getContents().trim();
                    String tempBox = sheet.getCell(15, i).getContents().trim();
                    if (tempBar.equals(barCode) || tempBox.equals(barCode)) {
                        result.set(1, sheet.getCell(2, i).getContents().trim());
                        result.set(2, sheet.getCell(12, i).getContents().trim());
                        result.set(3, sheet.getCell(13, i).getContents().trim());
                        result.set(4, sheet.getCell(3, i).getContents().trim());
                        result.set(5, sheet.getCell(21,i).getContents().trim());
                        break;
                    }
                }
            }
            if (mRadioButton2.isChecked()) {
                Workbook wb = Workbook.getWorkbook(getAssets().open("barCodeDB/20171122三统一系统条码库.xls"));
                Sheet sheet = wb.getSheet(0);
                for (int i = 1; i < sheet.getRows(); i++) {
                    String tempBar = sheet.getCell(0, i).getContents().trim();
                    String tempBox = sheet.getCell(1, i).getContents().trim();
                    if (tempBar.equals(barCode) || tempBox.equals(barCode)) {
                        result.set(1, sheet.getCell(2, i).getContents().trim());
                        break;
                    }
                }
            }
            if (mRadioButton3.isChecked()) {
                Workbook wb = Workbook.getWorkbook(getAssets().open("barCodeDB/20171123质检站条码库.xls"));
                Sheet sheet = wb.getSheet(0);
                for (int i = 2; i < sheet.getRows(); i++) {
                    String tempBar = sheet.getCell(0, i).getContents().trim();
                    String tempBox = sheet.getCell(1, i).getContents().trim();
                    if (tempBar.equals(barCode) || tempBox.equals(barCode)) {
                        result.set(1, sheet.getCell(2, i).getContents().trim());
                        break;
                    }
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

    public void setText(String barCode) {
        ArrayList<String> all = readExcel(barCode);
        mBarCodeView.setText(all.get(0));
        mCigaretteNameView.setText(all.get(1));
        mWholeSalePriceView.setText(all.get(2));
        mSuggestedPriceView.setText(all.get(3));
        mManufacturerView.setText(all.get(4));
        mSellInBeijingView.setText(all.get(5));
    }
}
