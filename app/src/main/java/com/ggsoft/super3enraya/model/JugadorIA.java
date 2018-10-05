package com.ggsoft.super3enraya.model;

import com.ggsoft.super3enraya.exception.JugadaIncorrectaException;
import com.ggsoft.super3enraya.pojo.Jugada;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ggsoft.super3enraya.model.MasterTresEnRaya.*;

public class JugadorIA {

    public static Jugada mejorJugadaPara(String signo, MasterTresEnRaya master, int nivel, int jugadas) {
        Jugada mejorJugada = null;
        int[] listaCeldas = master.getCeldasPermitidas();
        jugadas++;
        nivel--;
        // buscar todas las jugadas posibles
        List<Jugada> jugadasPermitidas = getJugadasPermitidas(master, listaCeldas);
        if(jugadasPermitidas.size()==1) {
            Jugada jugadaFinal = jugadasPermitidas.get(0);
            try{
                master.realizarJugada(jugadaFinal);
            }catch (JugadaIncorrectaException e){
                //do nothing
            }
            // evaluar master de cada jugada
            double eval = master.ponderarPartidaPara(signo);
            jugadaFinal.setJugadas(jugadas);
            jugadaFinal.setPeso(eval);
            return jugadaFinal;
        }else if(nivel== 0) {

            //escojo una al azar de las permitidas //FIXME solo toma la primera
            Jugada jugadaFinal = jugadasPermitidas.get(0);
            try{
            master.realizarJugada(jugadaFinal);
            }catch (JugadaIncorrectaException e){
                //do nothing
            }
            // evaluar master de cada jugada
            double eval = master.ponderarPartidaPara(signo);
            jugadaFinal.setJugadas(jugadas);
            jugadaFinal.setPeso(eval);
            return jugadaFinal;
        }else{
            boolean juegoCompleto = false;
            for (int i = 0 ; i <jugadasPermitidas.size();i++) {
                MasterTresEnRaya masterTemp = MasterTresEnRaya.copyMaster(master);
                try{
                    masterTemp.realizarJugada(jugadasPermitidas.get(i));
                }catch (JugadaIncorrectaException e){
                    juegoCompleto = true;
                }
                // evaluar master de cada jugada
                double eval = masterTemp.ponderarPartidaPara(signo);

                //condicion de parada
                if(juegoCompleto||masterTemp.juegoCompletado()){
                    // evaluar master de cada jugada
                    jugadasPermitidas.get(i).setJugadas(jugadas);
                    jugadasPermitidas.get(i).setPeso(eval);
                }else {

                    // buscar mejor jugada del oponente
                    //Jugada jugadaContrario = mejorJugadaPara(opuesto(signo), MasterTresEnRaya.copyMaster(masterTemp), nivel,0);
                    try {
                        masterTemp.realizarJugadaAleatoria();
                    }catch (JugadaIncorrectaException e){
                        juegoCompleto = true;
                    }
                   // masterTemp.realizarJugada(jugadaContrario,false);
                    eval = eval - masterTemp.ponderarPartidaPara(opuesto(signo));
                    if(juegoCompleto||masterTemp.juegoCompletado()) {
                        jugadasPermitidas.get(i).setJugadas(jugadas);
                        jugadasPermitidas.get(i).setPeso(eval);
                    }else{
                        List<Jugada> jugadasPermitidasInner = getJugadasPermitidas(masterTemp, masterTemp.getCeldasPermitidas());
                        Jugada mejorJugadaInner = mejorJugadaPara(signo,MasterTresEnRaya.copyMaster(masterTemp),nivel,jugadas);
                        try {
                        masterTemp.realizarJugada(mejorJugadaInner);
                        }catch (JugadaIncorrectaException e){
                            juegoCompleto = true;
                        }
                        jugadasPermitidas.get(i).setJugadas(mejorJugadaInner.getJugadas());
                        jugadasPermitidas.get(i).setPeso(eval+masterTemp.ponderarPartidaPara(signo));

                    }
                }
            }


            //Luego de tener todos los pesos posibles de cada jugada
            //seleccionamos la mejor de ellas
            if(jugadas==1) {
                Collections.sort(jugadasPermitidas);
            }else{
                Collections.sort(jugadasPermitidas);
            }
            return jugadasPermitidas.get(0);

        }

    }

    private static List<Jugada> getJugadasPermitidas(MasterTresEnRaya masterTemp, int[] listaCeldas) {
        List<Jugada> jugadasPermitidas = new ArrayList<Jugada>();
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

    public static String opuesto(String signo){
        if(signo.equals(X_SIGN)){
            return O_SIGN;
        }else{
            return X_SIGN;
        }
    }
}
