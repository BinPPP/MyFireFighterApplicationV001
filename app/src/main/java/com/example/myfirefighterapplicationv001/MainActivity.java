package com.example.myfirefighterapplicationv001;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addBtn = findViewById(R.id.setBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText num1 =  findViewById(R.id.textValue1);
                EditText num2 =  findViewById(R.id.textValue2);
                EditText num3 =  findViewById(R.id.textValue3);
                EditText num4 =  findViewById(R.id.textValue4);
                TextView value1 =  findViewById(R.id.valueView1);
                TextView value2 = findViewById(R.id.valueView2);
                TextView value3 =  findViewById(R.id.valueView3);
                TextView value4 = findViewById(R.id.valueView4);
                //Bluetooth function should happen while clicked (sending data)
                //demo onClick function for the moment (just displaying the set numbers)
                value1.setText(num1.getText().toString());
                value2.setText(num2.getText().toString());
                value3.setText(num3.getText().toString());
                value4.setText(num4.getText().toString());

            }
        });
    }
}
