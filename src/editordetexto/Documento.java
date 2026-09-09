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
public class Documento {
     private ArrayList<Object> bloques = new ArrayList<>();

    public ArrayList<Object> getBloques() { return bloques; }

    public void addBloque(Object bloque) {
        if (!(bloque instanceof Fragmento) && !(bloque instanceof Tabla)) {
            throw new IllegalArgumentException("El bloque debe ser Fragmento o Tabla");
        }
        bloques.add(bloque);
    }
}
