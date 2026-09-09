/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import editordetexto.*;
import editordetexto.excepciones.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 *
 * @author gabri
 */
public class EdtWrite {
    public void guardar(Documento doc, File archivo) throws IOException, ExtensionInvalidaException{
        if (!archivo.getName().toLowerCase().endsWith(Constantes.EXTENSION)) {
            throw new ExtensionInvalidaException(
                    "El archivo debe tener extensión " + Constantes.EXTENSION);
        }

        if (archivo.exists()) {
            archivo.delete();
        }

        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            raf.writeUTF(Constantes.ID);
            raf.writeInt(Constantes.VERSION_ACTUAL);

            ArrayList<Object> bloques = doc.getBloques();
            raf.writeInt(bloques.size());

            for (Object bloque : bloques) {
                if (bloque instanceof Fragmento) {
                    raf.writeByte(Constantes.TIPO_FRAGMENTO);
                    escribirFragmento(raf, (Fragmento) bloque);
                } else if (bloque instanceof Tabla) {
                    raf.writeByte(Constantes.TIPO_TABLA);
                    escribirTabla(raf, (Tabla) bloque);
                }
            }
        }
    }

    private void escribirFragmento(RandomAccessFile raf, Fragmento frag) throws IOException {
        FormatoTexto f = frag.getFormato();

        raf.writeUTF(frag.getTexto());
        raf.writeInt(f.getColor().getRGB());
        raf.writeBoolean(f.isNegrita());
        raf.writeBoolean(f.isCursiva());
        raf.writeBoolean(f.isSubrayado());
        raf.writeBoolean(f.isTachado());
        raf.writeUTF(f.getFuente());
        raf.writeInt(f.getsize());
    }

    private void escribirTabla(RandomAccessFile raf, Tabla t) throws IOException {
        raf.writeInt(t.getFilas());
        raf.writeInt(t.getColumnas());

        for (int f = 0; f < t.getFilas(); f++) {
            for (int c = 0; c < t.getColumnas(); c++) {
                Celda celda = t.getCelda(f, c);
                ArrayList<Fragmento> contenido = celda.getContenido();
                raf.writeInt(contenido.size());
                for (Fragmento frag : contenido) {
                    escribirFragmento(raf, frag);
                }
            }
        }
    }  
}
