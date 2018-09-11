package com.ggsoft.super3enraya;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.ggsoft.super3enraya.exception.JugadaIncorrectaException;
import com.ggsoft.super3enraya.model.MasterTresEnRaya;
import com.ggsoft.super3enraya.util.MarcadorPantalla;

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
                Snackbar.make(view, "Proximamente podras recomendarla a todos, por ahora mantenlo en secreto =)", Snackbar.LENGTH_LONG)
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
                Snackbar.make(view, "FELICIDADES GANO - "+signoActual.toUpperCase(), Snackbar.LENGTH_INDEFINITE)
                        .setAction("Action", null).show();
            }

            MarcadorPantalla.checkCasilla((ImageButton) view, signoActual);
            MarcadorPantalla.dibujarCeldasPermitidas(master.getCeldasPermitidas(),master.getCuadroMayor(),this);
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

    public void reiniciarJuego(View view) {
        //Basicamente reiniciar la applicacion -- alternativa rapida
        Intent mStartActivity = new Intent(this.getApplicationContext(), MainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(this.getApplicationContext(), mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)this.getApplicationContext().getSystemService(this.getApplicationContext().ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

    public void entrenarJuego(View view) {
        Snackbar.make(view, "Esto viene en la version paga, espere un poco por favor =)", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void aprendaJuego(View view) {
        Snackbar.make(view, "Es un juego para dos personas por si no lo ha notado, y facilmente los colores lo podran orientar, activa tu mente, Proximamente mas detalles", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
