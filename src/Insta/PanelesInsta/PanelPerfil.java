/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta.PanelesInsta;

import ConfigInsta.Rutas;
import ConfigInsta.ServicioArchivoInsta;
import ConfigInsta.UtilImagen;
import Excepciones.ArchivoCorruptoException;
import Insta.NavegarInsta;
import Insta.Publicacion;
import Insta.SesionActual;
import base.ArchivoUtil;
import base.ListaEnlazada;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.nio.file.Paths;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author vasqu
 */
public class PanelPerfil extends JPanel{
    private NavegarInsta navegador;
    private String usernameMostrado;
    private JPanel panelContenido;

    public PanelPerfil(NavegarInsta navegador, String username) {
        this.navegador = navegador;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(panelContenido);
        scrollPane.setBorder(null);
        
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBar());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0)); // Delgado
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBar(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);

        try {
            cargarPerfil(username);
        } catch (ArchivoCorruptoException e) {
            panelContenido.add(new JLabel("No se pudo cargar el perfil."));
        }
    }

    public void cargarPerfil(String username) throws ArchivoCorruptoException {
        this.usernameMostrado = username;
        panelContenido.removeAll();

        panelContenido.add(construirEncabezado());
        panelContenido.add(construirCuadricula());

        revalidate();
        repaint();
    }

    private JPanel construirEncabezado() throws ArchivoCorruptoException {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 30, 24));

        AvatarCircular avatar = AvatarCircular.crear(usernameMostrado, 160);
        panel.add(avatar);
        panel.add(Box.createHorizontalStrut(30));

        JPanel columnaDerecha = new JPanel();
        columnaDerecha.setLayout(new BoxLayout(columnaDerecha, BoxLayout.Y_AXIS));
        columnaDerecha.setBackground(Color.WHITE);
        columnaDerecha.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nombreLabel = new JLabel(usernameMostrado);
        nombreLabel.setFont(nombreLabel.getFont().deriveFont(Font.BOLD, 20f));
        nombreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        columnaDerecha.add(nombreLabel);
        columnaDerecha.add(Box.createVerticalStrut(12));

        ListaEnlazada<Publicacion> publicaciones = ArchivoUtil.leerLista(Rutas.rutaInsta(usernameMostrado));
        ListaEnlazada<String> seguidores = ServicioArchivoInsta.obtenerSeguidores(usernameMostrado);
        ListaEnlazada<String> seguidos = ServicioArchivoInsta.obtenerSeguidos(usernameMostrado);

        JLabel contadores = new JLabel(
            "<html><body style='white-space: nowrap; margin: 0; padding: 0;'>" +
            "<b>" + publicaciones.length() + "</b> <span style='font-weight: normal; color: black;'>publicaciones</span>" +
            "&nbsp;&nbsp;&nbsp;&nbsp;" +
            "<b>" + seguidores.length() + "</b> <span style='font-weight: normal; color: black;'>seguidores</span>" +
            "&nbsp;&nbsp;&nbsp;&nbsp;" +
            "<b>" + seguidos.length() + "</b> <span style='font-weight: normal; color: black;'>seguidos</span>" +
            "</body></html>"
        );
        contadores.setFont(contadores.getFont().deriveFont(14f));
        contadores.setAlignmentX(Component.LEFT_ALIGNMENT);
        columnaDerecha.add(contadores);
        columnaDerecha.add(Box.createVerticalStrut(16));

        columnaDerecha.add(construirBotonAccion(seguidores));

        panel.add(columnaDerecha);

        Dimension pref = panel.getPreferredSize();
        panel.setMaximumSize(new Dimension(pref.width, pref.height));

        return panel;
    }

    private JButton construirBotonAccion(ListaEnlazada<String> seguidores) {
        String usuarioActual = SesionActual.getInstancia().getUserActual().getUser();
        boolean esMiPerfil = usuarioActual.equals(usernameMostrado);

        if (esMiPerfil) {
            JButton boton = new JButton("Editar perfil");
            boton.setAlignmentX(Component.LEFT_ALIGNMENT);
            boton.setForeground(Color.GRAY);

            boton.setBackground(new Color(230, 230, 230));
            boton.setForeground(Color.BLACK);
            boton.setOpaque(true);
            boton.setBorderPainted(false);
            boton.setFocusPainted(false);
            boton.addActionListener(e -> navegador.mostrarEditarPerfil());
            return boton;
        }
        boolean yaLoSigo = seguidores.contiene(usuarioActual);
        JButton boton = new JButton(yaLoSigo ? "Dejar de seguir" : "Seguir");
        boton.setBackground(new Color(0, 149, 246));
        boton.setForeground(Color.WHITE);
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.addActionListener(e -> {
            if (yaLoSigo) {
                seguidores.eliminar(usuarioActual);
            } else {
                seguidores.insertarFinal(usuarioActual);
            }
            try {
                cargarPerfil(usernameMostrado);
            } catch (ArchivoCorruptoException ex) { }
        });
        return boton;
    }

    private JPanel construirCuadricula() throws ArchivoCorruptoException {
        ListaEnlazada<Publicacion> publicaciones = ArchivoUtil.leerLista(Rutas.rutaInsta(usernameMostrado));

        if (publicaciones.estaVacia()) {
            JPanel vacio = new JPanel();
            vacio.setBackground(Color.WHITE);
            vacio.add(new JLabel("Sin publicaciones"));
            return vacio;
        }

        JPanel grid = new JPanel(new GridLayout(0, 3, 4, 4));
        grid.setBackground(Color.WHITE);

        for (int i = 0; i < publicaciones.length(); i++) {
            Publicacion p = publicaciones.obtenerEn(i);
            String rutaCompleta = Paths.get(Rutas.rutaImagenes(usernameMostrado), p.getRutaImagen()).toString();
            ImageIcon icono = new ImageIcon(rutaCompleta);
            Image escalada = UtilImagen.escalarAlta(icono.getImage(), 200, 200);
            
            JLabel lblFoto = new JLabel(new ImageIcon(escalada));
            lblFoto.setPreferredSize(new Dimension(200, 200));
            grid.add(lblFoto);
        }

       
        JPanel contenedorGrid = new JPanel(new BorderLayout());
        contenedorGrid.setBackground(Color.WHITE);
        contenedorGrid.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenedorGrid.add(grid, BorderLayout.CENTER);

      
        int filas = (int) Math.ceil((double) publicaciones.length() / 3.0);
        int altoTotal = filas * 204; 
        
        Dimension dimGenerada = new Dimension(608, altoTotal);
        contenedorGrid.setPreferredSize(dimGenerada);
        contenedorGrid.setMaximumSize(dimGenerada);

        return contenedorGrid;
    }}
