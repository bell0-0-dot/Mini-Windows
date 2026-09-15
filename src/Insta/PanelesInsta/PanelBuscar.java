/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta.PanelesInsta;

import ConfigInsta.ServicioArchivoInsta;
import Excepciones.ArchivoCorruptoException;
import Insta.NavegarInsta;
import Insta.Publicacion;
import Insta.UsuarioInsta;
import base.ListaEnlazada;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author vasqu
 */
public class PanelBuscar  extends JPanel{
    
    private NavegarInsta navegador;
    private JTextField campoBusqueda;
    private JPanel contenedorResultados;
    
     public PanelBuscar(NavegarInsta navegador) throws ArchivoCorruptoException{
        this.navegador = navegador;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        
        JPanel columnaCentral = new JPanel();
        columnaCentral.setLayout(new BoxLayout(columnaCentral, BoxLayout.Y_AXIS));
        columnaCentral.setBackground(Color.WHITE);
        columnaCentral.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

       
        campoBusqueda = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        campoBusqueda.setOpaque(false);
        campoBusqueda.setBackground(new Color(245, 245, 245));
        campoBusqueda.setFont(campoBusqueda.getFont().deriveFont(14f));
        campoBusqueda.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        campoBusqueda.setAlignmentX(Component.CENTER_ALIGNMENT);
        campoBusqueda.setMaximumSize(new Dimension(600, 40));
        campoBusqueda.setPreferredSize(new Dimension(600, 44));
        columnaCentral.add(campoBusqueda);
        columnaCentral.add(Box.createVerticalStrut(24));

        
        columnaCentral.add(construirEncabezadoSeccion("Recientes"));
        columnaCentral.add(Box.createVerticalStrut(12));

       
        contenedorResultados = new JPanel();
        contenedorResultados.setLayout(new BoxLayout(contenedorResultados, BoxLayout.Y_AXIS));
        contenedorResultados.setBackground(Color.WHITE);
        contenedorResultados.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenedorResultados.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        columnaCentral.add(contenedorResultados);
        columnaCentral.add(Box.createVerticalGlue());

        
        JPanel contenedorExterno = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        contenedorExterno.setBackground(Color.WHITE);
        contenedorExterno.add(columnaCentral);

        JScrollPane scroll = new JScrollPane(contenedorExterno);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        
        campoBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { buscar(); }
            @Override
            public void removeUpdate(DocumentEvent e) { buscar(); }
            @Override
            public void changedUpdate(DocumentEvent e) { buscar(); }
        });

        mostrarSugeridos();
    }
     
     private JPanel construirEncabezadoSeccion(String titulo) {
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Color.WHITE);
        encabezado.setMaximumSize(new Dimension(420, 25));
        encabezado.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 15f));

        JLabel lblAccion = new JLabel("Borrar todo");
        lblAccion.setFont(lblAccion.getFont().deriveFont(Font.BOLD, 13f));
        lblAccion.setForeground(new Color(0, 149, 246)); 
        lblAccion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblAccion.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            contenedorResultados.removeAll();
            contenedorResultados.revalidate();
            contenedorResultados.repaint();
            }
        });
        
        
        encabezado.add(lblTitulo, BorderLayout.WEST);
        encabezado.add(lblAccion, BorderLayout.EAST);
        return encabezado;
    }
     
     private void buscar() {
        String texto = campoBusqueda.getText().trim();
        contenedorResultados.removeAll();

        try {
            if (texto.isEmpty()) {
                mostrarSugeridos();
            } else if (texto.startsWith("#")) {
                String tagLimpio = texto.substring(1).toLowerCase();
                ListaEnlazada<Publicacion> publicaciones = ServicioArchivoInsta.buscarPorHashtag(tagLimpio);
                
                if (publicaciones.estaVacia()) {
                JLabel lblVacio = new JLabel("No se encontraron publicaciones con " + texto);
                lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
                lblVacio.setForeground(new Color(142, 142, 142));
                contenedorResultados.add(lblVacio);
            } else {
               
                JPanel panelGrilla = new JPanel(new GridLayout(0, 3, 4, 4));
                panelGrilla.setBackground(Color.WHITE);
                panelGrilla.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
                panelGrilla.setAlignmentX(Component.CENTER_ALIGNMENT);

                for (int i = 0; i < publicaciones.length(); i++) {
                    Publicacion p = publicaciones.obtenerEn(i);
                    panelGrilla.add(construirMiniaturaGrid(p));
                }
                
                contenedorResultados.add(panelGrilla);
            }
            } else {
                ListaEnlazada<UsuarioInsta> usuarios = ServicioArchivoInsta.buscarUsuariosParcial(texto);
                for (int i = 0; i < usuarios.length(); i++) {
                    contenedorResultados.add(construirFilaUsuario(usuarios.obtenerEn(i)));
                }
            }
        } catch (ArchivoCorruptoException e) {
            contenedorResultados.add(new JLabel("No se pudo completar la búsqueda."));
        }

        contenedorResultados.revalidate();
        contenedorResultados.repaint();
    }
     
      private void mostrarSugeridos() {
        try {
            ListaEnlazada<UsuarioInsta> sugeridos = ServicioArchivoInsta.obtenerUsuariosSugeridos(8);
            for (int i = 0; i < sugeridos.length(); i++) {
                contenedorResultados.add(construirFilaUsuario(sugeridos.obtenerEn(i)));
            }
        } catch (ArchivoCorruptoException e) {
            contenedorResultados.add(new JLabel("No se pudieron cargar sugerencias."));
        }
    }
     
       private JPanel construirFilaUsuario(UsuarioInsta usuario) throws ArchivoCorruptoException {
        JPanel fila = new JPanel(new BorderLayout(12, 0));
        fila.setBackground(Color.WHITE);
        fila.setAlignmentX(Component.CENTER_ALIGNMENT);
        fila.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        fila.setMaximumSize(new Dimension(420, 60));

        
        AvatarCircular avatar = AvatarCircular.crear(usuario.getUser(), 44);

        
        JPanel panelTextos = new JPanel();
        panelTextos.setLayout(new BoxLayout(panelTextos, BoxLayout.Y_AXIS));
        panelTextos.setBackground(Color.WHITE);

        JLabel lblUser = new JLabel(usuario.getUser());
        lblUser.setFont(lblUser.getFont().deriveFont(Font.BOLD, 13f));
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        
        JLabel lblDetalle = new JLabel(usuario.getNombre() != null ? usuario.getNombre(): "Sugerencia para ti");
        lblDetalle.setFont(lblDetalle.getFont().deriveFont(12f));
        lblDetalle.setForeground(new Color(142, 142, 142));
        lblDetalle.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelTextos.add(Box.createVerticalGlue());
        panelTextos.add(lblUser);
        panelTextos.add(Box.createVerticalStrut(2));
        panelTextos.add(lblDetalle);
        panelTextos.add(Box.createVerticalGlue());

        
        JLabel btnEliminar = new JLabel("✕");
        btnEliminar.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            contenedorResultados.remove(fila); 
            contenedorResultados.revalidate(); 
            contenedorResultados.repaint();    
        }
        });
        
      
        fila.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!btnEliminar.getBounds().contains(e.getPoint())) {
                    navegador.mostrarPerfil(usuario.getUser());
                }
            }
        });  
        btnEliminar.setFont(btnEliminar.getFont().deriveFont(14f));
        btnEliminar.setForeground(new Color(142, 142, 142));
        btnEliminar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        fila.add(avatar, BorderLayout.WEST);
        fila.add(panelTextos, BorderLayout.CENTER);
        fila.add(btnEliminar, BorderLayout.EAST);

      
        fila.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        fila.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
              
                if (!btnEliminar.getBounds().contains(e.getPoint())) {
                    navegador.mostrarPerfil(usuario.getUser());
                }
            }
        });

        return fila;
    }
      private JPanel construirMiniaturaGrid(Publicacion p) {
        JPanel panelImagen = new JPanel(new BorderLayout());
        panelImagen.setPreferredSize(new Dimension(190, 190)); 
        panelImagen.setBackground(new Color(235, 235, 235));
        panelImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

      
        JLabel lblFoto = new JLabel();
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        String nombreArchivo = p.getRutaImagen();
        
        if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
            String carpetaImagenes = ConfigInsta.Rutas.rutaImagenes(p.getAutor());

           
            File archivoFoto = new File(carpetaImagenes, nombreArchivo);

            if (archivoFoto.exists()) {
                ImageIcon icono = new ImageIcon(archivoFoto.getAbsolutePath());
                Image img = icono.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(img));
            } else {
                lblFoto.setText("📷 (No encontrada)");
                lblFoto.setFont(lblFoto.getFont().deriveFont(11f));
                lblFoto.setForeground(Color.GRAY);
            }
        } else {
            lblFoto.setText("📷");
        }

        panelImagen.add(lblFoto, BorderLayout.CENTER);

        
        panelImagen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                navegador.mostrarPerfil(p.getAutor());
            }
        });

        return panelImagen;
    } 

}