package com.ggsoft.super3enraya.model;

import com.ggsoft.super3enraya.exception.JugadaIncorrectaException;

import java.util.Random;

public class MasterTresEnRaya {
    public static final String X_SIGN = "x";
    public static final String O_SIGN = "o";

    TresEnRaya[] lista3EnRaya;
    String proximoSigno;
    int[] celdasPermitidas; //cero no permitido, uno permitido, dos ganado
    int[] celdasGanadas; //cero ganada, uno no ganada
    private int ultimaCeldaJugada;
    private int ultimaCasillaJugada;


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

    public boolean realizarJugadaAleatoria() throws JugadaIncorrectaException {
        // la idea de este metodo es generar una jugada aletoria para poder entrenar
        int celdaAleatoria = 0;
        int casillaAleatoria = 0;

        // buscar una celda aleatoria disponible
        while(true){
            int random = (int)(Math.random() *9)+1;
            if(isCeldaPermitida(random)){
                if(!lista3EnRaya[random].isFull()) {
                    celdaAleatoria=random;
                    break;
                }
            }
        }

        // buscar una casilla aleatoria disponible dentro de la celda escogida
        while(true){
            int random = (int)(Math.random() *9)+1;
            String signo = lista3EnRaya[celdaAleatoria].getPosicion(random);
            if(signo==null || signo.isEmpty()){
                casillaAleatoria= random;
                break;
            }
        }

        return realizarJugada(celdaAleatoria,casillaAleatoria);
    }
    // La celda indica el numero de TresEnRaya seleccionado (1-9) y la casilla es la posicion dentro de esa celda (1-9)
    public boolean realizarJugada(int celda, int casilla) throws JugadaIncorrectaException {
        this.ultimaCeldaJugada = celda;
        this.ultimaCasillaJugada = casilla;
        // se puede jugar en esa celda
            if(!isCeldaPermitida(celda)){
                throw new JugadaIncorrectaException("CI - Celda Invalida");
            }
            //realizar jugada para ver si genera ganador
            if(this.lista3EnRaya[celda].marcarCasilla(casilla,this.proximoSigno)){
                //hubo ganador de celda
                // Se oculta la celda y se marca con el ganador en pantalla y en el principal
                marcarCeldaGanadora(celda);
                // Se valida si hubo ganador en el maestro
                if(this.lista3EnRaya[0].marcarCasilla(celda, this.proximoSigno)){
                    //hubo ganador de Partida
                    marcarCeldasPermitidas(casilla);
                    return true;
                }
            }
            //valido en que celda se permite la proxima jugada
            marcarCeldasPermitidas(casilla);
        //continuar partida
        cambiarSigno();
        return false;
    }

    private boolean isCeldaPermitida(int celda) {
        if(this.celdasPermitidas[celda-1]==1){
            return true;
        }else{
            return false;
        }
    }

    private void marcarCeldasPermitidas(int casilla) {
        if(this.celdasGanadas[casilla-1]==0){
            //en caso de que la celda especificada este ganada se podra jugar libremente en cualquier celda no ganada
            for (int i = 0;i<9;i++) {
                if(this.celdasGanadas[i]==0){
                    this.celdasPermitidas[i]=2;
                }else{
                    this.celdasPermitidas[i]=1;
                }
            }
        }else {
            //solo se puede jugar en la celda con numero igual a la casilla
            for (int i = 0;i<9;i++) {
                if(this.celdasGanadas[i]==0){
                    this.celdasPermitidas[i]=2;
                }else{
                    this.celdasPermitidas[i]=0;
                }
            }
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


    public TresEnRaya getCuadroMayor() {
        return this.lista3EnRaya[0];
    }

    public boolean juegoCompletado() {
        // si el juego principal tienes todas las celdas ganadas en 0 o si todas las que esten en uno, esten ya completos
        for(int i= 0;i<9 ;i++){
            if(this.celdasGanadas[i]==1&&!this.lista3EnRaya[i+1].isFull()){
                return false;
            }
        }
        return true;
    }

    public int getUltimaCeldaJugada(){
        return ultimaCeldaJugada;
    }

    public int getUltimaCasillaJugada(){
        return ultimaCasillaJugada;
    }
}
