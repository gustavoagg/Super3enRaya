package com.ggsoft.super3enraya.util;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ggsoft.super3enraya.R;
import com.ggsoft.super3enraya.model.MasterTresEnRaya;
import com.ggsoft.super3enraya.model.TresEnRaya;

public class MarcadorPantalla {
    public static final int FINAL_PERDISTE = 1;
    public static final int FINAL_GANASTE = 2;
    public static final int FINAL_EMPATE = 3;
    public static final int FINAL_INIT = 0;

    public static void checkCasilla(ImageButton view, String signoActual) {
        if(signoActual.equals(MasterTresEnRaya.X_SIGN)) {
            view.setImageResource(R.drawable.sign_x);
        }else{
            view.setImageResource(R.drawable.sign_o);

        }
    }

    public static void reiniciarCeldas(AppCompatActivity main){
        for(int i=1;i<10;i++){
            //Para cada celda volver al color original
            LinearLayout layout = main.findViewById(main.getResources().getIdentifier("celda_"+i, "id", main.getPackageName()));
            layout.setBackgroundColor(main.getResources().getColor(R.color.celda_back));
            //Para cada LinearLayout de 3 casillas  se hace nuevamente visible
            for(int j=1;j<4;j++) {
                layout = main.findViewById(main.getResources().getIdentifier("celda_" + i + "_"+j, "id", main.getPackageName()));
                layout.setVisibility(View.VISIBLE);
            }

            //Para cada casilla dentro de la celda i limpiar el signo
            for(int j=1;j<10;j++) {
                ImageButton button = main.findViewById(main.getResources().getIdentifier("imageButton-"+i+"-"+j,"id", main.getPackageName()));
                button.setImageResource(R.drawable.no_sign);
            }
        }

    }
    public static void dibujarCeldasPermitidas(int[] celdasPermitidas,TresEnRaya tresEnRaya, AppCompatActivity main) {

        //iterar a traves de las celdas permitidas para cambiar el color de las mismas de
        //acuerdo a su valor
        for(int i=0;i<9;i++){

            switch (celdasPermitidas[i]) {
                case 0:
                    desactivarCelda(i + 1, main);
                    break;
                case 1:
                    activarCelda(i + 1, main);
                    break;
                case 2:
                    dibujarCeldaMayor(i + 1, tresEnRaya.getPosicion(i + 1), main);
                    //celdaGanadora(i+1,main);
                    break;
            }
        }

    }

    private static void activarCelda(int i, AppCompatActivity main) {
        LinearLayout layout = main.findViewById(main.getResources().getIdentifier("celda_"+i, "id", main.getPackageName()));
                layout.setBackgroundColor(main.getResources().getColor(R.color.celda_activada));
    }

    private static void desactivarCelda(int i, AppCompatActivity main) {
        LinearLayout layout = main.findViewById(main.getResources().getIdentifier("celda_"+i, "id", main.getPackageName()));
        layout.setBackgroundColor(main.getResources().getColor(R.color.celda_desactivada));
    }


    private static void dibujarCeldaMayor(int i, String posicion, AppCompatActivity main) {
        for(int j=1;j<4;j++) {
            LinearLayout layout = main.findViewById(main.getResources().getIdentifier("celda_" + i + "_"+j, "id", main.getPackageName()));
            layout.setVisibility(View.INVISIBLE);
        }
        LinearLayout layout = main.findViewById(main.getResources().getIdentifier("celda_"+i, "id", main.getPackageName()));
        if(posicion.equalsIgnoreCase(MasterTresEnRaya.O_SIGN)){
            layout.setBackground(main.getResources().getDrawable(R.drawable.big_white_o));
        }else if(posicion.equalsIgnoreCase(MasterTresEnRaya.X_SIGN)){
            layout.setBackground(main.getResources().getDrawable(R.drawable.big_white_x));
        }
    }

    public static void mostrarJugador(String sign, AppCompatActivity main) {
        ImageView jugador = main.findViewById(R.id.quienJuega);
        if (sign.equalsIgnoreCase(MasterTresEnRaya.O_SIGN)){
            jugador.setImageResource(R.drawable.sign_o);
        }else{
            jugador.setImageResource(R.drawable.sign_x);
        }
    }
    public static void cambiarJugador(String sign, AppCompatActivity main) {
        if (sign.equalsIgnoreCase(MasterTresEnRaya.O_SIGN)){
            mostrarJugador(MasterTresEnRaya.X_SIGN,main);
        }else{
            mostrarJugador(MasterTresEnRaya.O_SIGN,main);
        }
    }


    public static void mostrarFinal(int finalPerdiste, AppCompatActivity main) {
        ImageView cartel = main.findViewById(R.id.imgFinal);
        switch (finalPerdiste){
            case 0: //Bienvenida
               cartel.setImageResource(R.drawable.splash_tateti);
               cartel.setBackgroundColor(main.getResources().getColor(R.color.ultra_light_gray));

                break;
            case 1: //Perdiste
                cartel.setImageResource(R.drawable.perdiste);
                cartel.setBackgroundColor(0);
                break;

            case 2: // Ganaste
                cartel.setImageResource(R.drawable.ganaste);
                cartel.setBackgroundColor(0);
                break;

            case 3: //Empate
                cartel.setImageResource(R.drawable.empate);
                cartel.setBackgroundColor(0);
                break;
        }
        cartel.setVisibility(View.VISIBLE);
    }
}
