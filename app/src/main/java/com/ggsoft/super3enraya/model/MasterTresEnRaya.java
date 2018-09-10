package com.ggsoft.super3enraya.model;

import com.ggsoft.super3enraya.exception.JugadaIncorrectaException;

import java.util.List;

public class MasterTresEnRaya {
    public static final String X_SIGN = "x";
    public static final String O_SIGN = "o";
    TresEnRaya[] lista3EnRaya;
    String proximoSigno;
    int[] celdasPermitidas; //cero no permitido, uno permitido
    int[] celdasGanadas; //cero ganada, uno no ganada


    public MasterTresEnRaya(int nroTresEnRaya){
        this.lista3EnRaya = new TresEnRaya[nroTresEnRaya+1];
        this.proximoSigno = O_SIGN;
        //Se almacenan 10 posiciones, ya que se toma la posicion 0 como el principal
        for (int i = 0; i<nroTresEnRaya+1 ; i++){
            this.lista3EnRaya[i] = new TresEnRaya();
        }
        this.celdasPermitidas = new int[]{1,1,1,1,1,1,1,1,1};
        this.celdasGanadas = new int[]{1,1,1,1,1,1,1,1,1};
    }

    // La celda indica el numero de TresEnRaya seleccionado (1-9) y la casilla es la posicion dentro de esa celda (1-9)
    public boolean realizarJugada(int celda, int casilla) throws JugadaIncorrectaException {

            if(this.lista3EnRaya[celda].marcarCasilla(casilla,this.proximoSigno)){
                //hubo ganador de celda
                // Se oculta la celda y se marca con el ganador en pantalla y en el principal

                // Se valida si hubo ganador en el maestro
                if(this.lista3EnRaya[0].marcarCasilla(celda, this.proximoSigno)){
                    //hubo ganador de Partida
                    marcarCeldaGanadora(celda);
                    return true;
                }
            }
            //valido en que celda se permite la proxima jugada
            validarCeldasPermitidas(casilla);
        //continuar partida
        cambiarSigno();
        return false;
    }

    private void validarCeldasPermitidas(int casilla) {
        if(this.celdasGanadas[casilla-1]==0){
            //en caso de que la celda especificada este ganada se podra jugar libremente en cualquier celda no ganada
            this.celdasPermitidas = this.celdasGanadas;
        }else {
            //solo se puede jugar en la celda con numero igual a la casilla
            this.celdasPermitidas = new int[]{0,0,0,0,0,0,0,0,0};
            this.celdasPermitidas[casilla-1]=1;
        }
    }

    private void marcarCeldaGanadora(int celda) {
        this.celdasGanadas[celda-1]=0;
    }

    public int[] getCeldasPermitidas(){
        return celdasPermitidas;
    }

    public String getProximoSigno(){
        return this.proximoSigno;
    }


    private void cambiarSigno() {
        if(this.proximoSigno.equals(O_SIGN)){
            this.proximoSigno = X_SIGN;
        }else{
            this.proximoSigno = O_SIGN;
        }
    }


}
