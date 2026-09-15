/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta.PanelesInsta;

import Excepciones.ArchivoCorruptoException;
import Insta.NavegarInsta;
import Insta.SesionActual;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Frame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author vasqu
 */
public class PanelApp extends JPanel implements NavegarInsta{
    private static final String TARJETA_TIMELINE = "timeline";
    private static final String TARJETA_PERFIL = "perfil";
    private static final String TARJETA_BUSCAR = "buscar";
    private static final String TARJETA_INBOX = "inbox";
    private static final String TARJETA_NOTIFICACIONES = "notificaciones";
    private static final String TARJETA_PUBLICAR = "publicar";

    private CardLayout cardLayout;
    private JPanel contenedorTarjetas;
    private PanelPublicar panelPublicar;
    private PanelTimeline panelTimeline;
    private PanelPerfil panelPerfil;
    private PanelBuscar panelBuscar;
    private PanelLogin panelLogin;
    private PanelInbox panelInbox;
    private NavegarInsta navegador;

    public PanelApp(NavegarInsta navegador) {
        this.navegador = navegador;
        
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
         cardLayout =new CardLayout();
         contenedorTarjetas=new JPanel(cardLayout);
       try {
        panelTimeline = new PanelTimeline(this);
        panelPerfil = new PanelPerfil(this, SesionActual.getInstancia().getUserActual().getUser());
        panelBuscar = new PanelBuscar(this);
        panelInbox = new PanelInbox(this);
       
     

        contenedorTarjetas.add(panelTimeline, TARJETA_TIMELINE);
        contenedorTarjetas.add(panelPerfil, TARJETA_PERFIL);
        contenedorTarjetas.add(panelBuscar, TARJETA_BUSCAR);
        contenedorTarjetas.add(panelInbox, TARJETA_INBOX);
 
        add(new PanelSidebar(this), BorderLayout.WEST);
        } catch (ArchivoCorruptoException e) {
            add(new JLabel("No se pudo iniciar la aplicación: archivo de datos dañado."), BorderLayout.CENTER);
            return;
        }
        add(contenedorTarjetas, BorderLayout.CENTER);
        
        cardLayout.show(contenedorTarjetas, TARJETA_TIMELINE);
        
        recargarSesion();
    
    }
    @Override
    public void mostrarTimeline() {
        try {
            panelTimeline.refrescar();
        } catch (ArchivoCorruptoException e) {
             add(new JLabel("No se pudo iniciar la aplicación no hay datos para mostrar"), BorderLayout.CENTER);
            return;
        }
        cardLayout.show(contenedorTarjetas, TARJETA_TIMELINE);
    }
    
    @Override
    public void mostrarPerfil(String username) {
         try {
            panelPerfil.cargarPerfil(username);
            panelPerfil.cargarPerfil(username);
    } catch (ArchivoCorruptoException e) {
        
    }
    cardLayout.show(contenedorTarjetas, TARJETA_PERFIL);
    }
    @Override
    public void mostrarEditarPerfil(){};
    
    @Override
    public void mostrarBuscar() { 
        cardLayout.show(contenedorTarjetas, TARJETA_BUSCAR);
    }

    @Override
    public void mostrarInbox() { 
        cardLayout.show(contenedorTarjetas, TARJETA_INBOX);
    }

    @Override
    public void mostrarNotificaciones() { }

    @Override
    public void mostrarPublicar() { 
        try{
        Frame ventanaPublicar=(Frame) SwingUtilities.getWindowAncestor(this);
        PanelPublicar p1=new PanelPublicar(ventanaPublicar);
        p1.setVisible(true);
        
        }catch(ArchivoCorruptoException e){
           JOptionPane.showMessageDialog(this, 
            "Error al abrir la ventana de publicación: archivo de datos dañado.", 
            "Error", 
            JOptionPane.ERROR_MESSAGE); 
        }
    }

    @Override
    public void cerrarSesion() { 
        SesionActual.getInstancia().cerrarSesion();
        
        if (this.navegador != null) {
            this.navegador.cerrarSesion();
    }
    }
    public void recargarSesion() {
        this.removeAll(); 

        cardLayout = new CardLayout();
        contenedorTarjetas = new JPanel(cardLayout);

        try {
          
            panelTimeline = new PanelTimeline(this);
            panelPerfil = new PanelPerfil(this, SesionActual.getInstancia().getUserActual().getUser());
            panelBuscar = new PanelBuscar(this);
            panelInbox = new PanelInbox(this);

            contenedorTarjetas.add(panelTimeline, TARJETA_TIMELINE);
            contenedorTarjetas.add(panelPerfil, TARJETA_PERFIL);
            contenedorTarjetas.add(panelBuscar, TARJETA_BUSCAR);
            contenedorTarjetas.add(panelInbox, TARJETA_INBOX);

            
            add(new PanelSidebar(this), BorderLayout.WEST);
            add(contenedorTarjetas, BorderLayout.CENTER);

            cardLayout.show(contenedorTarjetas, TARJETA_TIMELINE);

        } catch (ArchivoCorruptoException e) {
            add(new JLabel("No se pudo iniciar la aplicación: archivo de datos dañado."), BorderLayout.CENTER);
        }

       
        this.revalidate();
        this.repaint();
    }

    @Override
    public void alIniciarSesion() {
        recargarSesion();
    }


    
}
