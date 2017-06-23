package com.tacttiles.touche;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import com.tacttiles.api.Glove;

//import com.tacttiles.touche.alfabet.AlfabetActivity;

public class TelaInicialActivity extends Activity {
    Glove glove;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_tela_inicial);
    }

    public void openExerciciosActivity(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void openSobreActivity(View view) {
        Intent i = new Intent(this, SobreActivity.class);
        startActivity(i);
    }
}
