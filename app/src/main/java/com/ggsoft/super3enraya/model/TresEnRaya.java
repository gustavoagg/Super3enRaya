package com.ggsoft.super3enraya.model;

import com.ggsoft.super3enraya.exception.JugadaIncorrectaException;

public class TresEnRaya {
    private static final int E_GANADO = 20;
    private static final int E_PERDIDO = -20;
    private static final int E_PLUS = 2;
    private String[] casillas;
    private String ganador;
    private int nroJugadas;


    TresEnRaya(){
        this.casillas = new String[9];
        this.ganador="";
        this.nroJugadas = 0;
    }

    public boolean marcarCasilla(int nroCasilla,String signo) throws JugadaIncorrectaException {

        if(!this.ganador.isEmpty()){
            throw new JugadaIncorrectaException("JT - Juego Terminado");
        }
        if(this.casillas[nroCasilla-1]==null||this.casillas[nroCasilla-1].isEmpty()) {
            this.casillas[nroCasilla - 1] = signo;
            nroJugadas++;
            if (huboGanador()) {
                this.ganador = signo;
                return true;
            }
        }else{
            throw new JugadaIncorrectaException("CO - Casilla Ocupada");
        }
        return false;
    }

    public boolean isNotFull(){
        return nroJugadas < 9 ;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public double evaluarChancesPara(String sign){
        double result = 0;
        if(this.ganador.isEmpty()){
            result = result + evaluarTrio(1,2,3,sign);
            result = result + evaluarTrio(4,5,6,sign);
            result = result + evaluarTrio(7,8,9,sign);
            result = result + evaluarTrio(3,6,9,sign);
            result = result + evaluarTrio(2,5,8,sign);
            result = result + evaluarTrio(1,4,7,sign);
            result = result + evaluarTrio(1,5,9,sign);
            result = result + evaluarTrio(3,5,7,sign);
            return result;
        }else if(this.ganador.equals(sign)){
            return E_GANADO;
        }else{
            return E_PERDIDO;
        }


    }

    private double evaluarTrio(int i, int j, int k, String sign) {
        int result =0;
        String value =( this.casillas[i - 1]==null?"-":this.casillas[i - 1] )+
                (this.casillas[j - 1]==null?"-":this.casillas[j - 1])+
                (this.casillas[k - 1]==null?"-":this.casillas[k - 1]);
        if(sign.equals(MasterTresEnRaya.X_SIGN)){
            if(value.equals("xx-")||value.equals("x-x")||value.equals("-xx")){
                return E_PLUS*2;
            }else if(value.equals("x--")||value.equals("-x-")||value.equals("--x")){
                return E_PLUS;
            }
        }else{
            if(value.equals("oo-")||value.equals("o-o")||value.equals("-oo")){
                return E_PLUS*2;
            }else if(value.equals("o--")||value.equals("-o-")||value.equals("--o")){
                return E_PLUS;
            }
        }

        return result;
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
        }else if(sonIguales(3, 5, 7)){
            return true;
        }
        return false;
    }

    private boolean sonIguales(int i, int j, int k) {
        return this.casillas[i - 1] != null && this.casillas[j - 1] != null && this.casillas[i - 1].equals(this.casillas[j - 1]) && this.casillas[k - 1] != null && this.casillas[j - 1].equals(this.casillas[k - 1]);
    }

    public String getPosicion(int i){
        return this.casillas[i-1];
    }

    public int[] getCasillasPermitidas() {
        int[] permitidas = new int[]{0,0,0,0,0,0,0,0,0};
        int posicion = 0;
        for(String casilla: this.casillas){
            if(casilla==null || casilla.isEmpty()){
                permitidas[posicion] = 1;
            }
            posicion++;
        }
        return permitidas;
    }

    public static TresEnRaya copy(TresEnRaya oldTres){
        TresEnRaya newTres = new TresEnRaya();
        for (int i=0;i<9;i++) {
            if(oldTres.casillas[i]!=null){
                newTres.casillas[i]=new String(oldTres.casillas[i]);
            }
        }
        newTres.ganador = new String(oldTres.ganador);
        newTres.nroJugadas = new Integer(oldTres.nroJugadas);

        return newTres;
    }

    public boolean hasGanador() {
        if(this.ganador==null||this.ganador.equals("")){
            return true;
        }
        return false;
    }
}
