/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

import java.io.Serializable;
import java.time.LocalDateTime;
import base.ListaEnlazada;
/**
 *
 * @author vasqu
 */
public class Publicacion implements Serializable{
    private static final long serialVersionUID=1L;
    
    private String autor;
    private String contenido;
    private LocalDateTime fecha;
    private ListaEnlazada<String>menciones;
    private ListaEnlazada<String>hashtags;
    private String rutaImagen;
    private String rutaSticker;
    private ListaEnlazada<Reaccion>reacciones;
    private ListaEnlazada<Comentario>comentarios;

    public Publicacion(String autor, String contenido, LocalDateTime fecha, ListaEnlazada<String> menciones, ListaEnlazada<String> hashtags, String rutaImagen, String rutaSticker) {
        this.autor = autor;
        this.contenido = contenido;
        this.fecha = fecha;
        this.menciones = menciones;
        this.hashtags = hashtags;
        this.rutaImagen = rutaImagen;
        this.rutaSticker = rutaSticker;
        this.reacciones=new ListaEnlazada<>();
        this.comentarios=new ListaEnlazada<>();
    }
    
    public void agregarReaccion(Reaccion r){
        reacciones.insertarFinal(r);
    }
    
    public void agregarComentario(Comentario c){
        comentarios.insertarFinal(c);
    }
    
   
    public String getAutor() {
        return autor;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }


    public ListaEnlazada<String> getMenciones() {
        return menciones;
    }


    public ListaEnlazada<String> getHashtags() {
        return hashtags;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getRutaSticker() {
        return rutaSticker;
    }

    public void setRutaSticker(String rutaSticker) {
        this.rutaSticker = rutaSticker;
    }

    public ListaEnlazada<Reaccion> getReacciones() {
        return reacciones;
    }


    public ListaEnlazada<Comentario> getComentarios() {
        return comentarios;
    }
    
    
    
    
    
    
    
}
