package com.example.edward.a32_digits;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class LicenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        ArrayList<String> infoList = readExcel(getIntent().getStringExtra("licenseKey"));
        TextView mLicenseView = findViewById(R.id.licenseID);
        TextView mCommerceNameView = findViewById(R.id.commerceNameID);
        TextView mAddressView = findViewById(R.id.addressID);
        TextView mHandlerView = findViewById(R.id.handlerID);
        TextView mContactNameView = findViewById(R.id.contactNameID);
        TextView contactNumView = findViewById(R.id.contactNumID);
        TextView IDNumView = findViewById(R.id.IDNumID);
        TextView CommerceNumView = findViewById(R.id.commerceNumID);
        TextView startDateView = findViewById(R.id.startDateID);
        TextView endDateView = findViewById(R.id.endDateID);
        TextView belongsToView = findViewById(R.id.belongsToID);
        TextView statusView = findViewById(R.id.statusID);

        mLicenseView.setText(infoList.get(0));
        mCommerceNameView.setText(infoList.get(1));
        mAddressView.setText(infoList.get(2));
        mHandlerView.setText(infoList.get(3));
        mContactNameView.setText(infoList.get(4));

        contactNumView.setText(infoList.get(5));
        IDNumView.setText(infoList.get(6));
        CommerceNumView.setText(infoList.get(7));
        startDateView.setText(infoList.get(8));
        endDateView.setText(infoList.get(9));
        belongsToView.setText(infoList.get(10));
        statusView.setText(infoList.get(11));
    }

    public ArrayList<String> readExcel(String target) {
        ArrayList<String> result = new ArrayList<>(Arrays.asList("未知", "未知", "未知", "未知",
                "未知", "未知", "未知", "未知", "未知", "未知", "未知", "未知"));
        Integer row = 0;
        Boolean found = false;
        try {
            Workbook wb = Workbook.getWorkbook(getAssets().open("licenses20171114.xls"));
            Sheet sheet = wb.getSheet(0);
            for(int i = 2; i < sheet.getRows(); i++) {
                String temp = sheet.getCell(0, i).getContents();
                if (temp.equals(target)) {
                    row = i;
                    found = true;
                    break;
                }
            }
            if (found) {
                result.add(0, sheet.getCell(0, row).getContents());
                result.add(1, sheet.getCell(1, row).getContents());
                result.add(2, sheet.getCell(2, row).getContents());
                result.add(3, sheet.getCell(3, row).getContents());
                result.add(4, sheet.getCell(5, row).getContents());
                result.add(5, sheet.getCell(4, row).getContents());
                result.add(6, sheet.getCell(6, row).getContents());
                result.add(7, sheet.getCell(7, row).getContents());
                result.add(8, sheet.getCell(11, row).getContents());
                result.add(9, sheet.getCell(37, row).getContents());
                result.add(10, sheet.getCell(15, row).getContents());
                result.add(11, sheet.getCell(38, row).getContents());
            } else {
                getAlertDialog("零售户不在本区之内");
            }
            return result;
        } catch (BiffException e) {
            Log.e("Workbook Exception", e.getMessage());
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
        }
        return result;
    }

    public void getAlertDialog(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                LicenseActivity.this);

        // set title
        alertDialogBuilder.setTitle("警告！");

        // set dialog message
        alertDialogBuilder
                .setMessage("数据出错\n" + msg)
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
