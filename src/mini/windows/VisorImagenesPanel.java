package mini.windows;

import base.FileChooserUtil;
import base.ListaEnlazada;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class VisorImagenesPanel extends JPanel {

    private static final String[] EXTENSIONES_IMAGEN = {"jpg", "jpeg", "png", "gif", "bmp"};
    private static final int TAMANO_MINIATURA = 80;

    private final File carpetaRaizUsuario;
    private ListaEnlazada<File> imagenesCarpetaActual = new ListaEnlazada<>();
    private int indiceActual = -1;

    private File carpetaActual;

    private JLabel labelImagen;
    private JLabel labelInfo;
    private JButton botonAnterior;
    private JButton botonSiguiente;

    private JPanel panelMiniaturas;
    private JButton[] botonesMiniatura;

    public VisorImagenesPanel(String rutaRaizUsuario) {
        this.carpetaRaizUsuario = new File(rutaRaizUsuario);
        setLayout(new BorderLayout());

        add(construirBarraHerramientas(), BorderLayout.NORTH);

        labelImagen = new JLabel("", SwingConstants.CENTER);
        add(new JScrollPane(labelImagen), BorderLayout.CENTER);

        add(construirPanelInferior(), BorderLayout.SOUTH);

        File misImagenes = new File(carpetaRaizUsuario, "Mis Imagenes");
        if (misImagenes.exists()) {
            cargarCarpeta(misImagenes);
        } else {
            carpetaActual = carpetaRaizUsuario;
        }
    }

    private JToolBar construirBarraHerramientas() {
        JToolBar barra = new JToolBar();
        barra.setFloatable(false);

        JButton botonAbrir = new JButton("Abrir imagen...");
        botonAbrir.addActionListener(e -> abrirImagen());
        barra.add(botonAbrir);

        barra.addSeparator();

        botonAnterior = new JButton("< Anterior");
        botonAnterior.addActionListener(e -> mostrarIndice(indiceActual - 1));
        barra.add(botonAnterior);

        botonSiguiente = new JButton("Siguiente >");
        botonSiguiente.addActionListener(e -> mostrarIndice(indiceActual + 1));
        barra.add(botonSiguiente);

        actualizarBotones();
        return barra;
    }

    private JPanel construirPanelInferior() {
        panelMiniaturas = new JPanel();
        panelMiniaturas.setLayout(new BoxLayout(panelMiniaturas, BoxLayout.X_AXIS));
        panelMiniaturas.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        JScrollPane scrollMiniaturas = new JScrollPane(panelMiniaturas,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollMiniaturas.setPreferredSize(new Dimension(100, TAMANO_MINIATURA + 25));

        labelInfo = new JLabel("Sin imagen abierta", SwingConstants.CENTER);
        labelInfo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(scrollMiniaturas, BorderLayout.CENTER);
        panelInferior.add(labelInfo, BorderLayout.SOUTH);
        return panelInferior;
    }

    private void abrirImagen() {
        JFileChooser selector = FileChooserUtil.crearRestringido(carpetaRaizUsuario);
        selector.setCurrentDirectory(carpetaActual != null ? carpetaActual : carpetaRaizUsuario);
        selector.setFileFilter(new FileNameExtensionFilter("Imágenes", EXTENSIONES_IMAGEN));

        int opcion = selector.showOpenDialog(this);
        if (opcion != JFileChooser.APPROVE_OPTION) return;

        File archivoElegido = selector.getSelectedFile();
        cargarCarpeta(archivoElegido.getParentFile());

        for (int i = 0; i < imagenesCarpetaActual.length(); i++) {
            if (imagenesCarpetaActual.obtenerEn(i).equals(archivoElegido)) {
                mostrarIndice(i);
                return;
            }
        }
    }

    private void cargarCarpeta(File carpeta) {
        carpetaActual = carpeta;
        imagenesCarpetaActual = new ListaEnlazada<>();

        File[] archivos = carpeta.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                if (!archivo.isDirectory() && esImagen(archivo)) {
                    imagenesCarpetaActual.insertarFinal(archivo);
                }
            }
        }

        construirTiraDeMiniaturas();

        if (imagenesCarpetaActual.estaVacia()) {
            indiceActual = -1;
            labelImagen.setIcon(null);
            labelInfo.setText("No hay imágenes en esta carpeta.");
        } else {
            mostrarIndice(0);
        }
        actualizarBotones();
    }

    private boolean esImagen(File archivo) {
        String nombre = archivo.getName().toLowerCase();
        for (String ext : EXTENSIONES_IMAGEN) {
            if (nombre.endsWith("." + ext)) return true;
        }
        return false;
    }

    private void construirTiraDeMiniaturas() {
        panelMiniaturas.removeAll();
        int cantidad = imagenesCarpetaActual.length();
        botonesMiniatura = new JButton[cantidad];

        for (int i = 0; i < cantidad; i++) {
            JButton boton = new JButton();
            boton.setPreferredSize(new Dimension(TAMANO_MINIATURA, TAMANO_MINIATURA));
            boton.setMaximumSize(new Dimension(TAMANO_MINIATURA, TAMANO_MINIATURA));
            boton.setToolTipText(imagenesCarpetaActual.obtenerEn(i).getName());

            final int indice = i;
            boton.addActionListener(e -> mostrarIndice(indice));

            botonesMiniatura[i] = boton;
            panelMiniaturas.add(boton);
            panelMiniaturas.add(Box.createHorizontalStrut(4));
        }

        panelMiniaturas.revalidate();
        panelMiniaturas.repaint();

        if (cantidad > 0) {
            new Thread(new CargadorGaleriaTarea(imagenesCarpetaActual, TAMANO_MINIATURA, (indice, miniatura) -> {
                if (indice < botonesMiniatura.length) {
                    botonesMiniatura[indice].setIcon(miniatura);
                }
            })).start();
        }
    }

    private void mostrarIndice(int indice) {
        if (indice < 0 || indice >= imagenesCarpetaActual.length()) return;

        indiceActual = indice;
        File archivo = imagenesCarpetaActual.obtenerEn(indice);

        labelInfo.setText("Cargando " + archivo.getName() + "...");
        actualizarBotones();
        resaltarMiniaturaActual();

        int anchoDisponible = Math.max(getWidth() - 40, 300);
        int altoDisponible = Math.max(getHeight() - 150, 300);

        new Thread(new CargadorImagenTarea(archivo, anchoDisponible, altoDisponible, icono -> {
            if (icono != null) {
                labelImagen.setIcon(icono);
                labelInfo.setText(archivo.getName() + "  (" + (indiceActual + 1) + " de "
                        + imagenesCarpetaActual.length() + ")");
            } else {
                labelImagen.setIcon(null);
                labelInfo.setText("No se pudo cargar " + archivo.getName());
            }
        })).start();
    }

    private void resaltarMiniaturaActual() {
        if (botonesMiniatura == null) return;

        for (int i = 0; i < botonesMiniatura.length; i++) {
            boolean esActual = (i == indiceActual);
            botonesMiniatura[i].setBorder(BorderFactory.createLineBorder(
                    esActual ? Color.BLUE : Color.LIGHT_GRAY, esActual ? 3 : 1));
        }

        if (indiceActual >= 0 && indiceActual < botonesMiniatura.length) {
            botonesMiniatura[indiceActual].scrollRectToVisible(botonesMiniatura[indiceActual].getBounds());
        }
    }

    private void actualizarBotones() {
        botonAnterior.setEnabled(indiceActual > 0);
        botonSiguiente.setEnabled(indiceActual >= 0 && indiceActual < imagenesCarpetaActual.length() - 1);
    }
}
