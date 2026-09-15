/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta.PanelesInsta;

import ConfigInsta.ServicioArchivoInsta;
import Excepciones.ArchivoCorruptoException;
import Insta.NavegarInsta;
import Insta.Notificacion;
import Insta.SesionActual;
import Servidor.ClienteInsta;
import base.ListaEnlazada;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

/**
 *
 * @author vasqu
 */
public class PanelNotificaciones extends JPanel {
    private NavegarInsta navegador;
    private JPanel contenedorNotificaciones;

    public PanelNotificaciones(NavegarInsta navegador) {
        this.navegador = navegador;
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);

        inicializarInterfaz();
        cargarHistorialNotificaciones();
        conectarConServidor();
    }
    
    private void inicializarInterfaz() {
       
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        JLabel lblTitulo = new JLabel("Notificaciones");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.add(lblTitulo);
        contenedorNotificaciones = new JPanel();
        contenedorNotificaciones.setLayout(new BoxLayout(contenedorNotificaciones, BoxLayout.Y_AXIS));
        contenedorNotificaciones.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(contenedorNotificaciones);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUI(new BasicScrollBar());
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(header, BorderLayout.NORTH);
        this.add(scroll, BorderLayout.CENTER);
    }
    
    public void cargarHistorialNotificaciones() {
        contenedorNotificaciones.removeAll();
        String usuarioActual = SesionActual.getInstancia().getUserActual().getUser();

        try {
            ListaEnlazada<Notificacion> historial = ServicioArchivoInsta.obtenerNotificaciones(usuarioActual);

            if (historial == null || historial.length() == 0) {
                mostrarMensajeVacio();
            } else {
                // Iterar en orden inverso para mostrar las más recientes arriba
                for (int i = historial.length() - 1; i >= 0; i--) {
                    Notificacion notif = historial.obtenerEn(i);
                    agregarFilaNotificacion(notif, false);
                }
            }
        } catch (ArchivoCorruptoException e) {
            mostrarMensajeVacio();
        }

        contenedorNotificaciones.revalidate();
        contenedorNotificaciones.repaint();
    }
    
    private void conectarConServidor() {
        try {
            ClienteInsta.getInstancia().registrarListenerNotificaciones(notif -> {
               
                if (contenedorNotificaciones.getComponentCount() == 1 && 
                    contenedorNotificaciones.getComponent(0) instanceof JPanel) {
                    JPanel primero = (JPanel) contenedorNotificaciones.getComponent(0);
                    if (primero.getComponentCount() > 0 && primero.getComponent(0) instanceof JLabel) {
                        JLabel lbl = (JLabel) primero.getComponent(0);
                        if ("No tienes notificaciones aún.".equals(lbl.getText())) {
                            contenedorNotificaciones.removeAll();
                        }
                    }
                }

                
                agregarFilaNotificacion(notif, true);
            });
        } catch (Exception e) {
            System.err.println("Error al registrar listener de notificaciones: " + e.getMessage());
        }
}
private void agregarFilaNotificacion(Notificacion notif, boolean alInicio) {
        JPanel fila = new JPanel(new BorderLayout(15, 0));
        fila.setBackground(Color.WHITE);
        fila.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        fila.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

       
       try {
            Insta.UsuarioInsta emisorObj = ServicioArchivoInsta.buscarUsuario(notif.getEmisor());

            if (emisorObj != null) {
                AvatarCircular avatar = AvatarCircular.crear(notif.getEmisor(), 44);
                fila.add(avatar, BorderLayout.WEST);
            } else {
               
                JLabel lblAvatarDefault = new JLabel("👤", SwingConstants.CENTER);
                lblAvatarDefault.setFont(new Font("SansSerif", Font.PLAIN, 24));
                lblAvatarDefault.setPreferredSize(new Dimension(44, 44));
                fila.add(lblAvatarDefault, BorderLayout.WEST);
            }
        } catch (Exception e) {
            JLabel lblAvatarDefault = new JLabel("👤", SwingConstants.CENTER);
            lblAvatarDefault.setFont(new Font("SansSerif", Font.PLAIN, 24));
            lblAvatarDefault.setPreferredSize(new Dimension(44, 44));
            fila.add(lblAvatarDefault, BorderLayout.WEST);
        }

        
        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setBackground(Color.WHITE);

        JLabel lblMensaje = new JLabel("<html><b>" + notif.getEmisor() + "</b> " + obtenerMensajeSegunTipo(notif) + "</html>");
        lblMensaje.setFont(new Font("SansSerif", Font.PLAIN, 13));

        String fechaFormateada = notif.getFecha() != null 
                ? notif.getFecha().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")) 
                : "Ahora";
        
        JLabel lblFecha = new JLabel(fechaFormateada);
        lblFecha.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblFecha.setForeground(Color.GRAY);

        panelTexto.add(lblMensaje);
        panelTexto.add(Box.createVerticalStrut(3));
        panelTexto.add(lblFecha);

        fila.add(panelTexto, BorderLayout.CENTER);

     
        JLabel lblIconoTipo = new JLabel(obtenerIconoTipo(notif.getTipo()));
        lblIconoTipo.setFont(new Font("SansSerif", Font.PLAIN, 18));
        fila.add(lblIconoTipo, BorderLayout.EAST);

       
        fila.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (navegador != null) {
                    navegador.mostrarPerfil(notif.getEmisor());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                fila.setBackground(new Color(248, 248, 248));
                panelTexto.setBackground(new Color(248, 248, 248));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                fila.setBackground(Color.WHITE);
                panelTexto.setBackground(Color.WHITE);
            }
        });

        
        if (alInicio) {
            contenedorNotificaciones.add(fila, 0);
        } else {
            contenedorNotificaciones.add(fila);
        }

        contenedorNotificaciones.revalidate();
        contenedorNotificaciones.repaint();
    }

private String obtenerMensajeSegunTipo(Notificacion notif) {
        switch (notif.getTipo()) {
            case LIKE:
                return "le dio me gusta a tu publicación.";
            case COMENTARIO:
                return "comentó tu publicación.";
            case SEGUIDOR:
                return "comenzó a seguirte.";
            case MENCION:
                return "te mencionó en una publicación o comentario.";
            default:
                return notif.getMensaje();
        }
    }

private String obtenerIconoTipo(Notificacion.TipoNotificacion tipo) {
        if (tipo == null) return "🔔";
        switch (tipo) {
            case LIKE:
                return "❤️";
            case COMENTARIO:
                return "💬";
            case SEGUIDOR:
                return "👤";
            case MENCION:
                return "🏷️";
            default:
                return "🔔";
        }
    }

    private void mostrarMensajeVacio() {
        JPanel panelVacio = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 50));
        panelVacio.setBackground(Color.WHITE);

        JLabel lblVacio = new JLabel("No tienes notificaciones aún.", SwingConstants.CENTER);
        lblVacio.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblVacio.setForeground(Color.GRAY);

        panelVacio.add(lblVacio);
        contenedorNotificaciones.add(panelVacio);
    }
}
