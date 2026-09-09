/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mini.windows;

import Excepciones.ArchivoCorruptoException;
import base.ListaEnlazada;
import base.Usuario;
import editordetexto.GUIEditorTexto;
 
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author gabri
 */
public class VentanaPrincipal extends JFrame{
    private JPanel panelCentral;
    private CardLayout distribuidorTarjetas;

    private JPanel panelExplorador;
    private JPanel panelEditor;
 
    public VentanaPrincipal() {
        super("Mini-Windows");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
 
        distribuidorTarjetas = new CardLayout();
        panelCentral = new JPanel(distribuidorTarjetas);
        add(panelCentral, BorderLayout.CENTER);

        panelCentral.add(construirPanelBienvenida(), "bienvenida");
 
        add(construirBarraMenu(),BorderLayout.SOUTH);
        mostrarBienvenida();
    }
 
    private JMenuBar construirBarraMenu() {
        JMenuBar barra = new JMenuBar();
        Usuario actual = SesionActual.getUsuarioActual();
 
        JMenu menuHerramientas = new JMenu("Herramientas");
        agregarItem(menuHerramientas, "Explorador de archivos", e -> abrirExplorador(actual.getUser()));
        agregarItem(menuHerramientas, "Editor de texto", e -> abrirEditorTexto(actual.getUser()));
        agregarItem(menuHerramientas, "Visor de imágenes", e -> abrirVisorImagenes(actual.getUser()));
        agregarItem(menuHerramientas, "Consola de comandos", e -> mostrarProximamente("Consola de comandos"));
        agregarItem(menuHerramientas, "Reproductor de música", e -> mostrarProximamente("Reproductor de música"));
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
        
        barra.add(Box.createHorizontalGlue());
        
        JButton inicio = new JButton("Inicio");
        inicio.addActionListener(e -> mostrarBienvenida());
        barra.add(inicio);
        
        return barra;
    }
 
    private JMenuItem agregarItem(JMenu menu, String texto, java.awt.event.ActionListener accion) {
        JMenuItem item = new JMenuItem(texto);
        if (accion != null) item.addActionListener(accion);
        menu.add(item);
        return item;
    }
 
    private final java.util.Map<String, JPanel> panelesAbiertos = new java.util.HashMap<>();

    private JPanel construirPanelBienvenida() {
        Usuario actual = SesionActual.getUsuarioActual();
        JLabel label = new JLabel(
                "Bienvenido, " + actual.getUser() + " (" +
                (actual.getEsAdmin() ? "administrador" : "usuario estándar") + ")",
                SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(16f));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private void mostrarBienvenida() {
        distribuidorTarjetas.show(panelCentral, "bienvenida");
    }

    private void mostrarOCrearPanel(String clave, java.util.function.Supplier<JPanel> creador) {
        JPanel panel = panelesAbiertos.get(clave);
        if (panel == null) {
            panel = creador.get();
            panelesAbiertos.put(clave, panel);
            panelCentral.add(panel, clave);
        }
        distribuidorTarjetas.show(panelCentral, clave);
    }

    private void abrirExplorador(String username) {
        String ruta = GestorArchivos.rutaCarpetaUsuario(username);
        mostrarOCrearPanel("explorador:" + username, () -> new ExploradorPanel(ruta));
    }

    private void abrirEditorTexto(String username) {
        String rutaUsuario = GestorArchivos.rutaCarpetaUsuario(username);
        mostrarOCrearPanel("editor:" + username, () -> new GUIEditorTexto(rutaUsuario));
    }

    private void abrirVisorImagenes(String username) {
        String rutaUsuario = GestorArchivos.rutaCarpetaUsuario(username);
        mostrarOCrearPanel("visor:" + username, () -> new VisorImagenesPanel(rutaUsuario));
    }
 
    private void mostrarProximamente(String nombreHerramienta) {
        mostrarOCrearPanel("proximamente:" + nombreHerramienta, () -> {
            JLabel label = new JLabel(nombreHerramienta + " — próximamente", SwingConstants.CENTER);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(label, BorderLayout.CENTER);
            return panel;
        });
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
                abrirExplorador(elegido);
            }
        } catch (ArchivoCorruptoException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo leer la lista de usuarios.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    private void cerrarSesion() {
        SesionActual.cerrarSesion();
        new LoginFrame().setVisible(true);
        dispose();
    }
}