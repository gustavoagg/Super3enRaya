package com.ggsoft.super3enraya.pojo;

import android.support.annotation.NonNull;

import java.util.Comparator;

public class Jugada implements Comparable<Jugada>{
    private int celda;
    private int casilla;
    private int jugadas; //al estimar: numero de jugadas para mejor solucion
    private double peso; //para los valores al evaluar

    public Jugada(int celda, int casilla){
        this.celda = celda;
        this.casilla = casilla;
    }

    public Jugada(int celda, int casilla, int jugadas, int peso) {
        this.celda = celda;
        this.casilla = casilla;
        this.jugadas = jugadas;
        this.peso = peso;
    }

    public int getCelda() {
        return celda;
    }

    public void setCelda(int celda) {
        this.celda = celda;
    }

    public int getCasilla() {
        return casilla;
    }

    public void setCasilla(int casilla) {
        this.casilla = casilla;
    }

    public int getJugadas() {
        return jugadas;
    }

    public void setJugadas(int jugadas) {
        this.jugadas = jugadas;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }


    @Override
    public int compareTo(@NonNull Jugada o) {
        int c;
        c = new Integer(o.jugadas).compareTo(new Integer(this.jugadas));
        if (c == 0)
            c = new Double(o.peso).compareTo(new Double(this.peso));
        if (c == 0)
            c = new Integer(o.celda*o.casilla).compareTo(new Integer(this.celda*this.casilla));
        return c;
    }
}
