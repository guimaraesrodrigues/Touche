package com.tacttiles.touche.alfabet;

import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.tacttiles.touche.R;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class AlfabetActivity  extends AppCompatActivity {

    private TextView textAlfabet;
    private Button buttonStartAlfabet;
    private static char [] alfabeto = {'A', 'B', 'C', 'D', 'E', 'F'};
    private static int tempo_timer =(alfabeto.length+1)*1500;
    private static int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alfabet);


        textAlfabet = (TextView) findViewById(R.id.textAlfabet);
        buttonStartAlfabet = (Button) findViewById(R.id.buttonStartAlfabet);


        buttonStartAlfabet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //List<String> alfabeto = new ArrayList<String>();


                CountDownTimer timer = new CountDownTimer(tempo_timer, 1500) {
                    @Override
                    public void onTick(long l) {
                        textAlfabet.setText("" + alfabeto[i]);
                        i++;
                    }

                    @Override
                    public void onFinish() {
                        i = 0;
                    }
                };
                timer.start();

            }
        });
    }


}
