/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package editordetexto.excepciones;

/**
 *
 * @author gabri
 */
public class ArchivoCorruptoException extends Exception{
    public ArchivoCorruptoException(String mensaje){
        super(mensaje); 
    }
}
