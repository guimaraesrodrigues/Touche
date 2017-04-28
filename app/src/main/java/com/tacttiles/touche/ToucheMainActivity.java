package com.tacttiles.touche;

import android.os.Bundle;

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
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        glove.getDevice().getServiceConnection().disconnect();
    }

    @Override
    public void onSendMessage(String msg) {
        //TODO send multiple messages
        glove.draw(msg.toLowerCase()).start();
    }
}
