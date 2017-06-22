package com.tacttiles.touche;

import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FrasesActivity extends Activity {
    private TextView textFrases;
    private Button buttonFrases;
    private static String [] frases = {"BOM DIA", "PROF LEONELO É TOP", "PATRÍCIA É TOP", "ACESSIBILIDADE É LEGAL"};
    private static int tempo_timer =(frases.length+1)*2000;
    private static int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frases);

        textFrases = (TextView) findViewById(R.id.textFrases);
        buttonFrases = (Button) findViewById(R.id.buttonFrases);

        buttonFrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //List<String> alfabeto = new ArrayList<String>();


                CountDownTimer timer = new CountDownTimer(tempo_timer, 2000) {
                    @Override
                    public void onTick(long l) {
                        textFrases.setText(frases[i]);
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
