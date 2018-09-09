package com.ggsoft.super3enraya.model;

import com.ggsoft.super3enraya.exception.JugadaIncorrectaException;

import java.util.List;

public class MasterTresEnRaya {
    TresEnRaya[] lista3EnRaya;
    String proximoSigno;

    public MasterTresEnRaya(int nroTresEnRaya){
        this.lista3EnRaya = new TresEnRaya[nroTresEnRaya+1];
        this.proximoSigno = "o";
        //Se almacenan 10 posiciones, ya que se toma la posicion 0 como el principal
        for (int i = 0; i<10 ; i++){
            this.lista3EnRaya[i] = new TresEnRaya();
        }
    }

    // La celda indica el numero de TresEnRaya seleccionado (1-9) y la casilla es la posicion dentro de esa celda (1-9)
    public boolean realizarJugada(int celda, int casilla){
        try {
            if(this.lista3EnRaya[celda-1].marcarCasilla(casilla,this.proximoSigno)){
                //hubo ganador de celda
                // Se oculta la celda y se marca con el ganador en pantalla y en el principal

                // Se valida si hubo ganador en el maestro
                if(this.lista3EnRaya[0].marcarCasilla(celda, this.proximoSigno)){
                    //hubo ganador de Partida
                    return true;
                }
            }else{
                //s
            }
        } catch (JugadaIncorrectaException e) {
            //Mostrar mensaje de error, jugada indebida continuar partida
        }
        //continuar partida
        cambiarSigno();
        return false;
    }

    private void cambiarSigno() {
        if(this.proximoSigno.equals("o")){
            this.proximoSigno = "x";
        }else{
            this.proximoSigno = "o";
        }
    }


}
