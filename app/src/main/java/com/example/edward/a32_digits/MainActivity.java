package com.example.edward.a32_digits;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button m32Button;
    private Button mBarCodeButton;


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
                Intent i = new Intent(getApplicationContext(), ScanActivity.class);
                startActivity(i);
            }
        });
    }

}
