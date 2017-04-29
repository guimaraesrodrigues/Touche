package com.tacttiles.touche;

import android.os.Bundle;

import com.tacttiles.api.Device;
import com.tacttiles.api.Glove;
import com.tacttiles.touche.chat.ChatActivity;

/**
 * Created by andy on 27/04/17.
 */

public class ToucheMainActivity extends ChatActivity {

    private Glove glove;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glove = new Glove();

        glove.connect(getApplicationContext());

        glove.addListener(new Glove.GloveListener(){

            boolean inputEnabled = false;

            @Override
            public void onLetterDraw(int index, char[] str) {
                setCurrentOutMessageProgress(index);
            }

            @Override
            public void onLetterReceived(char c) {
                if (inputEnabled) {
                    appendToCurrentInMessage(c + "");
                } else {
                    if (c == ' '){
                        flushCurrentInMessage();
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
                enableSendButton(true);
            }

            @Override
            public void onDeviceLost() {
                enableSendButton(false);
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

    @Override
    public void onSendMessage(String msg) {
        if (msg.toLowerCase().equals("po")){
            glove.getDevice().powerOff();
            return;
        }
        //TODO send multiple messages
        glove.draw(msg.toLowerCase()).start();
    }
}
