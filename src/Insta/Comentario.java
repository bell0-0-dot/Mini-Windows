/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author vasqu
 */
public class Comentario implements Serializable{
    private static final long serialVersionUID=1L;
    
    private String autor;
    private String texto;
    private LocalDateTime fecha;

    public Comentario(String autor, String texto, LocalDateTime fecha) {
        this.autor = autor;
        this.texto = texto;
        this.fecha = fecha;
    }
    
     public String getAutor() {
        return autor;
    }

    public String getTexto() {
        return texto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
