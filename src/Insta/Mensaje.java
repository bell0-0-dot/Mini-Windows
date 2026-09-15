/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author vasqu
 */
public class Mensaje implements Serializable{
    private String autor;
    private String destinatario;
    private String contenido;
    private LocalDateTime fechaHora;

    public Mensaje(String autor, String destinatario, String contenido) {
        this.autor = autor;
        this.destinatario = destinatario;
        this.contenido = contenido;
        this.fechaHora = LocalDateTime.now();
    }

    public String getAutor() {
        return autor;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getContenido() {
        return contenido;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    
    public String getHoraFormato(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return fechaHora.format(formatter);
    }
         
    
}
