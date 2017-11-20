package com.example.edward.a32_digits;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class Digit32Activity extends AppCompatActivity {

    private Button mButton;
    private EditText mFirst16Text;
    private EditText mLast16Text;
    private TextView mDateView;
    private TextView mBoxView;
    private TextView mDerivedView;
    private TextView mCompanyView;
    private TextView mHomeView;
    private TextView mLicenseView;

//    private TextView m

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digit_32);
        mButton = findViewById(R.id.searchButton);

        mFirst16Text = findViewById(R.id.first16View);
        mLast16Text = findViewById(R.id.last16View);
        mDateView = findViewById(R.id.dateView);
        mBoxView = findViewById(R.id.boxView);
        mDerivedView = findViewById(R.id.derivedView);
        mCompanyView = findViewById(R.id.companyView);
        mHomeView = findViewById(R.id.homeView);
        mLicenseView = findViewById(R.id.licenseView);

        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //hide virtual keyboard after pressing button
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null :
                        getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                String first16 = mFirst16Text.getText().toString();
                String last16 = mLast16Text.getText().toString();

                //check first 16 digits
                if (first16.length() == 16){
                    String distributionDate = getDistributionDate(first16);
                    String boxNum = getBoxNum(first16);
                    String derivedNum = getDerivedNum(first16);
                    mDateView.setText(distributionDate);
                    mBoxView.setText(boxNum);
                    mDerivedView.setText(derivedNum);
                } else {
                    Toast.makeText(Digit32Activity.this, "请输入有效的前16位编码",
                            Toast.LENGTH_SHORT).show();
                    mDateView.setText(R.string.unknown);
                    mBoxView.setText(R.string.unknown);
                    mDerivedView.setText(R.string.unknown);
                }

                //check last 16 digits
                if (last16.length() == 16) {
                    String licenseKey = getLicenseView(last16);
                    mLicenseView.setText(licenseKey);
                    List<String> companyAndHome = getCompanyAndHome(last16);
                    mCompanyView.setText(companyAndHome.get(0) + "烟草");
                    mHomeView.setText(companyAndHome.get(1));

                    if (companyAndHome.get(0).equals("未知")) {
                        getAlertDialog("烟草公司不存在");
                    }
                    if (companyAndHome.get(1).equals("未知")) {
                        getAlertDialog("归属地不存在");
                    }

                } else {
                    Toast.makeText(Digit32Activity.this, "请输入有效的后16位编码",
                            Toast.LENGTH_SHORT).show();
                    mCompanyView.setText(R.string.unknown);
                    mHomeView.setText(R.string.unknown);
                    mLicenseView.setText(R.string.unknown);

                }


            }
        });
        mLicenseView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LicenseActivity.class);
                i.putExtra("licenseKey", mLicenseView.getText());
                startActivity(i);
            }
        });
    }

    public String getDistributionDate(String first16) {
        String date = first16.substring(0, 5);
        String year = "201" + date.substring(0, 1);
        String month = date.substring(1, 3);
        String day = date.substring(3);
        String pattern = "yyyy.MM.dd";
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            format.setLenient(false);
            format.parse(year + "." + month + "." + day);
        } catch (ParseException e) {
            getAlertDialog("日期不存在");
            return "未知";
        } catch (IllegalArgumentException e) {
            getAlertDialog("日期不存在");
            return "未知";
        }

        StringBuilder sb = new StringBuilder();
        return sb.append(year + "." + month + "." + day).toString();
    }

    public String getBoxNum(String first16) {
        String boxNum = first16.substring(5, 14);
        return boxNum;
    }

    public String getDerivedNum(String first16) {
        String derivedNum = first16.substring(14);
        if (Integer.valueOf(derivedNum) > 50 ) {
            getAlertDialog("派生码无效");
            return "未知";
        }
        return derivedNum;
    }

    public List<String> getCompanyAndHome(String last16) {
        String locationCode = last16.substring(4, 10);
        AssetManager am = Digit32Activity.this.getAssets();
        String line;
        List<String> result = Arrays.asList("未知", "未知");
        try {
            InputStream is = am.open(locationCode.substring(0, 1) + ".txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(locationCode)) {
                    List<String> lineList = Arrays.asList(line.split("--"));
                    result.set(0, lineList.get(3));
                    result.set(1, lineList.get(1));
                } else if (line.startsWith(locationCode.substring(0, 4))) {
                    List<String> lineList = Arrays.asList(line.split("--"));
                    result.set(0, lineList.get(3));
                }
            }
        } catch (IOException e) {
            Log.d("assetFile", e.getMessage());
        }
        return result;

    }

    public String getLicenseView(String last16) {
        String licenseKey = last16.substring(4);
        return licenseKey;
    }

    public void getAlertDialog(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Digit32Activity.this);

        // set title
        alertDialogBuilder.setTitle("警告！");

        // set dialog message
        alertDialogBuilder
                .setMessage("32位条码无效\n" + msg)
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
