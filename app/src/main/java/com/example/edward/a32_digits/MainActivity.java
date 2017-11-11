package com.example.edward.a32_digits;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private EditText mFirst16Text;
    private EditText mLast16Text;
    private TextView mDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.button);
        mFirst16Text = findViewById(R.id.first16View);
        mLast16Text = findViewById(R.id.last16View);
        mDateView = findViewById(R.id.dateView);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String first16 = mFirst16Text.getText().toString();
                String last16 = mLast16Text.getText().toString();
                String distributionDate = setDistributionDate(first16);
                Toast.makeText(MainActivity.this, getInfo(first16, last16),
                        Toast.LENGTH_SHORT).show();
                mDateView.setText(distributionDate);

            }
        });
    }

    public String getInfo(String first16, String last16) {
        return first16 + last16;
    }

    public String setDistributionDate(String first16) {
        String date = first16.substring(0, 5);
        String year = date.substring(0, 1);
        String month = date.substring(1, 3);
        String day = date.substring(3);
        StringBuilder sb = new StringBuilder();
        return sb.append("201" + year + "." + month + "." + day).toString();

    }
}
