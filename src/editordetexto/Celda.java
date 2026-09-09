/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editordetexto;

import java.util.ArrayList;

/**
 *
 * @author gabri
 */
public class Celda {
    private ArrayList<Fragmento> contenido = new ArrayList<>();

    public ArrayList<Fragmento> getContenido() {
        return contenido; 
    }
    public void setContenido(ArrayList<Fragmento> contenido) {
        this.contenido = contenido;
    }
    public void addFragmento(Fragmento f){
        this.contenido.add(f);
    }
}
