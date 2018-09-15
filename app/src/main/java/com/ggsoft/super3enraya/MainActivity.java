package com.ggsoft.super3enraya;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.ggsoft.super3enraya.exception.JugadaIncorrectaException;
import com.ggsoft.super3enraya.model.MasterTresEnRaya;
import com.ggsoft.super3enraya.util.MarcadorPantalla;

public class MainActivity extends AppCompatActivity {
    private boolean gameOver = false;
    private int boludoCount=0;
    private boolean isVsComputer = false;

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
        if(!gameOver) {
            String name = getResources().getResourceEntryName(view.getId());
            int celda = obtenerPosition(name, 1);
            int casilla = obtenerPosition(name, 2);

            //guardamos el signo ya que este cambia al realizar la jugada
            String signoActual = master.getProximoSigno();
            try {
                //realizar jugada del player
                evaluarJugada(view, signoActual, master.realizarJugada(celda, casilla));
                redibujarGrilla(master, signoActual);
                if (isVsComputer) {

                    //Se añade un delay para que la jugada sea apreciable por el jugador
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //realizar jugada de la pc
                            String signoActual = master.getProximoSigno();
                            try {
                                evaluarJugada(MainActivity.super.getCurrentFocus(), signoActual, master.realizarJugadaAleatoria());
                            } catch (JugadaIncorrectaException e) {
                               //do nothing, este caso no se debe generar ya que se valida la jugada antes
                                e.printStackTrace();
                            }
                            redibujarGrilla(master, signoActual);
                        }
                    }, 1500); // Millisecond 1000 = 1 sec
                }

                boludoCount = 0;
            } catch (JugadaIncorrectaException e) {
                //Jugada Indebida mostrar mensaje
                boludoCount++;
                if (boludoCount > 2) {
                    boludoCount = 1;
                    Snackbar.make(view, "OJO!! no insistas juega en el verde", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(view, "EPA!! ahi no puedes jugar", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        }else {
            Snackbar.make(view, "Juego culminado debes reiniciar para continuar", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void delay(int i) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Magic here
            }
        }, i*1000); // Millisecond 1000 = 1 sec
    }

    private void evaluarJugada(View view, String signoActual, boolean ganador) {
        if (ganador) {
            //Fin de Juego: Mostrar Ganador al signoActual
            gameOver = true;
            Snackbar.make(view, "FELICIDADES GANO - " + signoActual.toUpperCase(), Snackbar.LENGTH_INDEFINITE)
                    .setAction("Action", null).show();
        } else if (master.juegoCompletado()) {
            //Fin de juego: Se gana por puntos
            Snackbar.make(view, "GAME OVER - " + signoActual.toUpperCase(), Snackbar.LENGTH_INDEFINITE)
                    .setAction("Action", null).show();
            gameOver = true;
        }
    }


    private void redibujarGrilla(MasterTresEnRaya master, String signoActual) {
        ImageButton button = (ImageButton) this.findViewById(this.getResources()
                .getIdentifier("imageButton-" + master.getUltimaCeldaJugada() + "-"
                        + master.getUltimaCasillaJugada(), "id", this.getPackageName()));

        MarcadorPantalla.cambiarJugador(signoActual,this);
        MarcadorPantalla.checkCasilla(button, signoActual);
        MarcadorPantalla.dibujarCeldasPermitidas(master.getCeldasPermitidas(),master.getCuadroMayor(),this);
    }

    private void marcarLineaGanadora() {
        //buscar en el maestro cuales son las celdas ganadoras y cambiar el tinte de las mismas
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
        Snackbar.make(view, "Reiniciara, en  ..3..2..1", Snackbar.LENGTH_INDEFINITE)
                .setAction("Action", null).show();
        Intent mStartActivity = new Intent(this.getApplicationContext(), MainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(this.getApplicationContext(), mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)this.getApplicationContext().getSystemService(this.getApplicationContext().ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);

        System.exit(0);
    }

    public void entrenarJuego(View view) {
        if(!isVsComputer) {
            isVsComputer = true;
            Snackbar.make(view, "Modo Aleatorio Activado, jugar contra IA,  Suerte =)", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else{
            isVsComputer = false;
            Snackbar.make(view, "Modo Aleatorio Desactivado, jugar con un amigo", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public void aprendaJuego(View view) {
        Snackbar.make(view, "Facil!, Derrotando a tu oponente, ...deja ya de pensar y a jugar", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
