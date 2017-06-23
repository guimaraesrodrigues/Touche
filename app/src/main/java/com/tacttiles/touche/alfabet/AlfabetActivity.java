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

import com.tacttiles.api.Device;
import com.tacttiles.api.Glove;

public class AlfabetActivity  extends AppCompatActivity {

    private Glove glove;

    private TextView textAlfabet;
    private Button buttonStartAlfabet;
    private static char [] alfabeto_char = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();
    private String alfabeto_string = "abcdefghijklmnopqrstuvwxyz";

    private static int tempo_timer =(alfabeto_char.length+1)*1200;
    private static int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                    textAlfabet.setText(""+c);
                } else {
                    if (c == ' '){
                        textAlfabet.setText("A");
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

        setContentView(R.layout.activity_alfabet);


        textAlfabet = (TextView) findViewById(R.id.textAlfabet);
        buttonStartAlfabet = (Button) findViewById(R.id.buttonStartAlfabet);


        buttonStartAlfabet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                //onSendMessage(alfabeto_string);
                //glove.getDevice().vibrateDevice(3, 100);


               CountDownTimer timer = new CountDownTimer(tempo_timer, 1200) {
                    @Override
                    public void onTick(long l) {
                        textAlfabet.setText("" + alfabeto_char[i]);
                        onSendMessage(""+alfabeto_char[i]);
                        i++;
                    }

                    @Override
                    public void onFinish() {
                        i = 0;
                        glove.getDevice().vibrateDevice(3, 100);
                        textAlfabet.setText("FIM!");
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
