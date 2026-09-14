package mini.windows;

import Excepciones.ArchivoCorruptoException;
import base.ListaEnlazada;
import base.Usuario;
import editordetexto.GUIEditorTexto;
import consola.*;
import reproductorMusica.reproductorPanel;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.io.File;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author gabri
 */
public class VentanaPrincipal extends JFrame {

    private JDesktopPane escritorio;

    private JPanel panelBarraTareas;

    private final Map<String, JInternalFrame> ventanasAbiertas = new LinkedHashMap<>();

    private final Map<JInternalFrame, JButton> botonesTareas = new LinkedHashMap<>();

    public VentanaPrincipal() {
        super("Mini-Windows");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        escritorio = new JDesktopPane();
        escritorio.setBackground(new Color(0, 90, 140));
        escritorio.setDesktopManager(new GestorEscritorioSinIconos());
        add(escritorio, BorderLayout.CENTER);

        add(construirBarraInferior(), BorderLayout.SOUTH);
    }

    private JPanel construirBarraInferior() {
        JPanel barraInferior = new JPanel(new BorderLayout());

        barraInferior.add(construirBarraMenu(), BorderLayout.WEST);

        panelBarraTareas = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        JScrollPane scrollTareas = new JScrollPane(panelBarraTareas,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollTareas.setBorder(null);
        scrollTareas.setPreferredSize(new Dimension(10, 40));
        barraInferior.add(scrollTareas, BorderLayout.CENTER);
        JButton botonEscritorio = new JButton("Mostrar escritorio");
        botonEscritorio.addActionListener(e -> mostrarEscritorio());
        barraInferior.add(botonEscritorio, BorderLayout.EAST);
        return barraInferior;
    }

    private JMenuBar construirBarraMenu() {
        JMenuBar barra = new JMenuBar();
        Usuario actual = SesionActual.getUsuarioActual();

        JMenu menuHerramientas = new JMenu("Herramientas");
        agregarItem(menuHerramientas, "Explorador de archivos", e -> abrirExplorador(actual));
        agregarItem(menuHerramientas, "Editor de texto", e -> abrirEditorTexto(actual.getUser()));
        agregarItem(menuHerramientas, "Visor de imágenes", e -> abrirVisorImagenes(actual.getUser()));
        agregarItem(menuHerramientas, "Consola de comandos", e -> abrirConsola(actual.getUser()));
        agregarItem(menuHerramientas, "Reproductor de música", e -> abrirReproductorMusica(actual.getUser()));
        barra.add(menuHerramientas);

        if (actual.getEsAdmin()) {
            JMenu menuAdmin = new JMenu("Administración");
            agregarItem(menuAdmin, "Crear usuario", e -> crearUsuarioDesdeAdmin());
            agregarItem(menuAdmin, "Ver carpeta de otro usuario", e -> verCarpetaDeOtroUsuario());
            barra.add(menuAdmin);
        }

        JMenu menuSesion = new JMenu("Sesión");
        agregarItem(menuSesion, "Usuario actual: " + actual.getUser(), null).setEnabled(false);
        agregarItem(menuSesion, "Cerrar sesión", e -> cerrarSesion());
        barra.add(menuSesion);

        return barra;
    }

    private JMenuItem agregarItem(JMenu menu, String texto, java.awt.event.ActionListener accion) {
        JMenuItem item = new JMenuItem(texto);
        if (accion != null) item.addActionListener(accion);
        menu.add(item);
        return item;
    }

    private void mostrarOCrearVentana(String clave, String titulo, Icon icono,
                                       java.util.function.Supplier<JPanel> creadorContenido,
                                       int ancho, int alto) {
        JInternalFrame frame = ventanasAbiertas.get(clave);

        if (frame == null) {
            frame = new JInternalFrame(titulo, true, true, true, true);
            if (icono != null) frame.setFrameIcon(icono);
            frame.setSize(ancho, alto);
            frame.setLocation(30 + (ventanasAbiertas.size() % 8) * 25,
                    30 + (ventanasAbiertas.size() % 8) * 25);

            JPanel contenido = creadorContenido.get();
            frame.setContentPane(contenido);

            final JInternalFrame frameFinal = frame;
            frame.addInternalFrameListener(new InternalFrameAdapter() {
                @Override
                public void internalFrameClosing(InternalFrameEvent e) {
                    cerrarVentana(clave, frameFinal);
                }
            });
            frame.addPropertyChangeListener(JInternalFrame.IS_ICON_PROPERTY,
                    (PropertyChangeListener) this::onCambioIcono);

            ventanasAbiertas.put(clave, frame);
            escritorio.add(frame);
            agregarBotonTarea(frame, titulo);
        }

        try {
            if (frame.isIcon()) frame.setIcon(false);
            frame.setVisible(true);
            escritorio.getDesktopManager().deiconifyFrame(frame);
            frame.moveToFront();
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException ignorado) {
        }
    }

    private void onCambioIcono(PropertyChangeEvent evt) {
        JInternalFrame frame = (JInternalFrame) evt.getSource();
        JButton boton = botonesTareas.get(frame);
        if (boton != null) {
            boton.setFont(boton.getFont().deriveFont(
                    frame.isIcon() ? Font.ITALIC : Font.PLAIN));
        }
    }

    private void agregarBotonTarea(JInternalFrame frame, String titulo) {
        JButton boton = new JButton(titulo);
        boton.addActionListener(e -> alternarVentana(frame));
        botonesTareas.put(frame, boton);
        panelBarraTareas.add(boton);
        panelBarraTareas.revalidate();
        panelBarraTareas.repaint();
    }

    private void alternarVentana(JInternalFrame frame) {
        try {
            if (frame.isIcon()) {
                frame.setIcon(false);
                frame.setSelected(true);
                frame.moveToFront();
            } else if (frame.isSelected()) {
                frame.setIcon(true);
            } else {
                frame.setSelected(true);
                frame.moveToFront();
            }
        } catch (java.beans.PropertyVetoException ignorado) {
        }
    }

    private void cerrarVentana(String clave, JInternalFrame frame) {
        if (frame.getContentPane() instanceof reproductorPanel) {
            ((reproductorPanel) frame.getContentPane()).detener();
        }

        ventanasAbiertas.remove(clave);
        JButton boton = botonesTareas.remove(frame);
        if (boton != null) {
            panelBarraTareas.remove(boton);
            panelBarraTareas.revalidate();
            panelBarraTareas.repaint();
        }
        frame.dispose();
    }

    private void mostrarEscritorio() {
        for (JInternalFrame frame : ventanasAbiertas.values()) {
            try {
                if (!frame.isIcon()) frame.setIcon(true);
            } catch (java.beans.PropertyVetoException ignorado) {
            }
        }
    }

    private void abrirExplorador(Usuario usuario) {
        String username = usuario.getUser();
        String ruta = usuario.getEsAdmin() ? GestorArchivos.RAIZ : GestorArchivos.rutaCarpetaUsuario(username);
        mostrarOCrearVentana("explorador:" + username, "Explorador de archivos — " + username, null,
                () -> {
                    ExploradorPanel panel = new ExploradorPanel(ruta);
                    panel.setOyenteApertura(archivo -> manejarAperturaDeArchivo(archivo, username));
                    return panel;
                }, 650, 420);
    }

    private void manejarAperturaDeArchivo(File archivo, String username) {
        String nombre = archivo.getName().toLowerCase();

        if (nombre.endsWith(persistencia.Constantes.EXTENSION)
                || nombre.endsWith(persistencia.Constantes.EXTENSION_LEGADO)) {
            abrirEditorTexto(username, archivo);
        } else if (esImagen(nombre)) {
            abrirVisorImagenes(username, archivo);
        } else if (nombre.endsWith(".mp3")) {
            abrirReproductorMusica(username, archivo);
        } else {
            JOptionPane.showMessageDialog(this,
                    "No hay una herramienta asociada a este tipo de archivo (" + nombre + ").",
                    "Tipo de archivo no soportado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean esImagen(String nombreEnMinusculas) {
        return nombreEnMinusculas.endsWith(".jpg") || nombreEnMinusculas.endsWith(".jpeg")
                || nombreEnMinusculas.endsWith(".png") || nombreEnMinusculas.endsWith(".gif")
                || nombreEnMinusculas.endsWith(".bmp");
    }

    private void abrirEditorTexto(String username) {
        abrirEditorTexto(username, null);
    }

    private void abrirEditorTexto(String username, File archivoAAbrir) {
        String rutaUsuario = GestorArchivos.rutaCarpetaUsuario(username);
        String clave = "editor:" + username;
        mostrarOCrearVentana(clave, "Editor de texto — " + username, null,
                () -> new GUIEditorTexto(rutaUsuario), 650, 450);

        if (archivoAAbrir != null) {
            JInternalFrame frame = ventanasAbiertas.get(clave);
            if (frame != null && frame.getContentPane() instanceof GUIEditorTexto) {
                ((GUIEditorTexto) frame.getContentPane()).abrirArchivoExterno(archivoAAbrir);
            }
        }
    }

    private void abrirVisorImagenes(String username) {
        abrirVisorImagenes(username, null);
    }

    private void abrirVisorImagenes(String username, File archivoAAbrir) {
        String rutaUsuario = GestorArchivos.rutaCarpetaUsuario(username);
        String clave = "visor:" + username;
        mostrarOCrearVentana(clave, "Visor de imágenes — " + username, null,
                () -> new VisorImagenesPanel(rutaUsuario), 600, 450);

        if (archivoAAbrir != null) {
            JInternalFrame frame = ventanasAbiertas.get(clave);
            if (frame != null && frame.getContentPane() instanceof VisorImagenesPanel) {
                ((VisorImagenesPanel) frame.getContentPane()).abrirArchivoExterno(archivoAAbrir);
            }
        }
    }

    private void abrirConsola(String username) {
        String rutaUsuario = GestorArchivos.rutaCarpetaUsuario(username);
        mostrarOCrearVentana("consola:" + username, "Consola — " + username, null,
                () -> new ConsolaPanel(rutaUsuario), 600, 380);
    }

    private void abrirReproductorMusica(String username) {
        abrirReproductorMusica(username, null);
    }

    private void abrirReproductorMusica(String username, File archivoAAbrir) {
        String rutaUsuario = GestorArchivos.rutaCarpetaUsuario(username);
        String clave = "reproductor:" + username;
        mostrarOCrearVentana(clave, "Reproductor de Música — " + username, null,
                () -> new reproductorPanel(rutaUsuario), 800, 500);

        if (archivoAAbrir != null) {
            JInternalFrame frame = ventanasAbiertas.get(clave);
            if (frame != null && frame.getContentPane() instanceof reproductorPanel) {
                ((reproductorPanel) frame.getContentPane()).abrirArchivoExterno(archivoAAbrir);
            }
        }
    }

    private void mostrarProximamente(String nombreHerramienta) {
        mostrarOCrearVentana("proximamente:" + nombreHerramienta, nombreHerramienta, null, () -> {
            JLabel label = new JLabel(nombreHerramienta + " — próximamente", SwingConstants.CENTER);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(label, BorderLayout.CENTER);
            return panel;
        }, 400, 200);
    }

    private void crearUsuarioDesdeAdmin() {
        String username = JOptionPane.showInputDialog(this, "Nuevo username:");
        if (username == null || username.trim().isEmpty()) return;

        String password = JOptionPane.showInputDialog(this, "Contraseña:");
        if (password == null || password.trim().isEmpty()) return;

        int esAdmin = JOptionPane.showConfirmDialog(this, "¿Será administrador?",
                "Tipo de usuario", JOptionPane.YES_NO_OPTION);

        try {
            GestorArchivos.crearUsuario(username.trim(), password, esAdmin == JOptionPane.YES_OPTION);
            JOptionPane.showMessageDialog(this, "Usuario creado correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo crear el usuario: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verCarpetaDeOtroUsuario() {
        try {
            ListaEnlazada<Usuario> usuarios = base.ArchivoUtil.leerLista(GestorArchivos.RUTA_USUARIOS);            
            String[] nombres = new String[usuarios.length()];
            for (int i = 0; i < usuarios.length(); i++) {
                nombres[i] = usuarios.obtenerEn(i).getUser();
            }

            String elegido = (String) JOptionPane.showInputDialog(this,
                    "Selecciona un usuario:", "Ver carpeta",
                    JOptionPane.PLAIN_MESSAGE, null, nombres, nombres.length > 0 ? nombres[0] : null);
            if (elegido != null) {
                Usuario elegidoObj = usuarios.getCabeza().getDato();
                for (int i = 0; i < usuarios.length(); i++) {
                    if(usuarios.obtenerEn(i).getUser().equalsIgnoreCase(elegido)){
                        elegidoObj = usuarios.obtenerEn(i);
                    }
                }
                abrirExplorador(elegidoObj);
            }
        } catch (ArchivoCorruptoException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo leer la lista de usuarios.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cerrarSesion() {
        java.util.List<Map.Entry<String, JInternalFrame>> listaVentanas = 
                new java.util.ArrayList<>(ventanasAbiertas.entrySet());

        for (Map.Entry<String, JInternalFrame> entry : listaVentanas) {
            cerrarVentana(entry.getKey(), entry.getValue());
        }

        SesionActual.cerrarSesion();
        new LoginFrame().setVisible(true);
        dispose();
    }

    private static class GestorEscritorioSinIconos extends DefaultDesktopManager {
        @Override
        public void iconifyFrame(JInternalFrame f) {
            if (f.isSelected()) {
                try {
                    f.setSelected(false);
                } catch (java.beans.PropertyVetoException ignorado) {
                }
            }
            f.setVisible(false);
        }

        @Override
        public void deiconifyFrame(JInternalFrame f) {
            f.setVisible(true);
        }
    }
}