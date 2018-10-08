package com.ggsoft.super3enraya.model;

import com.ggsoft.super3enraya.exception.JugadaIncorrectaException;
import com.ggsoft.super3enraya.pojo.Jugada;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.ggsoft.super3enraya.model.MasterTresEnRaya.*;

public class JugadorIA {

    public static Jugada mejorJugadaPara(String signo, MasterTresEnRaya master, int nivel, int jugadas) {
        Jugada mejorJugada;
        int[] listaCeldas = master.getCeldasPermitidas();
        jugadas++;
        nivel--;
        // buscar todas las jugadas posibles
        List<Jugada> jugadasPermitidas = getJugadasPermitidas(master, listaCeldas);
        if(jugadasPermitidas.size()==1) {
            Jugada jugadaFinal = jugadasPermitidas.get(0);
            calcularPeso(signo, master, jugadas, jugadaFinal,false);
            mejorJugada = jugadaFinal;
        }else if(nivel== 0) {
            //evaluamos en busca de la mejor
            for (int i = 0 ; i <jugadasPermitidas.size();i++) {
                Jugada jugadaTemp = jugadasPermitidas.get(i);
                MasterTresEnRaya masterTemp = MasterTresEnRaya.copyMaster(master);
                calcularPeso(signo, masterTemp, jugadas, jugadaTemp, false);
            }
            //devolvemos una al azar (version previa - puede servir para los niveles)
            //Jugada jugadaFinal = jugadasPermitidas.get(new Random().nextInt(jugadasPermitidas.size()));

            Collections.sort(jugadasPermitidas);
            mejorJugada = jugadasPermitidas.get(0);
        }else{
            boolean juegoCompleto = false;

            for (int i = 0 ; i <jugadasPermitidas.size();i++) {
                Jugada jugadaTemp = jugadasPermitidas.get(i);
                MasterTresEnRaya masterTemp = MasterTresEnRaya.copyMaster(master);
                juegoCompleto = calcularPeso(signo, masterTemp, jugadas,  jugadaTemp,false);

                //condicion de parada
                if(!juegoCompleto && !masterTemp.juegoCompletado()){

                    // buscar mejor jugada del oponente
                    MasterTresEnRaya masterTempOponente = MasterTresEnRaya.copyMaster(masterTemp);
                    Jugada jugadaContrario = mejorJugadaPara(opuesto(signo), masterTempOponente, 1,0);

                    juegoCompleto = calcularPeso(signo, masterTemp, jugadas,  jugadaContrario,true);

                    if(!juegoCompleto && !masterTempOponente.juegoCompletado()) {
                        List<Jugada> jugadasPermitidasInner = getJugadasPermitidas(masterTemp, masterTemp.getCeldasPermitidas());
                        Jugada mejorJugadaInner = mejorJugadaPara(signo,MasterTresEnRaya.copyMaster(masterTemp),nivel,jugadas);
                        calcularPeso(signo, masterTemp, jugadas,  jugadaTemp,true);
                    }

                }
            }
            Collections.sort(jugadasPermitidas);
            mejorJugada = jugadasPermitidas.get(0);
            //Luego de tener todos los pesos posibles de cada jugada
            //seleccionamos la mejor de ellas

        }
        return mejorJugada;

    }

    private static boolean calcularPeso(String signo, MasterTresEnRaya master, int jugadas, Jugada jugadaFinal,boolean oponente) {
        double eval = 0;
        boolean juegoCompleto = false;
        try{
            master.realizarJugada(jugadaFinal);
        }catch (JugadaIncorrectaException e){
            juegoCompleto = true;
            if(oponente)
                eval = jugadaFinal.getPeso() - 1000;
            else
                eval = jugadaFinal.getPeso() + 1000;
        }
        // evaluar master de cada jugada
        eval = eval +  master.ponderarPartidaPara(signo);
        jugadaFinal.setJugadas(jugadas);
        jugadaFinal.setPeso(eval);
        return juegoCompleto;
    }

    private static List<Jugada> getJugadasPermitidas(MasterTresEnRaya masterTemp, int[] listaCeldas) {
        List<Jugada> jugadasPermitidas = new ArrayList<>();
        int celda = 0, casilla= 0;


        for (int celdaPermitida:listaCeldas) {
            celda++;
            if(celdaPermitida == 1 ) {//permitido
              TresEnRaya juego = masterTemp.getCuadro(celda);
                int[] listaCasillas = juego.getCasillasPermitidas();
                for(int casillaPermitida: listaCasillas){
                    casilla++;
                    if(casillaPermitida == 1){
                        Jugada jugadaTemp = new Jugada(celda,casilla);
                        jugadasPermitidas.add(jugadaTemp);
                    }
                }
                casilla=0;
            }
        }
        return jugadasPermitidas;
    }

    private static String opuesto(String signo){
        if(signo.equals(X_SIGN)){
            return O_SIGN;
        }else{
            return X_SIGN;
        }
    }
}
