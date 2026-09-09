/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editordetexto;

import java.awt.Color;

/**
 *
 * @author vasqu
 */
public class FormatoTexto {
    private Color color;
    private boolean negrita;
    private boolean cursiva;
    private boolean subrayado;
    private boolean tachado;
    private String fuente;
    private int size;

    public FormatoTexto() {
        this.color=Color.BLACK;
        this.negrita=false;
        this.cursiva=false;
        this.subrayado = false;
        this.tachado = false;
        this.fuente = "Arial";
        this.size = 12;
        
    }

    public FormatoTexto(Color color, boolean negrita, boolean cursiva, boolean subrayado, boolean tachado, String fuente, int size) {
        this.color = color;
        this.negrita = negrita;
        this.cursiva = cursiva;
        this.subrayado = subrayado;
        this.tachado = tachado;
        this.fuente = fuente;
        this.size = size;
    }
    
      @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FormatoTexto)) return false;
        FormatoTexto otro = (FormatoTexto) obj;
        return negrita == otro.negrita &&
               cursiva == otro.cursiva &&
               subrayado == otro.subrayado &&
               tachado == otro.tachado &&
               size == otro.size && color.equals(otro.color) && fuente.equals(otro.fuente);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isNegrita() {
        return negrita;
    }

    public void setNegrita(boolean negrita) {
        this.negrita = negrita;
    }

    public boolean isCursiva() {
        return cursiva;
    }

    public void setCursiva(boolean cursiva) {
        this.cursiva = cursiva;
    }

    public boolean isSubrayado() {
        return subrayado;
    }

    public void setSubrayado(boolean subrayado) {
        this.subrayado = subrayado;
    }

    public boolean isTachado() {
        return tachado;
    }

    public void setTachado(boolean tachado) {
        this.tachado = tachado;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public int getsize() {
        return size;
    }

    public void setsize(int size) {
        this.size = size;
    }
    
    
}
