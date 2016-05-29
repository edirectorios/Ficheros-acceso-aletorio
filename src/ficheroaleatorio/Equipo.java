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
    
    public void añadir(Jugador jugador){
        this.equipo.add(jugador);
    }
    
    public void eliminar(Jugador jugador){
        this.equipo.remove(jugador);
    }
}
