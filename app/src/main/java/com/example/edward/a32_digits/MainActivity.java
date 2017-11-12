package com.example.edward.a32_digits;

import android.content.Context;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.button);
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
                    Toast.makeText(MainActivity.this, "请输入有效的前16位编码",
                            Toast.LENGTH_SHORT).show();
                }

                //check last 16 digits
                if (last16.length() == 16) {
                    String licenseKey = getLicenseView(last16);
                    mLicenseView.setText(licenseKey);
                    List<String> companyAndHome = getCompanyAndHome(last16);
                    mCompanyView.setText(companyAndHome.get(0) + "烟草");
                    mHomeView.setText(companyAndHome.get(1));
                } else {
                    Toast.makeText(MainActivity.this, "请输入有效的后16位编码",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public String getInfo(String first16, String last16) {
        return first16 + last16;
    }

    public String getDistributionDate(String first16) {
        String date = first16.substring(0, 5);
        String year = date.substring(0, 1);
        String month = date.substring(1, 3);
        String day = date.substring(3);

        StringBuilder sb = new StringBuilder();
        return sb.append("201" + year + "." + month + "." + day).toString();
    }

    public String getBoxNum(String first16) {
        String boxNum = first16.substring(5, 14);
        return boxNum;
    }

    public String getDerivedNum(String first16) {
        String derivedNum = first16.substring(14);
        return derivedNum;
    }

    public List<String> getCompanyAndHome(String last16) {
        String companyCode = last16.substring(0, 4).toUpperCase();
        String locationCode = last16.substring(4, 10);
        AssetManager am = MainActivity.this.getAssets();
        String line;
        List<String> result = Arrays.asList("未知", "未知");
//        ArrayList<String> temp = new ArrayList<>();
        try {

            InputStream is = am.open(locationCode.substring(0, 1) + ".txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            while ((line = reader.readLine()) != null) {
                if (line.contains(companyCode + "--" + locationCode.substring(0, 3))) {
//                    temp.add(line);
                    result.set(0, Arrays.asList(line.split("--")).get(1));
                }
                if (line.contains(locationCode)) {
                    result.set(1, Arrays.asList(line.split("--")).get(1));
                }
            }
        } catch (IOException e) {
            Log.d("assetFile", e.getMessage());
        }
        return result;

    }

    public String getLicenseView(String last16) {
        String licenseKey = last16.substring(10);
        return licenseKey;
    }
}
