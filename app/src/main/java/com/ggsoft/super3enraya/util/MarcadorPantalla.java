package com.ggsoft.super3enraya.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.ggsoft.super3enraya.MainActivity;
import com.ggsoft.super3enraya.R;
import com.ggsoft.super3enraya.model.MasterTresEnRaya;

public class MarcadorPantalla {

    public static void checkCasilla(ImageButton view, String signoActual) {
        if(signoActual.equals(MasterTresEnRaya.X_SIGN)) {
            view.setImageResource(R.drawable.sign_x);
        }else{
            view.setImageResource(R.drawable.sign_o);

        }
    }

    public static void mostrarTodasCeldas(){

    }

    public static void reiniciarCeldas(AppCompatActivity main){
        for(int i=0;i<9;i++){
            LinearLayout layout = (LinearLayout) main.findViewById(main.getResources().getIdentifier("celda_"+i, "id", main.getPackageName()));
            layout.setBackgroundColor(main.getResources().getColor(R.color.celda_back));
        }

    }
    public static void dibujarCeldasPermitidas(int[] celdasPermitidas, AppCompatActivity main) {

        //iterar a traves de las celdas permitidas para cambiar el color de las mismas de
        //acuerdo a su valor
        for(int i=0;i<9;i++){

            if(celdasPermitidas[i]==0){
                desactivarCelda(i+1,main);
            }else if(celdasPermitidas[i]==1){
                activarCelda(i+1,main);
            }else if(celdasPermitidas[i]==2){
                celdaGanadora(i+1,main);
            }
        }

    }

    private static void activarCelda(int i, AppCompatActivity main) {
        LinearLayout layout = (LinearLayout) main.findViewById(main.getResources().getIdentifier("celda_"+i, "id", main.getPackageName()));
                layout.setBackgroundColor(main.getResources().getColor(R.color.celda_activada));
    }

    private static void desactivarCelda(int i, AppCompatActivity main) {
        LinearLayout layout = (LinearLayout) main.findViewById(main.getResources().getIdentifier("celda_"+i, "id", main.getPackageName()));
        layout.setBackgroundColor(main.getResources().getColor(R.color.celda_desactivada));
    }

    private static void celdaGanadora(int i, AppCompatActivity main) {
        LinearLayout layout = (LinearLayout) main.findViewById(main.getResources().getIdentifier("celda_"+i, "id", main.getPackageName()));
        layout.setBackgroundColor(main.getResources().getColor(R.color.celda_ganadora));
    }
}
