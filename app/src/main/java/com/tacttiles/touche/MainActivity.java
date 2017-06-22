package com.tacttiles.touche;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.tacttiles.touche.alfabet.AlfabetActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void openAlfabetActivity(View view) {
        Intent i = new Intent(this, AlfabetActivity.class);
        startActivity(i);
    }

    public void openPalavrasActivity(View view) {
        Intent i = new Intent(this, PalavrasActivity.class);
        startActivity(i);
    }

    public void openFrasesActivity(View view) {
        Intent i = new Intent(this, FrasesActivity.class);
        startActivity(i);
    }

    public void openChatActivity(View view) {
        Intent i = new Intent(this, ToucheMainActivity.class);
        startActivity(i);
    }
}
