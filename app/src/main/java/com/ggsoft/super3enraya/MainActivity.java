package com.ggsoft.super3enraya;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ggsoft.super3enraya.exception.JugadaIncorrectaException;
import com.ggsoft.super3enraya.model.JugadorIA;
import com.ggsoft.super3enraya.model.MasterTresEnRaya;
import com.ggsoft.super3enraya.pojo.Jugada;
import com.ggsoft.super3enraya.util.MarcadorPantalla;

public class MainActivity extends AppCompatActivity {
    private boolean gameOver = false;
    private int boludoCount=0;
    private boolean isVsComputer = false;

    //Se añade un delay para quitar la ventana del titulo
    private ImageView cartel;

    private MasterTresEnRaya master;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cartel = findViewById(R.id.imgFinal);

        //Crear nuevo Juego y limpiar pantalla
        master = new MasterTresEnRaya(9);
        //Mostrar nombre del Juego
        MarcadorPantalla.mostrarFinal(MarcadorPantalla.FINAL_INIT,this);
        MarcadorPantalla.dibujarCeldasPermitidas(master.getCeldasPermitidas(),master.getCuadroMayor(),this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    cartel.setVisibility(View.GONE);
            }
        }, 4000); // Millisecond 1000 = 1 sec

        //Añadiendo info al boton flotante
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Realizado por Gustavo Gonzalez - para el curso de DiplomadosOnLine.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //mantener la ventana activa
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void salvarJuegoActual(){


    }

    private void cargarJuegoSalvado(){

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
                evaluarJugada(master.realizarJugada(new Jugada(celda, casilla)),false);
                redibujarGrilla(master, signoActual);
                if (isVsComputer) {

                    //Se añade un delay para que la jugada sea apreciable por el jugador
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //realizar jugada de la pc
                            String signoActual = master.getProximoSigno();
                            try {
                                Jugada jugada = JugadorIA.mejorJugadaPara(signoActual,MasterTresEnRaya.copyMaster(master),2,0);
                                evaluarJugada(master.realizarJugada(jugada),true);
                               // evaluarJugada(master.realizarJugadaAleatoria(),true);
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

    private void evaluarJugada(boolean ganador, boolean isAI) {
        if (ganador) {
            //Fin de Juego: Mostrar Ganador al signoActual
            gameOver = true;
            if(isAI){
                MarcadorPantalla.mostrarFinal(MarcadorPantalla.FINAL_PERDISTE,this);
            }else {
                MarcadorPantalla.mostrarFinal(MarcadorPantalla.FINAL_GANASTE,this);
            }
        } else if (master.juegoCompletado()) {
            MarcadorPantalla.mostrarFinal(MarcadorPantalla.FINAL_EMPATE, this);
            gameOver = true;
        }
    }


    private void redibujarGrilla(MasterTresEnRaya master, String signoActual) {
        ImageButton button =  this.findViewById(this.getResources()
                .getIdentifier("imageButton-" + master.getUltimaCeldaJugada() + "-"
                        + master.getUltimaCasillaJugada(), "id", this.getPackageName()));

        MarcadorPantalla.cambiarJugador(signoActual,this);
        MarcadorPantalla.checkCasilla(button, signoActual);
        MarcadorPantalla.dibujarCeldasPermitidas(master.getCeldasPermitidas(),master.getCuadroMayor(),this);
    }

    private void marcarLineaGanadora() {
        //buscar en el maestro cuales son las celdas ganadoras y cambiar el tinte de las mismas
    }

    private int obtenerPosition(String name, int pos) {
        String[] text = name.split("-");
        return Integer.parseInt(text[pos]);
    }

    public void reiniciarJuego(View view) {
        gameOver = false;
        master = new MasterTresEnRaya(9);
        MarcadorPantalla.reiniciarCeldas(this);
        MarcadorPantalla.dibujarCeldasPermitidas(master.getCeldasPermitidas(),master.getCuadroMayor(),this);
        MarcadorPantalla.mostrarJugador(master.getProximoSigno(),this);
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

    public void info(View view) {
        Snackbar.make(view, "Realizado por Gustavo Gonzalez - para el curso de diplomadosonline.com", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void ocultar(View view){
        view.setVisibility(View.GONE);
    }

}
