package base;

import Excepciones.ArchivoCorruptoException;
import java.io.*;

/**
 *
 * @author gabri
 */
public class ArchivoUtil {
    
                    //Gabriel: esto es para que no podamos pasar elementos no serializables
    public static <T extends Serializable> void guardarLista(String rutaArchivo, ListaEnlazada<T> lista) throws IOException{
        
        File archivo = new File(rutaArchivo);
        File carpeta = archivo.getParentFile();
        
        if(carpeta != null && !carpeta.exists()){
            carpeta.mkdirs();
        }
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            System.out.println("Error escribiendo al archivo: '" + rutaArchivo + "' - " + e.getMessage());
            throw e;
        }
    }
    
    public static <T> ListaEnlazada<T> leerLista(String rutaArchivo) throws ArchivoCorruptoException{
        
        File archivo = new File(rutaArchivo);
        if(!archivo.exists()){
            //Gabriel: Aqui regresa una lista vacia, se puede usar estaVacia() para revisar
            return new ListaEnlazada<>();
        }
        
        try (ObjectInputStream lectura = new ObjectInputStream(new FileInputStream(archivo))) {
            return (ListaEnlazada<T>) lectura.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new ArchivoCorruptoException(rutaArchivo);
        }    
    }
    
    public static <T extends Serializable> void agregarRegistro(String rutaArchivo, T nuevoRegistro) throws ArchivoCorruptoException, IOException{
        
        ListaEnlazada<T> lista = leerLista(rutaArchivo);
        lista.insertarFinal(nuevoRegistro);
        guardarLista(rutaArchivo,lista);        
        
    }
}
