package com.tacttiles.touche;

import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tacttiles.api.Device;
import com.tacttiles.api.Glove;

public class FrasesActivity extends Activity {
    private Glove glove;
    private TextView textFrases;
    private Button buttonFrases;
    private static String [] frases = {"BOM DIA", "BOA TARDE", "BOA NOITE", "ESTOU BEM"};
    private static int tick = 8000;
    private static int tempo_timer =(frases.length+1)*tick;
    private static int i = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frases);

        textFrases = (TextView) findViewById(R.id.textFrases);
        buttonFrases = (Button) findViewById(R.id.buttonFrases);

        glove = new Glove();

        glove.connect(getApplicationContext());

        glove.addListener(new Glove.GloveListener(){
            boolean inputEnabled = false;

            @Override
            public void onLetterDraw(int index, char[] str) {

                //INSERTCODE
                //
            }

            @Override
            public void onLetterReceived(char c) {
                if (inputEnabled) {
                    //textAlfabet.setText(""+c);
                } else {
                    if (c == ' '){
                        //textAlfabet.setText("A");
                    }
                }
            }

            @Override
            public void onButtonPressed(int time) {
                inputEnabled = !inputEnabled;
                if (inputEnabled){
                    glove.getDevice().vibrateDevice(1,100);
                } else {
                    glove.getDevice().vibrateDevice(3,50);
                }
            }

            @Override
            public void onDeviceFound() {


            }

            @Override
            public void onDeviceLost() {

            }

        });

        buttonFrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                CountDownTimer timer = new CountDownTimer(tempo_timer, tick) {
                    @Override
                    public void onTick(long l) {
                        textFrases.setText(frases[i]);
                        onSendMessage(frases[i]);
                        i++;
                    }

                    @Override
                    public void onFinish() {
                        i = 0;
                        glove.getDevice().vibrateDevice(3, 100);
                        textFrases.setText("FIM!");
                    }
                };
                timer.start();

            }
        });
    }

    @Override
    protected void onDestroy() {
        //FIXME:
        glove.getDevice().powerOff();
        glove.getDevice().getServiceConnection().disconnect();
        super.onDestroy();
    }


    public void onSendMessage(String msg) {

        //TODO send multiple messages
        glove.draw(msg.toLowerCase()).start();
    }

}
