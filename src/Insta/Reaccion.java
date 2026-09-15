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
public class Reaccion implements Serializable{
    private static final long serialVersionUID=1L;
    private String autor;
    private LocalDateTime fecha;

    public Reaccion(String autor, LocalDateTime fecha) {
        this.autor = autor;
        this.fecha = fecha;
    }
    
    public boolean equals(Object obj){
        if(this==obj){
            return true;
        }
        if(obj==null||!(obj instanceof Reaccion)){
            return false;
        }
        
        Reaccion r=(Reaccion) obj;
        return autor.equals(r.autor);
    }
    
    public String getAutor(){
        return autor;
    }
    
    public LocalDateTime getFecha(){
        return fecha;
    }
    
    
}
