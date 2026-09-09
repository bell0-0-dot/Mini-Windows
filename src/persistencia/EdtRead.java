/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import editordetexto.*;
import editordetexto.excepciones.*;

import java.awt.Color;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author gabri
 */
public class EdtRead {
    public Documento abrir(File archivo) throws IOException, ArchivoCorruptoException,ExtensionInvalidaException, ArchivoTruncadoException {

        if (!archivo.exists()) {
            throw new ArchivoCorruptoException("El archivo no existe: " + archivo.getPath());
        }
        if (!archivo.getName().toLowerCase().endsWith(Constantes.EXTENSION)) {
            throw new ExtensionInvalidaException(
                    "El archivo debe tener extensión " + Constantes.EXTENSION);
        }

        Documento doc = new Documento();

        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            String magic = raf.readUTF();
            if (!magic.equals(Constantes.ID)) {
                throw new ArchivoCorruptoException(
                "Cabecera inválida: el archivo no es un .edt reconocible");
            }

            int version = raf.readInt();
            if (version != Constantes.VERSION_ACTUAL) {
                throw new ArchivoCorruptoException("Versión de formato no soportada: " + version);
            }

            int cantidadBloques = raf.readInt();
            if (cantidadBloques < 0) {
                throw new ArchivoCorruptoException("Cantidad de bloques inválida");
            }

            for (int i = 0; i < cantidadBloques; i++) {
                byte codigoTipo = raf.readByte();

                if (codigoTipo == Constantes.TIPO_FRAGMENTO) {
                    doc.addBloque(leerFragmento(raf));
                } else if (codigoTipo == Constantes.TIPO_TABLA) {
                    doc.addBloque(leerTabla(raf));
                } else {
                    throw new ArchivoCorruptoException("Tipo de bloque desconocido: " + codigoTipo);
                }
            }

        } catch (EOFException e) {
            throw new ArchivoTruncadoException("El archivo está incompleto o fue truncado a la mitad");
        }

        return doc;
    }

    private Fragmento leerFragmento(RandomAccessFile raf) throws IOException {
        String texto = raf.readUTF();
        int colorRGB = raf.readInt();
        boolean negrita = raf.readBoolean();
        boolean cursiva = raf.readBoolean();
        boolean subrayado = raf.readBoolean();
        boolean tachado = raf.readBoolean();
        String fuente = raf.readUTF();
        int size = raf.readInt();

        FormatoTexto formato = new FormatoTexto(new Color(colorRGB), negrita, cursiva, subrayado, tachado, fuente, size);

        return new Fragmento(texto, formato);
    }

    private Tabla leerTabla(RandomAccessFile raf) throws IOException {
        int filas = raf.readInt();
        int columnas = raf.readInt();

        Tabla tabla = new Tabla(filas, columnas);

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                int cantidadFragmentos = raf.readInt();
                Celda celda = new Celda();
                for (int i = 0; i < cantidadFragmentos; i++) {
                    celda.addFragmento(leerFragmento(raf));
                }
                tabla.setCelda(f, c, celda);
            }
        }

        return tabla;
    }
}
