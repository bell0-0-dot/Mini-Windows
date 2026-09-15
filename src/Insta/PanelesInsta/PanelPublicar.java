 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta.PanelesInsta;

import ConfigInsta.Rutas;
import ConfigInsta.UtilImagen;
import Excepciones.ArchivoCorruptoException;
import Insta.Publicacion;
import Insta.SesionActual;
import Servidor.ClienteInsta;
import Servidor.PeticionRed;
import Servidor.RespuestaRed;
import base.ArchivoUtil;
import base.ListaEnlazada;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author vasqu
 */
public class PanelPublicar extends JDialog{
    private String usuarioActual;
    private JLabel lblImagen;
    private JTextArea txtDescripcion;
    private File archivoSeleccionado;
    private String stickerSeleccionado;
    private Frame parentFrame;
    private JLabel lblPreviewSticker;
    private JPanel panelCentroDerecho;

    public PanelPublicar(Frame parent) throws ArchivoCorruptoException {
        super(parent, true);
        this.parentFrame = parent;
        this.usuarioActual = SesionActual.getInstancia().getUserActual().getUser();
        setFocusable(true);
        setUndecorated(true);
        setSize(850, 520);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(218, 218, 218), 2));

        add(construirEncabezado(), BorderLayout.NORTH);

        JPanel contenedorCentral = new JPanel(new GridLayout(1, 2));
        contenedorCentral.setBackground(Color.WHITE);

        contenedorCentral.add(construirSeccionIzquierda());
        contenedorCentral.add(construirSeccionDerecha());

        add(contenedorCentral, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> {
            getRootPane().requestFocusInWindow();
        });
    }

    private JPanel construirEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(850, 45));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(218, 218, 218)));

        JLabel btnCerrar = new JLabel(" ✕ ", SwingConstants.CENTER);
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
                dispose();
            }
        });

        JLabel lblTitulo = new JLabel("Crear nueva publicación", SwingConstants.CENTER);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 15f));

        JLabel btnCompartir = new JLabel("Compartir  ");
        btnCompartir.setFont(btnCompartir.getFont().deriveFont(Font.BOLD, 14f));
        btnCompartir.setForeground(new Color(0, 149, 246));
        btnCompartir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCompartir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                publicar();
            }
        });

        panel.add(btnCerrar, BorderLayout.WEST);
        panel.add(lblTitulo, BorderLayout.CENTER);
        panel.add(btnCompartir, BorderLayout.EAST);

        return panel;
    }

    private JPanel construirSeccionIzquierda() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(250, 250, 250));

        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        lblImagen = new JLabel("Seleccionar imagen", SwingConstants.CENTER);
        lblImagen.setFont(lblImagen.getFont().deriveFont(13f));
        lblImagen.setForeground(Color.GRAY);
        lblImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblImagen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarImagen();
            }
        });

        panel.add(lblImagen, BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirSeccionDerecha() throws ArchivoCorruptoException {
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBackground(Color.WHITE);

        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        panelUsuario.setBackground(Color.WHITE);

        AvatarCircular avatar = AvatarCircular.crear(usuarioActual, 35);
        JLabel lblUser = new JLabel(usuarioActual);
        lblUser.setFont(lblUser.getFont().deriveFont(Font.BOLD, 13f));

        panelUsuario.add(avatar);
        panelUsuario.add(lblUser);

        panelCentroDerecho = new JPanel();
        panelCentroDerecho.setLayout(new BoxLayout(panelCentroDerecho, BoxLayout.Y_AXIS));
        panelCentroDerecho.setBackground(Color.WHITE);

        txtDescripcion = new JTextArea(3, 35);
        txtDescripcion.setText("Escribe una descripción...");
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBackground(Color.WHITE);
        txtDescripcion.setFont(txtDescripcion.getFont().deriveFont(13f));
        txtDescripcion.setMargin(new java.awt.Insets(10, 10, 5, 10));

        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setBorder(null);
        scrollDesc.getVerticalScrollBar().setUI(new BasicScrollBar());
        scrollDesc.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0)); 
        scrollDesc.getVerticalScrollBar().setUnitIncrement(16);
        scrollDesc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollDesc.setBackground(Color.WHITE);
        scrollDesc.getViewport().setBackground(Color.WHITE);
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

       
        Dimension dScroll = scrollDesc.getPreferredSize();
        scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, dScroll.height));

        lblPreviewSticker = new JLabel();
        lblPreviewSticker.setHorizontalAlignment(SwingConstants.LEFT);
        lblPreviewSticker.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPreviewSticker.setBorder(BorderFactory.createEmptyBorder(2, 10, 4, 0));

        JPanel panelPreviewSticker = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelPreviewSticker.setBackground(Color.WHITE);
        panelPreviewSticker.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton btnQuitarSticker = new JButton("✕");
        btnQuitarSticker.setName("btnQuitar");
        btnQuitarSticker.setFont(new Font("SansSerif", Font.BOLD, 10));
        btnQuitarSticker.setForeground(Color.GRAY);
        btnQuitarSticker.setBorderPainted(false);
        btnQuitarSticker.setContentAreaFilled(false);
        btnQuitarSticker.setFocusPainted(false);
        btnQuitarSticker.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnQuitarSticker.setVisible(false); 

        btnQuitarSticker.addActionListener(e -> {
            this.stickerSeleccionado = null;
            lblPreviewSticker.setIcon(null);
            btnQuitarSticker.setVisible(false);
            panelCentroDerecho.revalidate();
            panelCentroDerecho.repaint();
        });

        panelPreviewSticker.add(lblPreviewSticker);
        panelPreviewSticker.add(btnQuitarSticker);
        
        
        txtDescripcion.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void actualizar() {
                Dimension d = scrollDesc.getPreferredSize();
                scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, d.height));
                panelCentroDerecho.revalidate();
                panelCentroDerecho.repaint();
            }
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
        });

        txtDescripcion.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtDescripcion.getText().equals("Escribe una descripción...")) {
                    txtDescripcion.setText("");
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtDescripcion.getText().trim().isEmpty()) {
                    txtDescripcion.setText("Escribe una descripción...");
                }
            }
        });

        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));
        panelInferior.setBackground(Color.WHITE);
        panelInferior.setAlignmentX(Component.LEFT_ALIGNMENT);

       
        panelCentroDerecho.add(scrollDesc);
        panelCentroDerecho.add(panelPreviewSticker);
        panelCentroDerecho.add(panelInferior);
        panelCentroDerecho.add(Box.createVerticalGlue()); 

        JPanel panelStickersContenedor = new JPanel(new BorderLayout());
        panelStickersContenedor.setBackground(Color.WHITE);
        panelStickersContenedor.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(8, 0, 0, 0)
        ));

        JLabel lblTituloStickers = new JLabel(" Stickers disponibles");
        lblTituloStickers.setFont(lblTituloStickers.getFont().deriveFont(Font.BOLD, 12f));
        lblTituloStickers.setForeground(Color.GRAY);

        JPanel panelGaleriaStickers = new JPanel(new GridLayout(0, 4, 8, 8));
        panelGaleriaStickers.setBackground(Color.WHITE);
        cargarGaleriaStickers(panelGaleriaStickers);

        JScrollPane scrollStickers = new JScrollPane(panelGaleriaStickers);
        scrollStickers.getVerticalScrollBar().setUI(new BasicScrollBar());
        scrollStickers.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0)); 
        scrollStickers.setHorizontalScrollBar(null);
        scrollStickers.setBorder(null);
        scrollStickers.getVerticalScrollBar().setUnitIncrement(10);

        panelStickersContenedor.add(lblTituloStickers, BorderLayout.NORTH);
        panelStickersContenedor.add(scrollStickers, BorderLayout.CENTER);

        panelDerecho.add(panelUsuario, BorderLayout.NORTH);
        panelDerecho.add(panelCentroDerecho, BorderLayout.CENTER);
        panelDerecho.add(panelStickersContenedor, BorderLayout.SOUTH);

        panelStickersContenedor.setPreferredSize(new Dimension(350, 230));

        return panelDerecho;
    }

    private void cargarGaleriaStickers(JPanel panelGaleria) {
        panelGaleria.removeAll();
        
        JLabel btnAgregar = new JLabel("+", SwingConstants.CENTER);
        btnAgregar.setFont(new Font("SansSerif", Font.BOLD, 22));
        btnAgregar.setForeground(new Color(0, 149, 246));
        btnAgregar.setBorder(BorderFactory.createDashedBorder(new Color(200, 200, 200), 1.5f, 3, 3, false));
        btnAgregar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAgregar.setToolTipText("Importar nuevo sticker");

        btnAgregar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                importarStickerPersonal(panelGaleria);
            }
        });

        panelGaleria.add(btnAgregar);
        
        File carpetaPersonales = new File(Rutas.rutaStickersPersonales(usuarioActual));
        if (carpetaPersonales.exists()) {
            String[] personales = carpetaPersonales.list((dir, name) -> name.toLowerCase().endsWith(".png"));
            if (personales != null) {
                for (String nombre : personales) {
                    agregarMiniaturaSticker(panelGaleria, new File(carpetaPersonales, nombre), nombre);
                }
            }
        }
        File carpetaGlobales = new File(Rutas.RUTA_STICKERS_GLOBALES);
        if (!carpetaGlobales.exists()) {
            carpetaGlobales.mkdirs();
        }
        String[] globales = carpetaGlobales.list((dir, name) -> name.toLowerCase().endsWith(".png"));
        if (globales != null) {
            for (String nombre : globales) {
                agregarMiniaturaSticker(panelGaleria, new File(carpetaGlobales, nombre), nombre);
            }
        }

        panelGaleria.revalidate();
        panelGaleria.repaint();
        
        
    }
    private void agregarMiniaturaSticker(JPanel panelGaleria, File archivoSticker, String nombreArchivo) {
        ImageIcon icono = new ImageIcon(archivoSticker.getAbsolutePath());
        Image escalada = UtilImagen.escalarAlta(icono.getImage(), 50, 50);

        JLabel miniatura = new JLabel(new ImageIcon(escalada));
        miniatura.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        
        miniatura.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarSticker(nombreArchivo);
            }
        });

        panelGaleria.add(miniatura);
    }
    
    

    private void seleccionarSticker(String nombreArchivo) {
        this.stickerSeleccionado = nombreArchivo;
        File enPersonales = new File(Rutas.rutaStickersPersonales(usuarioActual), nombreArchivo);
        File enGlobales = new File(Rutas.RUTA_STICKERS_GLOBALES, nombreArchivo);

        File archivoSticker = enPersonales.exists() ? enPersonales : enGlobales;

        if (archivoSticker.exists()) {
            ImageIcon icono = new ImageIcon(archivoSticker.getAbsolutePath());
            Image escalada = UtilImagen.escalarAlta(icono.getImage(), 45, 45);
            lblPreviewSticker.setIcon(new ImageIcon(escalada));

           
            Container contenedor = lblPreviewSticker.getParent();
            if (contenedor != null) {
                for (Component comp : contenedor.getComponents()) {
                    if (comp instanceof JButton && "btnQuitar".equals(comp.getName())) {
                        comp.setVisible(true);
                    }
                }
                contenedor.revalidate();
                contenedor.repaint();
            }

            panelCentroDerecho.revalidate();
            panelCentroDerecho.repaint();
        }
    }

    private void seleccionarImagen() {
        FileDialog chooser = new FileDialog(parentFrame, "Seleccionar imagen", FileDialog.LOAD);
        chooser.setFile("*.jpg;*.jpeg;*.png");
        chooser.setVisible(true);

        String directorio = chooser.getDirectory();
        String archivo = chooser.getFile();

        if (directorio != null && archivo != null) {
            archivoSeleccionado = new File(directorio, archivo);

            ImageIcon icono = new ImageIcon(archivoSeleccionado.getAbsolutePath());
            Image escalada = UtilImagen.escalarAlta(icono.getImage(), 420, 470);

            lblImagen.setIcon(new ImageIcon(escalada));
            lblImagen.setText("");
        }
    }
    
    private void importarStickerPersonal(JPanel panelGaleria) {
        FileDialog chooser = new FileDialog(parentFrame, "Seleccionar sticker (.png)", FileDialog.LOAD);
        chooser.setFile("*.png");
        chooser.setVisible(true);

        String directorio = chooser.getDirectory();
        String archivo = chooser.getFile();

        if (directorio != null && archivo != null) {
            File origen = new File(directorio, archivo);
            File destinoCarpeta = new File(Rutas.rutaStickersPersonales(usuarioActual));

            if (!destinoCarpeta.exists()) {
                destinoCarpeta.mkdirs();
            }

            String nombreUnico = System.currentTimeMillis() + "_" + archivo;
            File destino = new File(destinoCarpeta, nombreUnico);

            try {
                Files.copy(origen.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                
                cargarGaleriaStickers(panelGaleria);
                seleccionarSticker(nombreUnico);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el sticker personal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void publicar() {
        if (archivoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una imagen antes de compartir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String texto = txtDescripcion.getText().equals("Escribe una descripción...") ? "" : txtDescripcion.getText().trim();

        try {
            String nombreNuevoArchivo = System.currentTimeMillis() + "_" + archivoSeleccionado.getName();
            File carpetaDestino = new File(Rutas.rutaImagenes(usuarioActual));

            if (!carpetaDestino.exists()) {
                carpetaDestino.mkdirs();
            }

            File archivoDestino = new File(carpetaDestino, nombreNuevoArchivo);
            Files.copy(archivoSeleccionado.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);

            ListaEnlazada<String> menciones = new ListaEnlazada<>();
            ListaEnlazada<String> hashtags = new ListaEnlazada<>();
            
            extraerHashtagsYMenciones(texto, menciones, hashtags);

           
            Publicacion nueva = new Publicacion(
                usuarioActual,
                texto,
                java.time.LocalDateTime.now(),
                menciones,
                hashtags,
                nombreNuevoArchivo,
                stickerSeleccionado
            );
            PeticionRed peticion = new PeticionRed("NUEVA_PUBLICACION", nueva);
            RespuestaRed respuesta = ClienteInsta.getInstancia().enviarPeticion(peticion);

            if (respuesta != null && respuesta.isExito()) {
                JOptionPane.showMessageDialog(this, "¡Publicación realizada!");
                dispose();
            } else {
                String errorMsg = (respuesta != null) ? respuesta.getMensajeError() : "Sin respuesta";
                JOptionPane.showMessageDialog(this, "Error del servidor: " + errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
            }

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar la imagen: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error de comunicación con el servidor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            
            /*String rutaArchivoInsta = Rutas.rutaInsta(usuarioActual);
            ListaEnlazada<Publicacion> publicaciones = ArchivoUtil.leerLista(rutaArchivoInsta);
            publicaciones.insertarFinal(nueva);
            ArchivoUtil.guardarLista(rutaArchivoInsta, publicaciones);*/

            JOptionPane.showMessageDialog(this, "¡Publicación realizada!");
            dispose();
    }

    private void extraerHashtagsYMenciones(String texto, ListaEnlazada<String> menciones, ListaEnlazada<String> hashtags) {
        if (texto == null || texto.isEmpty()) return;

        String[] palabras = texto.split("\\s+");

        for (String palabra : palabras) {
            if (palabra.startsWith("#") && palabra.length() > 1) {
                String tagLimpio = palabra.replaceAll("[^#a-zA-Z0-9_]", "");
                if (tagLimpio.length() > 1) {
                    hashtags.insertarFinal(tagLimpio);
                }
            } else if (palabra.startsWith("@") && palabra.length() > 1) {
                String mencionLimpia = palabra.replaceAll("[^@a-zA-Z0-9_]", "");
                if (mencionLimpia.length() > 1) {
                    menciones.insertarFinal(mencionLimpia);
                }
            }
        }
    }
}