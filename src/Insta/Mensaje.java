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
    private static final long serialVersionUID = 1L;
    private String autor;
    private String destinatario;
    private String contenido;
    private boolean esSticker;
    private LocalDateTime fechaHora;

    public Mensaje(String autor, String destinatario, String contenido, boolean esSticker) {
        this.autor = autor;
        this.destinatario = destinatario;
        this.contenido = contenido;
        this.esSticker = esSticker;
        this.fechaHora = LocalDateTime.now();
    }
    public Mensaje(String emisor, String receptor, String contenido) {
        this(emisor, receptor, contenido, false);
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
    
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public boolean isEsSticker() {
        return esSticker;
    }

    public void setEsSticker(boolean esSticker) {
        this.esSticker = esSticker;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    
    public String getHoraFormato(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return fechaHora.format(formatter);
    }
         
    
}
