/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta.PanelesInsta;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import Insta.NavegarInsta;
import Insta.SesionActual;
/**
 *
 * @author vasqu
 */
public class JPanelPrincipal extends JPanel implements NavegarInsta{
    private CardLayout cardLayout;
    private PanelTimeline timeline;
    private PanelApp panelApp;
    private static final String TARJETA_TIMELINE = "timeline";
    private static final String TARJETA_PRINCIPAL = "panelApp";

    public JPanelPrincipal() {
        cardLayout =new CardLayout();
        
        this.setPreferredSize(new Dimension(800, 600));
        this.setLayout(cardLayout);
        
        PanelLogin panelLogin = new PanelLogin(card -> mostrar(card));
        PanelRegistro panelregistro = new PanelRegistro(card -> mostrar(card));
        timeline = new PanelTimeline(this);
        panelApp=new PanelApp(this);
        
         this.add(panelLogin, "login");
         this.add(panelregistro,"registro");
         this.add(timeline, TARJETA_TIMELINE);
         this.add(panelApp, TARJETA_PRINCIPAL);
         
        
    }
    public void mostrar(String nombreCard){
        if (TARJETA_PRINCIPAL.equals(nombreCard)) {
            panelApp.recargarSesion();
        }
        cardLayout.show(this, nombreCard);
    }

    @Override
    public void mostrarTimeline() {
        cardLayout.show(this, TARJETA_PRINCIPAL);
        
       
    }

    @Override
    public void mostrarBuscar() {
        
    }

    @Override
    public void mostrarInbox() {
       
    }

    @Override
    public void mostrarNotificaciones() {
        
    }

    @Override
    public void mostrarPublicar() {
        
    }

    @Override
    public void mostrarPerfil(String username) {
        
    }

    @Override
    public void mostrarEditarPerfil() {
       
    }

    @Override
    public void cerrarSesion() {
        
        cardLayout.show(this, "login");
        
    }

    @Override
    public void alIniciarSesion() {
       panelApp.recargarSesion();
       cardLayout.show(this, TARJETA_PRINCIPAL);
    }

   
    
}
