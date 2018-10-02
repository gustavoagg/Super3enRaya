package com.ggsoft.super3enraya.model;

import com.ggsoft.super3enraya.exception.JugadaIncorrectaException;
import com.ggsoft.super3enraya.pojo.Jugada;

public class MasterTresEnRaya {
    public static final String X_SIGN = "x";
    public static final String O_SIGN = "o";

    private static final double FACTOR = 1.5;

    private TresEnRaya[] lista3EnRaya;
    private String proximoSigno;
    private int[] celdasPermitidas; //cero no permitido, uno permitido, dos ganado
    private int[] celdasGanadas; //cero ganada, uno no ganada
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

    //pondera dando un peso luego de una jugada especifica, asi el IA puede compara opciones
    public double ponderarPartidaPara(String sign){
        double result = 0;
        for (TresEnRaya tresEnRaya:lista3EnRaya) {
            result = result + (tresEnRaya.evaluarChancesPara(sign) * FACTOR);
        }
        //Se agrega triple peso sobre el principal, una del ciclo previo
        result = result + getCuadroMayor().evaluarChancesPara(sign);
        return result;
    }

    public boolean realizarJugadaAleatoria() throws JugadaIncorrectaException {
        // la idea de este metodo es generar una jugada aletoria para poder entrenar
        int celdaAleatoria;
        int casillaAleatoria;
        int counter=0;

        // buscar una celda aleatoria disponible
        while(true){
            int random = (int)(Math.random() *9)+1;
            counter++;
            if(isCeldaPermitida(random)){
                if(lista3EnRaya[random].isNotFull()&&lista3EnRaya[random].hasGanador()) {
                    celdaAleatoria=random;
                    break;
                }
            }
            if(counter>100)
                throw new JugadaIncorrectaException("IA - No consiguio donde jugar ");
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

        return realizarJugada(new Jugada(celdaAleatoria,casillaAleatoria));
    }
    // La celda indica el numero de TresEnRaya seleccionado (1-9) y la casilla es la posicion dentro de esa celda (1-9)
    public boolean realizarJugada(Jugada jugada) throws JugadaIncorrectaException {
        this.ultimaCeldaJugada = jugada.getCelda();
        this.ultimaCasillaJugada = jugada.getCasilla();
        // se puede jugar en esa celda
            if(!isCeldaPermitida(jugada.getCelda())){
                throw new JugadaIncorrectaException("CI - Celda Invalida");
            }
            //realizar jugada para ver si genera ganador
            if(this.lista3EnRaya[jugada.getCelda()].marcarCasilla(jugada.getCasilla(),this.proximoSigno)){
                //hubo ganador de celda
                // Se oculta la celda y se marca con el ganador en pantalla y en el principal

               marcarCeldaGanadora(jugada.getCelda());
                // Se valida si hubo ganador en el maestro
                if(this.lista3EnRaya[0].marcarCasilla(jugada.getCelda(), this.proximoSigno)){
                    //hubo ganador de Partida
                    marcarCeldasPermitidas(jugada.getCasilla());
                    return true;
                }
            }
            //valido en que celda se permite la proxima jugada
            marcarCeldasPermitidas(jugada.getCasilla());
        //continuar partida
        cambiarSigno();
        return false;
    }

    private boolean isCeldaPermitida(int celda) {
        return this.celdasPermitidas[celda - 1] == 1;
    }

    private void marcarCeldasPermitidas(int casilla) {
        if(this.celdasGanadas[casilla-1]==0 || !this.getCuadro(casilla).isNotFull()){
            //en caso de que la celda especificada este ganada o este completa se podra jugar libremente en cualquier celda no ganada
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

    public TresEnRaya getCuadro(int i){ return this.lista3EnRaya[i];}

    public boolean juegoCompletado() {
        // si el juego principal tienes todas las celdas ganadas en 0 o si todas las que esten en uno, esten ya completos
        for(int i= 0;i<9 ;i++){
            if(this.celdasGanadas[i]==1&& this.lista3EnRaya[i+1].isNotFull()){
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

    public TresEnRaya[] getLista3EnRaya() {
        return lista3EnRaya;
    }

    public int[] getCeldasGanadas() {
        return celdasGanadas;
    }

    public static MasterTresEnRaya copyMaster(MasterTresEnRaya oldMaster){
        MasterTresEnRaya newMaster = new MasterTresEnRaya(9);
        for(int i = 0; i<10 ;i++){
            newMaster.lista3EnRaya[i]= TresEnRaya.copy(oldMaster.lista3EnRaya[i]);
        }
        newMaster.proximoSigno = new String(oldMaster.getProximoSigno());
        for (int i=0;i<9;i++) {
            newMaster.celdasPermitidas[i]=new Integer(oldMaster.celdasPermitidas[i]);
        }
        for (int i=0;i<9;i++) {
            newMaster.celdasGanadas[i]=new Integer(oldMaster.celdasGanadas[i]);
        }
        newMaster.ultimaCeldaJugada = new Integer(oldMaster.getUltimaCeldaJugada());
        newMaster.ultimaCasillaJugada = new Integer(oldMaster.getUltimaCasillaJugada());
        return newMaster;
    }
}
