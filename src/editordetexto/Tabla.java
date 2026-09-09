/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editordetexto;

/**
 *
 * @author gabri
 */
public class Tabla {
    private int filas;
    private int columnas;
    private Celda[][] celdas;

    public Tabla(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.celdas = new Celda[filas][columnas];
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                celdas[f][c] = new Celda();
            }
        }
    }

    public int getFilas() {
        return filas; 
    }
    public int getColumnas() {
        return columnas;
    }

    public Celda getCelda(int fila, int columna){
        return celdas[fila][columna]; 
    }
    public void setCelda(int fila, int columna, Celda celda){
        celdas[fila][columna] = celda;
    }
}
