/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta.PanelesInsta;

import ConfigInsta.Rutas;
import ConfigInsta.ServicioArchivoInsta;
import Excepciones.ArchivoCorruptoException;
import Insta.Mensaje;
import Insta.NavegarInsta;
import Insta.PanelesInsta.AvatarCircular;
import Insta.SesionActual;
import Insta.UsuarioInsta;
import Servidor.ClienteInsta;
import Servidor.PeticionRed;
import Servidor.RespuestaRed;
import base.ListaEnlazada;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author vasqu
 */
public class PanelInbox extends JPanel{
    private NavegarInsta navegador;
    private JPanel panelListaChats;
    private JPanel contenedorDerecho;
    private CardLayout cardLayoutDerecho;

    private JPanel panelMensajes;
    private JTextField campoTexto;
    private JLabel labelChatActivo;
    private JPanel panelHeaderChatInfo; 
    private String contactoSeleccionado = "";
    private int ultimoConteoMensajes = -1;
    private Timer timerRefrescoChat;
    private Timer timerActualizacion;
    private String conversacionAbiertaCon;

    public PanelInbox(NavegarInsta navegador) throws ArchivoCorruptoException {
        this.navegador = navegador;
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);

        inicializarInterfaz();
        
       
        timerRefrescoChat = new Timer(3000, e -> refrescarChatEnTiempoReal());
        
        
        conectarConServidor();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if (!contactoSeleccionado.isEmpty()) {
                    timerRefrescoChat.start();
                }
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                timerRefrescoChat.stop();
            }
        });

        try {
            cargarSeguidosComoChats();
        } catch (ArchivoCorruptoException e) {

        }
    }

    private void conectarConServidor() {
        try {
           
            ClienteInsta.getInstancia().registrarListenerNotificaciones(notif -> {
                if (!contactoSeleccionado.isEmpty()) {
                    SwingUtilities.invokeLater(this::refrescarChatEnTiempoReal);
                }
            });
        } catch (Exception e) {
            System.err.println("Error al conectar listener con el servidor: " + e.getMessage());
        }
    }

    private void inicializarInterfaz() {

        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setPreferredSize(new Dimension(300, 0));
        panelIzquierdo.setBackground(Color.WHITE);

        JPanel panelHeaderIzquierdo = new JPanel(new BorderLayout());
        panelHeaderIzquierdo.setBackground(Color.WHITE);
        panelHeaderIzquierdo.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 20));

        String miUsuario = SesionActual.getInstancia().getUserActual().getUser();

        JPanel panelUsuarioInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelUsuarioInfo.setBackground(Color.WHITE);

        try {
            AvatarCircular avatarUsuario = AvatarCircular.crear(miUsuario, 32);
            panelUsuarioInfo.add(avatarUsuario);
        } catch (ArchivoCorruptoException e) {

        }

        JLabel lblMiUsuario = new JLabel(miUsuario + " ∨");
        lblMiUsuario.setFont(new Font("SansSerif", Font.BOLD, 18));
        panelUsuarioInfo.add(lblMiUsuario);

        panelHeaderIzquierdo.add(panelUsuarioInfo, BorderLayout.WEST);

        JTextField txtBuscar = new JTextField(" Buscar");
        txtBuscar.setForeground(Color.GRAY);
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtBuscar.setBackground(new Color(245, 245, 245));
        txtBuscar.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        txtBuscar.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtBuscar.getText().equals(" Buscar") || txtBuscar.getText().equals("Buscar")) {
                    txtBuscar.setText("");
                    txtBuscar.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtBuscar.getText().trim().isEmpty()) {
                    txtBuscar.setText(" Buscar");
                    txtBuscar.setForeground(Color.GRAY);
                }
            }
        });
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }

            private void filtrar() {
                String texto = txtBuscar.getText().trim();

                if (texto.equalsIgnoreCase("Buscar")) {
                    texto = "";
                }
                filtrarListaChats(texto);
            }
        });

        JLabel lblMensajesTitulo = new JLabel("Mensajes");
        lblMensajesTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblMensajesTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        lblMensajesTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel panelSuperiorIzquierdo = new JPanel();
        panelSuperiorIzquierdo.setLayout(new BoxLayout(panelSuperiorIzquierdo, BoxLayout.Y_AXIS));
        panelSuperiorIzquierdo.setBackground(Color.WHITE);
        panelSuperiorIzquierdo.add(panelHeaderIzquierdo);

        JPanel pnlBusqueda = new JPanel(new BorderLayout());
        pnlBusqueda.setBackground(Color.WHITE);
        pnlBusqueda.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        pnlBusqueda.add(txtBuscar, BorderLayout.CENTER);

        panelSuperiorIzquierdo.add(pnlBusqueda);
        panelSuperiorIzquierdo.add(lblMensajesTitulo);

        panelListaChats = new JPanel();
        panelListaChats.setLayout(new BoxLayout(panelListaChats, BoxLayout.Y_AXIS));
        panelListaChats.setBackground(Color.WHITE);

        JScrollPane scrollLista = new JScrollPane(panelListaChats);
        scrollLista.getVerticalScrollBar().setUI(new BasicScrollBar());
        scrollLista.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0)); // Delgado
        scrollLista.getVerticalScrollBar().setUnitIncrement(16);
        scrollLista.setHorizontalScrollBar(null);
        scrollLista.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, new Color(230, 230, 230)));
        scrollLista.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panelIzquierdo.add(panelSuperiorIzquierdo, BorderLayout.NORTH);
        panelIzquierdo.add(scrollLista, BorderLayout.CENTER);

        cardLayoutDerecho = new CardLayout();
        contenedorDerecho = new JPanel(cardLayoutDerecho);

        contenedorDerecho.add(crearPantallaVacia(), "VACIO");
        contenedorDerecho.add(crearPantallaChat(), "CHAT");
        contenedorDerecho.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(230, 230, 230)));
        this.add(panelIzquierdo, BorderLayout.WEST);
        this.add(contenedorDerecho, BorderLayout.CENTER);

        cardLayoutDerecho.show(contenedorDerecho, "VACIO");
    }

    private JPanel crearPantallaVacia() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(Color.WHITE);

        JLabel lblIcono = new JLabel("✈", SwingConstants.CENTER);
        lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 45));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("Tus mensajes");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Envía fotos y mensajes privados a un amigo.");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSub.setForeground(Color.GRAY);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        contenido.add(lblIcono);
        contenido.add(Box.createVerticalStrut(15));
        contenido.add(lblTitulo);
        contenido.add(Box.createVerticalStrut(8));
        contenido.add(lblSub);

        panel.add(contenido);
        return panel;
    }

    private void refrescarChatEnTiempoReal() {
        if (contactoSeleccionado.isEmpty() || SesionActual.getInstancia().getUserActual() == null) {
            return;
        }

        String miUsuario = SesionActual.getInstancia().getUserActual().getUser();
        
      
        ListaEnlazada<Mensaje> historial = obtenerChatDesdeServidor(miUsuario, contactoSeleccionado);

        int conteoActual = (historial != null) ? historial.length() : 0;

        if (conteoActual != ultimoConteoMensajes) {
            cargarHistorialChat(miUsuario, contactoSeleccionado);
            try {
                cargarSeguidosComoChats();
            } catch (ArchivoCorruptoException ex) { }
        }
    }

    private JPanel crearPantallaChat() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        header.setPreferredSize(new Dimension(0, 60));

        panelHeaderChatInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelHeaderChatInfo.setBackground(Color.WHITE);

        labelChatActivo = new JLabel();
        labelChatActivo.setFont(new Font("SansSerif", Font.BOLD, 16));

        panelHeaderChatInfo.add(labelChatActivo);
        header.add(panelHeaderChatInfo, BorderLayout.WEST);

        panelMensajes = new JPanel();
        panelMensajes.setLayout(new BoxLayout(panelMensajes, BoxLayout.Y_AXIS));

        panelMensajes.setBackground(Color.WHITE);

        JScrollPane scrollMensajes = new JScrollPane(panelMensajes);
        scrollMensajes.setBorder(null);
        scrollMensajes.getVerticalScrollBar().setUI(new BasicScrollBar());
        scrollMensajes.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0)); // Delgado
        scrollMensajes.getVerticalScrollBar().setUnitIncrement(16);
        scrollMensajes.setHorizontalScrollBar(null);

        JPanel panelInput = new JPanel(new BorderLayout(10, 0));
        panelInput.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        panelInput.setBackground(Color.WHITE);

        campoTexto = new JTextField();
        campoTexto.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campoTexto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JButton btnSticker = new JButton("☺");
        btnSticker.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btnSticker.setFocusPainted(false);
        btnSticker.setContentAreaFilled(false);
        btnSticker.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSticker.addActionListener(e -> mostrarSelectorStickers(btnSticker));

        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setBackground(new Color(0, 149, 246));
        btnEnviar.setOpaque(true);
        btnEnviar.setContentAreaFilled(true);            
        btnEnviar.setBorderPainted(false);               
        btnEnviar.setFocusPainted(false);
        btnEnviar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnEnviar.addActionListener(e -> enviarTexto());
        campoTexto.addActionListener(e -> enviarTexto());

        panelInput.add(btnSticker, BorderLayout.WEST);
        panelInput.add(campoTexto, BorderLayout.CENTER);
        panelInput.add(btnEnviar, BorderLayout.EAST);

        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollMensajes, BorderLayout.CENTER);
        panel.add(panelInput, BorderLayout.SOUTH);

        return panel;
    }

    public void cargarSeguidosComoChats() throws ArchivoCorruptoException {
        String miUsuario = SesionActual.getInstancia().getUserActual().getUser();
        panelListaChats.removeAll();

       
        ListaEnlazada<String> seguidos = obtenerSeguidosDesdeServidor(miUsuario);
        ListaEnlazada<String> agregados = new ListaEnlazada<>();

        if (seguidos != null) {
            for (int i = 0; i < seguidos.length(); i++) {
                String usuarioSeguido = seguidos.obtenerEn(i);

                if (!agregados.contiene(usuarioSeguido)) {
                    agregados.insertarFinal(usuarioSeguido);

                  
                    ListaEnlazada<Mensaje> chat = obtenerChatDesdeServidor(miUsuario, usuarioSeguido);
                    String ultimoTexto = "Haz clic para chatear";
                    String tiempo = "Ahora";

                    if (chat != null && chat.length() > 0) {
                        Mensaje ultimoMsg = chat.obtenerEn(chat.length() - 1);
                        ultimoTexto = ultimoMsg.getContenido();
                        tiempo = ultimoMsg.getHoraFormato();
                    }
                    agregarFilaContacto(usuarioSeguido, ultimoTexto, tiempo);
                }
            }
        }

        panelListaChats.revalidate();
        panelListaChats.repaint();
    }

    private void filtrarListaChats(String filtro) {
        String query = filtro.toLowerCase();

        for (Component comp : panelListaChats.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel fila = (JPanel) comp;
                boolean coincide = false;

                for (Component subComp : fila.getComponents()) {
                    if (subComp instanceof JPanel) { 
                        for (Component c : ((JPanel) subComp).getComponents()) {
                            if (c instanceof JLabel) {
                                JLabel lbl = (JLabel) c;
                                if (lbl.getText().toLowerCase().contains(query)) {
                                    coincide = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                fila.setVisible(query.isEmpty() || coincide);
            }
        }

        panelListaChats.revalidate();
        panelListaChats.repaint();
    }

    private void agregarFilaContacto(String username, String ultimoMensaje, String tiempo) {
        JPanel fila = new JPanel(new BorderLayout(12, 0));
        fila.setBackground(Color.WHITE);
        fila.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        fila.setMaximumSize(new Dimension(300, 60));
        fila.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        try {
            AvatarCircular avatar = AvatarCircular.crear(username, 40);
            fila.add(avatar, BorderLayout.WEST);
        } catch (ArchivoCorruptoException e) {
            fila.add(new JLabel("●"), BorderLayout.WEST);
        }

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setBackground(Color.WHITE);

        JLabel lblName = new JLabel(username);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel lblSub = new JLabel(ultimoMensaje + " · " + tiempo);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(Color.GRAY);

        textos.add(lblName);
        textos.add(Box.createVerticalStrut(2));
        textos.add(lblSub);

        fila.add(textos, BorderLayout.CENTER);

        fila.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                contactoSeleccionado = username;
                String miUsuario = SesionActual.getInstancia().getUserActual().getUser();

                panelHeaderChatInfo.removeAll();
                try {
                    AvatarCircular avatarTop = AvatarCircular.crear(username, 36);
                    panelHeaderChatInfo.add(avatarTop);
                } catch (ArchivoCorruptoException ex) {}

                labelChatActivo.setText(username);
                panelHeaderChatInfo.add(labelChatActivo);

                panelHeaderChatInfo.revalidate();
                panelHeaderChatInfo.repaint();
                cargarHistorialChat(miUsuario, username);
                cardLayoutDerecho.show(contenedorDerecho, "CHAT");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                fila.setBackground(new Color(245, 245, 245));
                textos.setBackground(new Color(245, 245, 245));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                fila.setBackground(Color.WHITE);
                textos.setBackground(Color.WHITE);
            }
        });

        panelListaChats.add(fila);
        panelListaChats.revalidate();
    }

    private void enviarTexto() {
        String texto = campoTexto.getText().trim();
        if (!texto.isEmpty() && !contactoSeleccionado.isEmpty()) {
            String miUsuario = SesionActual.getInstancia().getUserActual().getUser();
            Mensaje msg = new Mensaje(miUsuario, contactoSeleccionado, texto);

            
            guardarMensajeEnServidor(msg);

            agregarBurbujaTexto(msg);
            campoTexto.setText("");
            try {
                cargarSeguidosComoChats();
            } catch (ArchivoCorruptoException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void enviarSticker(String rutaImagen) {
        if (!contactoSeleccionado.isEmpty()) {
            String miUsuario = SesionActual.getInstancia().getUserActual().getUser();

            File archivoSticker = new File(rutaImagen);
            String nombreSticker = archivoSticker.getName();

            Mensaje msg = new Mensaje(
                    miUsuario,
                    contactoSeleccionado,
                    nombreSticker,
                    true
            );

            agregarBurbujaImagen(msg, true);

           
            guardarMensajeEnServidor(msg);

            try {
                cargarSeguidosComoChats();
            } catch (ArchivoCorruptoException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void cargarHistorialChat(String miUsuario, String contacto) {
        panelMensajes.removeAll();

  
        ListaEnlazada<Mensaje> historial = obtenerChatDesdeServidor(miUsuario, contacto);

        if (historial != null) {
            ultimoConteoMensajes = historial.length();
            for (int i = 0; i < historial.length(); i++) {
                Mensaje msg = historial.obtenerEn(i);

                if (msg.isEsSticker()) {
                    agregarBurbujaImagen(msg, msg.getAutor().equalsIgnoreCase(miUsuario));
                } else {
                    agregarBurbujaTexto(msg);
                }
            }
        }

        panelMensajes.revalidate();
        panelMensajes.repaint();

        SwingUtilities.invokeLater(() ->
            panelMensajes.scrollRectToVisible(new Rectangle(0, panelMensajes.getHeight(), 1, 1))
        );
    }

    private void agregarBurbujaTexto(Mensaje msg) {
        String miUsuario = SesionActual.getInstancia().getUserActual().getUser();
        boolean esMio = msg.getAutor().equalsIgnoreCase(miUsuario);

        JPanel fila = new JPanel(new FlowLayout(esMio ? FlowLayout.RIGHT : FlowLayout.LEFT, 10, 2));
        fila.setBackground(Color.WHITE);

        BurbujaRedondeada burbuja = new BurbujaRedondeada(
                msg.getContenido(),
                esMio ? new Color(0, 149, 246) : new Color(239, 239, 239),
                esMio ? Color.WHITE : Color.BLACK
        );

        fila.add(burbuja);

        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, fila.getPreferredSize().height));

        panelMensajes.add(fila);
        panelMensajes.revalidate();

        SwingUtilities.invokeLater(() -> panelMensajes.scrollRectToVisible(new Rectangle(0, panelMensajes.getHeight(), 1, 1)));
    }

    private void agregarBurbujaImagen(Mensaje mensaje, boolean esMio) {
        String rutaSticker = Rutas.rutaStickersPersonales(mensaje.getAutor()) + File.separator + mensaje.getContenido().toString();
        File archivoImg = new File(rutaSticker);

        if (!archivoImg.exists()) {
            String rutaGlobal = Rutas.RUTA_STICKERS_GLOBALES + File.separator + mensaje.getContenido();
            archivoImg = new File(rutaGlobal);
        }

        JPanel fila = new JPanel(new FlowLayout(esMio ? FlowLayout.RIGHT : FlowLayout.LEFT, 10, 2));
        fila.setBackground(Color.WHITE);

        if (archivoImg.exists()) {
            ImageIcon iconOriginal = new ImageIcon(archivoImg.getAbsolutePath());
            Image imgEscalada = iconOriginal.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            JLabel lblSticker = new JLabel(new ImageIcon(imgEscalada));
            fila.add(lblSticker);
        } else {
            JLabel lblError = new JLabel("Sticker no encontrado");
            lblError.setFont(new Font("SansSerif", Font.ITALIC, 11));
            lblError.setForeground(Color.GRAY);
            fila.add(lblError);
        }

        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, fila.getPreferredSize().height));

        panelMensajes.add(fila);
        panelMensajes.revalidate();

        SwingUtilities.invokeLater(() ->
            panelMensajes.scrollRectToVisible(new Rectangle(0, panelMensajes.getHeight(), 1, 1))
        );
    }

    private void mostrarSelectorStickers(Component invoker) {
        JPopupMenu popupMenu = new JPopupMenu();
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(280, 220));

        String miUsuario = SesionActual.getInstancia().getUserActual().getUser();

        tabbedPane.addTab("Personales", crearGridStickers(Rutas.rutaStickersPersonales(miUsuario), popupMenu, true));
        tabbedPane.addTab("Globales", crearGridStickers(Rutas.RUTA_STICKERS_GLOBALES, popupMenu, false));

        popupMenu.add(tabbedPane);
        popupMenu.show(invoker, 0, -230);
    }

    private JScrollPane crearGridStickers(String rutaCarpeta, JPopupMenu popup, boolean esPersonal) {
        JPanel panelGrid = new JPanel(new GridLayout(0, 3, 5, 5));
        panelGrid.setBackground(Color.WHITE);

        if (esPersonal) {
            JButton btnImportar = new JButton("+");
            btnImportar.setFont(new Font("SansSerif", Font.BOLD, 22));
            btnImportar.setToolTipText("Importar nuevo sticker");
            btnImportar.setFocusPainted(false);
            btnImportar.setContentAreaFilled(false);
            btnImportar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnImportar.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

            btnImportar.addActionListener(e -> importarStickerPersonal(popup));
            panelGrid.add(btnImportar);
        }

        File carpeta = new File(rutaCarpeta);
        if (carpeta.exists() && carpeta.isDirectory()) {
            File[] archivos = carpeta.listFiles((dir, name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg"));
            if (archivos != null) {
                for (File f : archivos) {
                    ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                    Image img = icon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
                    JButton btn = new JButton(new ImageIcon(img));
                    btn.setPreferredSize(new Dimension(70, 70));
                    btn.setContentAreaFilled(false);
                    btn.setFocusPainted(false);
                    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                    btn.addActionListener(e -> {
                        popup.setVisible(false);
                        enviarSticker(f.getAbsolutePath());
                    });

                    panelGrid.add(btn);
                }
            }
        }

        JScrollPane scroll = new JScrollPane(panelGrid);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUI(new BasicScrollBar());
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0)); // Delgado
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBar(null);
        return scroll;
    }

    private static class BurbujaRedondeada extends JPanel {
        private String texto;
        private Color colorFondo;
        private Color colorTexto;

        public BurbujaRedondeada(String texto, Color colorFondo, Color colorTexto) {
            this.texto = texto;
            this.colorFondo = colorFondo;
            this.colorTexto = colorTexto;
            this.setOpaque(false);
            this.setLayout(new BorderLayout());
            this.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

            JLabel label = new JLabel("<html><p style='width: 180px;'>" + texto + "</p></html>");
            label.setForeground(colorTexto);
            label.setFont(new Font("SansSerif", Font.PLAIN, 14));
            this.add(label, BorderLayout.CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(colorFondo);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 18, 18));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private void importarStickerPersonal(JPopupMenu popup) {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);

        FileDialog chooser = new FileDialog(parentFrame, "Seleccionar imagen", FileDialog.LOAD);
        chooser.setFile("*.jpg;*.jpeg;*.png");
        chooser.setVisible(true);

        String directorio = chooser.getDirectory();
        String archivo = chooser.getFile();

        if (directorio != null && archivo != null) {
            File archivoSeleccionado = new File(directorio, archivo);
            String miUsuario = SesionActual.getInstancia().getUserActual().getUser();

            File carpetaDestino = new File(Rutas.rutaStickersPersonales(miUsuario));
            if (!carpetaDestino.exists()) {
                carpetaDestino.mkdirs();
            }

            File archivoDestino = new File(carpetaDestino, archivoSeleccionado.getName());

            try {
                Files.copy(archivoSeleccionado.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);

                if (popup != null) {
                    popup.setVisible(false); 
                }

                JOptionPane.showMessageDialog(this, "Sticker agregado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al guardar el sticker: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

  //servidor
    private void guardarMensajeEnServidor(Mensaje msg) {
        try {
            PeticionRed peticion = new PeticionRed("GUARDAR_MENSAJE", msg);
            ClienteInsta.getInstancia().enviarPeticion(peticion);
        } catch (Exception e) {
            System.err.println("Error al guardar mensaje en el servidor: " + e.getMessage());
        }
    }

    private ListaEnlazada<Mensaje> obtenerChatDesdeServidor(String u1, String u2) {
        try {
            String[] params = new String[]{u1, u2};
            PeticionRed peticion = new PeticionRed("OBTENER_CHAT_ENTRE", (Object[]) params);
            RespuestaRed respuesta = ClienteInsta.getInstancia().enviarPeticion(peticion);

            if (respuesta != null && respuesta.isExito() && respuesta.getContenido() instanceof ListaEnlazada) {
                return (ListaEnlazada<Mensaje>) respuesta.getContenido();
            }
        } catch (Exception e) {
            System.err.println("Error al obtener chat desde el servidor: " + e.getMessage());
        }
        return new ListaEnlazada<>();
    }

    private ListaEnlazada<String> obtenerSeguidosDesdeServidor(String usuario) {
        try {
            PeticionRed peticion = new PeticionRed("OBTENER_SEGUIDOS", usuario);
            RespuestaRed respuesta = ClienteInsta.getInstancia().enviarPeticion(peticion);

            if (respuesta != null && respuesta.isExito() && respuesta.getContenido() instanceof ListaEnlazada) {
                return (ListaEnlazada<String>) respuesta.getContenido();
            }
        } catch (Exception e) {
            System.err.println("Error al obtener seguidos desde el servidor: " + e.getMessage());
        }
        return new ListaEnlazada<>();
    }
}
