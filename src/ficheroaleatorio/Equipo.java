/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ficheroaleatorio;

import java.util.ArrayList;

/**
 *
 * @author MXIII
 */
public class Equipo {
    
    private ArrayList<Jugador> equipo;
    
    public Equipo(){
        this.equipo = new ArrayList<>();
    }
    
    /**
     * Metodo añadir
     * Este metodo sirve para añadir un jugador a un equipo
     * @param jugador El jugador a añadir
     */
    public void añadir(Jugador jugador){
        this.equipo.add(jugador);
    }
    
    /**
     * Metodo eliminar
     * Este metodo sirve para eliminar un jugador de un equipo
     * @param jugador El jugador a ser eliminado
     */
    public void eliminar(Jugador jugador){
        this.equipo.remove(jugador);
    }
}
