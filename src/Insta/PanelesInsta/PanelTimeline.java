/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta.PanelesInsta;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import Insta.NavegarInsta;
import javax.swing.BoxLayout;
import Insta.SesionActual;
import Insta.Publicacion;
import ConfigInsta.ServicioArchivoInsta;
import Excepciones.ArchivoCorruptoException;
import Insta.UsuarioInsta;
import base.ListaEnlazada;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
/**
 *
 * @author vasqu
 */
public class PanelTimeline extends JPanel{
    private JPanel contenedorPublicaciones;
    private JPanel panelSugerencias;
    private NavegarInsta navegador;

    public PanelTimeline(NavegarInsta navegador) {
       this.navegador=navegador;
       setLayout(new BorderLayout());
       setBackground(Color.WHITE);
       
        contenedorPublicaciones = new JPanel();
        contenedorPublicaciones.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenedorPublicaciones.setLayout(new BoxLayout(contenedorPublicaciones, BoxLayout.Y_AXIS));
        contenedorPublicaciones.setBackground(Color.WHITE);
        
        JScrollPane scrollFeed = new JScrollPane(contenedorPublicaciones);
        scrollFeed.setBorder(null);
        scrollFeed.getViewport().setBackground(Color.WHITE);
        scrollFeed.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollFeed.getVerticalScrollBar().setUI(new BasicScrollBar());
        scrollFeed.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0)); // Delgado
        scrollFeed.getVerticalScrollBar().setUnitIncrement(16);
        scrollFeed.setHorizontalScrollBar(null);
        scrollFeed.setPreferredSize(new Dimension(480, 10));
        
        panelSugerencias = new JPanel();
        panelSugerencias.setLayout(new BoxLayout(panelSugerencias, BoxLayout.Y_AXIS));
        panelSugerencias.setBackground(Color.WHITE);
        
        panelSugerencias.setPreferredSize(new Dimension(350, 0));
       javax.swing.border.Border bordeIzquierdo = BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(230, 230, 230));


        javax.swing.border.Border padding = BorderFactory.createEmptyBorder(20, 24, 20, 20); 


        panelSugerencias.setBorder(BorderFactory.createCompoundBorder(bordeIzquierdo, padding));
        add(scrollFeed, BorderLayout.LINE_START);
        
        add(panelSugerencias, BorderLayout.EAST);
        
        try {
            refrescar();
        } catch (ArchivoCorruptoException e) {
            e.printStackTrace();
            contenedorPublicaciones.add(new JLabel("No se pudo cargar el timeline."));
        }
    }
    
    public void refrescar() throws ArchivoCorruptoException{
        
        if (SesionActual.getInstancia().getUserActual() == null) {
        return; 
    }
        
        contenedorPublicaciones.removeAll();
        panelSugerencias.removeAll();
        String username=SesionActual.getInstancia().getUserActual().getUser();
        ListaEnlazada<Publicacion>publicaciones=new ServicioArchivoInsta().obtenerTimeline(username);
        
        for (int i = 0; i < publicaciones.length(); i++) {
            Publicacion p=publicaciones.obtenerEn(i);
            contenedorPublicaciones.add(new PanelPublicacion(p,navegador));
            contenedorPublicaciones.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        construirBarraLateral(username);
        
        contenedorPublicaciones.revalidate();
        contenedorPublicaciones.repaint();
        panelSugerencias.revalidate();
        panelSugerencias.repaint();
    }
    
    private void construirBarraLateral(String miUsername) throws ArchivoCorruptoException {
       
        JPanel filaPerfilPropio = new JPanel(new BorderLayout(10, 0));
        filaPerfilPropio.setBackground(Color.WHITE);
        filaPerfilPropio.setMaximumSize(new Dimension(300, 50));
        filaPerfilPropio.setAlignmentX(Component.LEFT_ALIGNMENT);

        AvatarCircular avatarMiUser = AvatarCircular.crear(miUsername, 48);

        JPanel infoUser = new JPanel();
        infoUser.setLayout(new BoxLayout(infoUser, BoxLayout.Y_AXIS));
        infoUser.setBackground(Color.WHITE);

        JLabel lblMiUser = new JLabel(miUsername);
        lblMiUser.addMouseListener(new MouseAdapter (){
            public void mouseClicked(MouseEvent e){
                navegador.mostrarPerfil(miUsername);
            }
        });
        
        lblMiUser.setFont(lblMiUser.getFont().deriveFont(Font.BOLD, 13f));
        lblMiUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMiUser.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lblMiNombre = new JLabel("Tú");
        lblMiNombre.setFont(lblMiNombre.getFont().deriveFont(12f));
        lblMiNombre.setForeground(new Color(142, 142, 142));
        lblMiNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoUser.add(Box.createVerticalGlue());
        infoUser.add(lblMiUser);
        infoUser.add(Box.createVerticalStrut(2));
        infoUser.add(lblMiNombre);
        infoUser.add(Box.createVerticalGlue());

        JLabel btnCambiar = new JLabel("Editar Perfil");
        btnCambiar.setFont(btnCambiar.getFont().deriveFont(Font.BOLD, 12f));
        btnCambiar.setForeground(new Color(0, 149, 246)); 
        btnCambiar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        filaPerfilPropio.add(avatarMiUser, BorderLayout.WEST);
        filaPerfilPropio.add(infoUser, BorderLayout.CENTER);
        filaPerfilPropio.add(btnCambiar, BorderLayout.EAST);

        panelSugerencias.add(filaPerfilPropio);
        panelSugerencias.add(Box.createVerticalStrut(24));

       
        JPanel encabezadoSugerencias = new JPanel(new BorderLayout());
        encabezadoSugerencias.setBackground(Color.WHITE);
        encabezadoSugerencias.setMaximumSize(new Dimension(300, 20));
        encabezadoSugerencias.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTituloSug = new JLabel("Sugerencias para ti");
        lblTituloSug.setFont(lblTituloSug.getFont().deriveFont(Font.BOLD, 14f));
        lblTituloSug.setForeground(new Color(142, 142, 142));

        JLabel btnVerTodos = new JLabel("Ver todos");
        btnVerTodos.setFont(btnVerTodos.getFont().deriveFont(Font.BOLD, 12f));
        btnVerTodos.setForeground(new Color(38, 38, 38));
        btnVerTodos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        encabezadoSugerencias.add(lblTituloSug, BorderLayout.WEST);
        encabezadoSugerencias.add(btnVerTodos, BorderLayout.EAST);

        panelSugerencias.add(encabezadoSugerencias);
        panelSugerencias.add(Box.createVerticalStrut(16));

       
        ListaEnlazada<UsuarioInsta> sugeridos = ServicioArchivoInsta.obtenerUsuariosSugeridos(5);
        for (int i = 0; i < sugeridos.length(); i++) {
            UsuarioInsta u = sugeridos.obtenerEn(i);
            
            if (!u.getUser().equals(miUsername)) {
                panelSugerencias.add(construirFilaSugerida(u));
                panelSugerencias.add(Box.createVerticalStrut(12));
            }
        }

        panelSugerencias.add(Box.createVerticalGlue());
    }
    
    private JPanel construirFilaSugerida(UsuarioInsta usuario) throws ArchivoCorruptoException {
        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setBackground(Color.WHITE);
        fila.setMaximumSize(new Dimension(300, 48));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);

        AvatarCircular avatar = AvatarCircular.crear(usuario.getUser(), 40);

        JPanel panelTextos = new JPanel();
        panelTextos.setLayout(new BoxLayout(panelTextos, BoxLayout.Y_AXIS));
        panelTextos.setBackground(Color.WHITE);

        JLabel lblUser = new JLabel(usuario.getUser());
        lblUser.setFont(lblUser.getFont().deriveFont(Font.BOLD, 13f));
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);


        panelTextos.add(Box.createVerticalGlue());
        panelTextos.add(lblUser);
        panelTextos.add(Box.createVerticalStrut(2));
        panelTextos.add(Box.createVerticalGlue());

       
        JLabel btnSeguir = new JLabel("Seguir");
        btnSeguir.setFont(btnSeguir.getFont().deriveFont(Font.BOLD, 12f));
        btnSeguir.setForeground(new Color(0, 149, 246));
        btnSeguir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnSeguir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (btnSeguir.getText().equals("Seguir")) {
                    //server
                    btnSeguir.setText("Siguiendo");
                    btnSeguir.setForeground(Color.BLACK);
                } else {
                    btnSeguir.setText("Seguir");
                    btnSeguir.setForeground(new Color(0, 149, 246));
                }
            }
        });

       
        fila.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        fila.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!btnSeguir.getBounds().contains(e.getPoint())) {
                    navegador.mostrarPerfil(usuario.getUser());
                }
            }
        });

        fila.add(avatar, BorderLayout.WEST);
        fila.add(panelTextos, BorderLayout.CENTER);
        fila.add(btnSeguir, BorderLayout.EAST);

        return fila;
    }
    
}
