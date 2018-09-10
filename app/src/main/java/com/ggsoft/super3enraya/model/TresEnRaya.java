package com.ggsoft.super3enraya.model;

import com.ggsoft.super3enraya.exception.JugadaIncorrectaException;

public class TresEnRaya {
    String[] casillas;
    String ganador;


    public TresEnRaya(){
        this.casillas = new String[9];
        this.ganador="";
    }

    public boolean marcarCasilla(int nroCasilla,String signo) throws JugadaIncorrectaException {
        if(!this.ganador.isEmpty()){
            throw new JugadaIncorrectaException("JT - Juego Terminado");
        }
        if(this.casillas[nroCasilla-1]==null||this.casillas[nroCasilla-1].isEmpty()) {
            this.casillas[nroCasilla - 1] = signo;
            if (huboGanador()) {
                this.ganador = signo;
                return true;
            }
        }else{
            throw new JugadaIncorrectaException("CO - Casilla Ocupada");
        }
        return false;
    }

    private boolean huboGanador() {

        if (sonIguales(1,2,3)){
            return true;
        }else if(sonIguales(4,5,6)){
            return true;
        }else if(sonIguales(7,8,9)){
            return true;
        }else if(sonIguales(3,6,9)){
            return true;
        }else if(sonIguales(2,5,8)){
            return true;
        }else if(sonIguales(1,4,7)){
            return true;
        }else if(sonIguales(1,5,9)){
            return true;
        }else if(sonIguales(3,5,7)){
            return true;
        }
        return false;
    }

    private boolean sonIguales(int i, int j, int k) {
        if(this.casillas[i-1]!=null && this.casillas[j-1]!=null && this.casillas[i-1].equals(this.casillas[j-1])){
            if(this.casillas[k-1]!=null && this.casillas[j-1].equals(this.casillas[k-1])){
                return true;
            }
        }
        return false;
    }


    public String toString(){
        return this.ganador +" - "+this.casillas.toString();
    }
}
