package com.tacttiles.touche;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PalavrasActivity extends AppCompatActivity {

    private TextView textPalavras;
    private Button buttonStartPalavras;
    private static String [] palavras = {"SIM", "OI", "EU", "NÃO", "TCHAU", "TESTE"};
    private static int tempo_timer =(palavras.length+1)*1500;
    private static int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palavras);


        textPalavras = (TextView) findViewById(R.id.textPalavras);
        buttonStartPalavras = (Button) findViewById(R.id.buttonStartPalavras);

        buttonStartPalavras.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //List<String> alfabeto = new ArrayList<String>();


                CountDownTimer timer = new CountDownTimer(tempo_timer, 1500) {
                    @Override
                    public void onTick(long l) {
                        textPalavras.setText(palavras[i]);
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
