package com.ggsoft.super3enraya;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.ggsoft.super3enraya.exception.JugadaIncorrectaException;
import com.ggsoft.super3enraya.model.MasterTresEnRaya;
import com.ggsoft.super3enraya.util.MarcadorPantalla;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public MasterTresEnRaya master;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        master = new MasterTresEnRaya(9);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void jugarAqui(View view)
    {
        String name = getResources().getResourceEntryName(view.getId());
        int celda = obtenerPosition(name,1);
        int casilla = obtenerPosition(name,2);

        //guardamos el signo ya que este cambia al realizar la jugada
        String signoActual = master.getProximoSigno();
        try {
            if(master.realizarJugada(celda,casilla)){
                //Fin de Juego: Mostrar Ganador al signoActual
            }

            MarcadorPantalla.checkCasilla((ImageButton) view, signoActual);
            MarcadorPantalla.dibujarCeldasPermitidas(master.getCeldasPermitidas(),this);
        } catch (JugadaIncorrectaException e) {
            //Jugada Indebida mostrar mensaje
            Snackbar.make(view, "EPA!! ahi no puedes jugar", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }

    private void checkCasilla(ImageButton view, String signoActual) {
        if(signoActual.equals(MasterTresEnRaya.X_SIGN)) {
            view.setImageResource(R.drawable.sign_x);
        }else{
            view.setImageResource(R.drawable.sign_o);
        }
    }

    private int obtenerPosition(String name, int pos) {
        String[] text = name.split("-");
        int result = Integer.parseInt(text[pos]);
        return result;
    }
}
