/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta.PanelesInsta;

import ConfigInsta.UtilImagen;
import Excepciones.ArchivoCorruptoException;
import Insta.NavegarInsta;
import Insta.SesionActual;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author vasqu
 */
public class PanelSidebar extends JPanel{
    private AvatarCircular avatar;
    private String usernameActual;
    public PanelSidebar(NavegarInsta navegador) throws ArchivoCorruptoException {
      
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        this.usernameActual = SesionActual.getInstancia().getUserActual().getUser();
       
       
        setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)),
        BorderFactory.createEmptyBorder(150, 0, 20, 0)
));
        add(crearFilaNav("/Insta/Imagenes/houseIcon.png", "Inicio", () -> navegador.mostrarTimeline()));
        add(crearFilaNav("/Insta/Imagenes/searchIcon.png", "Buscar", () -> navegador.mostrarBuscar()));
        add(crearFilaNav("/Insta/Imagenes/notifyIcon.png", "Notificaciones", () -> navegador.mostrarNotificaciones()));
        add(crearFilaNav("/Insta/Imagenes/InboxIcon.png", "Mensajes", () -> navegador.mostrarInbox()));
        add(crearFilaNav("/Insta/Imagenes/plusIcon.png", "Crear", () -> navegador.mostrarPublicar()));
        
        add(crearFilaPerfil(navegador));
       
        
        

        add(Box.createVerticalGlue());   
        add(crearFilaNav("/Insta/Imagenes/settingsIcon.png", "Configuración", () -> { /* pendiente */ }));
        add(crearFilaNav("/Insta/Imagenes/offIcon.png", "Cerrar sesión", () -> navegador.cerrarSesion()));
}

    
private JPanel crearFilaNav(String rutaIcono, String texto, Runnable accion) {
    JPanel fila = new JPanel();
    fila.setLayout(new BoxLayout(fila, BoxLayout.X_AXIS));
    fila.setBackground(Color.WHITE);
    fila.setBorder(BorderFactory.createEmptyBorder(8, 16, 16, 16));
    fila.setAlignmentX(Component.LEFT_ALIGNMENT);
    fila.setMaximumSize(new Dimension(220, 40));

    ImageIcon icono1 = UtilImagen.cargarIconoEscalado(rutaIcono, 26, 26);
    JLabel icono = new JLabel(icono1);

    JLabel etiqueta = new JLabel(texto);
    etiqueta.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));

    fila.add(icono);
    fila.add(etiqueta);
    fila.add(Box.createHorizontalGlue());   

    fila.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    fila.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            accion.run();
        }
    });

    return fila;
}
private JPanel crearFilaPerfil(NavegarInsta navegador) throws ArchivoCorruptoException {
    JPanel fila = new JPanel();
    fila.setLayout(new BoxLayout(fila, BoxLayout.X_AXIS));
    fila.setAlignmentX(Component.LEFT_ALIGNMENT);
    fila.setBackground(Color.WHITE);
    fila.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
    fila.setAlignmentX(Component.LEFT_ALIGNMENT);
    fila.setMaximumSize(new Dimension(220, 40));

    AvatarCircular avatar = AvatarCircular.crear(usernameActual, 32);
    JLabel etiqueta = new JLabel("Perfil");
    etiqueta.setBorder(BorderFactory.createEmptyBorder(0, 9, 0, 0));

    fila.add(avatar);
    fila.add(Box.createHorizontalStrut(0));
    fila.add(etiqueta);
    

    fila.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    fila.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            String user = SesionActual.getInstancia().getUserActual().getUser();
            navegador.mostrarPerfil(user);
        }
    });

    return fila;
}
public void actualizarAvatar() {
       if (SesionActual.getInstancia().getUserActual() != null) {
        this.usernameActual = SesionActual.getInstancia().getUserActual().getUser();

        
        Container filaPerfil = avatar.getParent();

        if (filaPerfil != null) {
          
            filaPerfil.remove(avatar);

            try {
                
                avatar = AvatarCircular.crear(usernameActual, 32);

              
                filaPerfil.add(avatar, 0);

                filaPerfil.revalidate();
                filaPerfil.repaint();
            } catch (ArchivoCorruptoException e) {
                e.printStackTrace();
            }
        }
    }
}}
