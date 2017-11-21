package com.example.edward.a32_digits;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.znq.zbarcode.CaptureActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MainActivity extends AppCompatActivity {
    private Button m32Button;
    private Button mBarCodeButton;
    private int QR_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m32Button = findViewById(R.id.button32);
        mBarCodeButton = findViewById(R.id.barCodeButton);

        m32Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Digit32Activity.class);
                startActivity(i);
            }
        });

        mBarCodeButton.setOnClickListener(new View.OnClickListener() {
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
            String result = b.getString(CaptureActivity.EXTRA_STRING);
            Toast.makeText(this, result + "", Toast.LENGTH_SHORT).show();
            Log.d("scannedCode", result);

            ArrayList<String> all = readExcel(result);
            StringBuilder sb = new StringBuilder();
            sb.append("编码：" + all.get(0) + "\n");
            sb.append("名称：" + all.get(1) + "\n");
            sb.append("三批价：" + all.get(2) + "\n");
            sb.append("建议零售价：" + all.get(3) + "\n");
            sb.append("单位：" + all.get(4) + "\n");
            sb.append("是否为北京在销卷烟：" + all.get(5) + "\n");
            getAlertDialog(sb.toString());
            Log.d("BarCode", all.get(0));
            Log.d("Name", all.get(1));
            Log.d("WholesalePrice", all.get(2));
            Log.d("Unit", all.get(3));
            Log.d("PS", all.get(4));

        }
    }

    public ArrayList<String> readExcel(String barCode) {
        Integer row = 0;
        Boolean found = false;
        ArrayList<String> result = new ArrayList<>(Arrays.asList(barCode, "未知", "未知", "未知", "未知", "未知"));
        try {
            Workbook wb = Workbook.getWorkbook(getAssets().open("20171121全国卷烟在销名录.xls"));
            Sheet sheet = wb.getSheet(0);
            for(int i = 2; i < sheet.getRows(); i++) {
                String tempBar = sheet.getCell(14, i).getContents().trim();
                String tempBox = sheet.getCell(15, i).getContents().trim();
//                Log.d("read cell", temp);
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

        } catch (IOException e) {
            Log.e("IOException: ", e.getMessage());
        } catch (BiffException e) {
            Log.e("BiffException: ", e.getMessage());
        }
        return result;
    }

    public void getAlertDialog(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);

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
