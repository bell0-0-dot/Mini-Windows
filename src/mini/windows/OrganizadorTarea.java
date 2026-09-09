package mini.windows;

import base.ListaEnlazada;
import java.io.File;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;

public class OrganizadorTarea implements Runnable {

    private static final String[] EXT_IMAGENES = {"jpg", "jpeg", "png", "gif", "bmp"};
    private static final String[] EXT_DOCUMENTOS = {"pdf", "doc", "docx", "txt", "xlsx", "pptx"};
    private static final String[] EXT_MUSICA = {"mp3", "wav", "ogg"};

    private final File carpeta;
    private final Consumer<OrganizadorTarea> alTerminar;

    private final ListaEnlazada<String> imagenesOrganizadas = new ListaEnlazada<>();
    private final ListaEnlazada<String> documentosOrganizados = new ListaEnlazada<>();
    private final ListaEnlazada<String> musicaOrganizada = new ListaEnlazada<>();

    public OrganizadorTarea(File carpeta, Consumer<OrganizadorTarea> alTerminar) {
        this.carpeta = carpeta;
        this.alTerminar = alTerminar;
    }

    @Override
    public void run() {
        File[] archivos = carpeta.listFiles();

        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isDirectory()) {
                    continue;
                }
                String categoria = categoriaDe(archivo);
                if (categoria != null) {
                    moverA(archivo, categoria);
                }
            }
        }

        if (alTerminar != null) {
            SwingUtilities.invokeLater(() -> alTerminar.accept(this));
        }
    }

    private String categoriaDe(File archivo) {
        String nombre = archivo.getName().toLowerCase();
        int punto = nombre.lastIndexOf('.');
        if (punto == -1) return null;

        String ext = nombre.substring(punto + 1);
        if (contiene(EXT_IMAGENES, ext)) return "Imagenes";
        if (contiene(EXT_DOCUMENTOS, ext)) return "Documentos";
        if (contiene(EXT_MUSICA, ext)) return "Musica";
        return null;
    }

    private boolean contiene(String[] arreglo, String valor) {
        for (String s : arreglo) {
            if (s.equals(valor)) return true;
        }
        return false;
    }

    private void moverA(File archivo, String nombreSubcarpeta) {
        File carpetaDestino = new File(carpeta, nombreSubcarpeta);
        if (!carpetaDestino.exists()) {
            carpetaDestino.mkdirs();
        }

        File destino = new File(carpetaDestino, archivo.getName());
        if (destino.exists()) {
            destino.delete();
        }

        boolean movido = archivo.renameTo(destino);
        if (!movido) {
            System.err.println("No se pudo mover " + archivo.getName());
            return;
        }

        registrarEnLista(nombreSubcarpeta, destino.getAbsolutePath());
    }

    private void registrarEnLista(String categoria, String rutaArchivo) {
        switch (categoria) {
            case "Imagenes":
                imagenesOrganizadas.insertarFinal(rutaArchivo);
                break;
            case "Documentos":
                documentosOrganizados.insertarFinal(rutaArchivo);
                break;
            case "Musica":
                musicaOrganizada.insertarFinal(rutaArchivo);
                break;
        }
    }

    public ListaEnlazada<String> getImagenesOrganizadas() {
        return imagenesOrganizadas;
    }

    public ListaEnlazada<String> getDocumentosOrganizados() {
        return documentosOrganizados;
    }

    public ListaEnlazada<String> getMusicaOrganizada() {
        return musicaOrganizada;
    }
}