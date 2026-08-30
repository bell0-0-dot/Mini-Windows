package Excepciones;

/**
 *
 * @author gabri
 */
public class ArchivoCorruptoException extends Exception{
    public ArchivoCorruptoException(String rutaArchivo){
        super("El archivo '"+rutaArchivo+"' no se pudo leer, puede estar corrupto.");
    }
}
