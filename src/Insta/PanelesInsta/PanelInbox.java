/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta.PanelesInsta;

import Excepciones.ArchivoCorruptoException;
import Insta.Mensaje;
import Insta.NavegarInsta;
import Insta.SesionActual;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

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
        private String contactoSeleccionado = "";
    
    public PanelInbox(NavegarInsta navegador) {
        this.navegador = navegador;
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);

        inicializarInterfaz();
       
    }
    private void inicializarInterfaz() {
       
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setPreferredSize(new Dimension(330, 0));
        panelIzquierdo.setBackground(Color.WHITE);

       
        JPanel panelHeaderIzquierdo = new JPanel(new BorderLayout());
        panelHeaderIzquierdo.setBackground(Color.WHITE);
        panelHeaderIzquierdo.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        String miUsuario = SesionActual.getInstancia().getUserActual().getUser();
        JLabel lblMiUsuario = new JLabel(miUsuario + " ∨");
        lblMiUsuario.setFont(new Font("SansSerif", Font.BOLD, 18));

        panelHeaderIzquierdo.add(lblMiUsuario, BorderLayout.WEST);

        
        JTextField txtBuscar = new JTextField(" Buscar");
        txtBuscar.setForeground(Color.GRAY);
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtBuscar.setBackground(new Color(245, 245, 245));
        txtBuscar.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

       
        JLabel lblMensajesTitulo = new JLabel("Mensajes");
        lblMensajesTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblMensajesTitulo.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

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
        scrollLista.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, new Color(230, 230, 230)));
        scrollLista.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panelIzquierdo.add(panelSuperiorIzquierdo, BorderLayout.NORTH);
        panelIzquierdo.add(scrollLista, BorderLayout.CENTER);

      
        cardLayoutDerecho = new CardLayout();
        contenedorDerecho = new JPanel(cardLayoutDerecho);

      
        JPanel panelVacio = crearPantallaVacia();

        
        JPanel panelChatActivo = crearPantallaChat();

        contenedorDerecho.add(panelVacio, "VACIO");
        contenedorDerecho.add(panelChatActivo, "CHAT");

        this.add(panelIzquierdo, BorderLayout.WEST);
        this.add(contenedorDerecho, BorderLayout.CENTER);

        cardLayoutDerecho.show(contenedorDerecho, "VACIO");
    }
    private JPanel crearPantallaVacia() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
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

        JLabel lblSub = new JLabel("Envía fotos y mensajes privados a un amigo o un grupo.");
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
    private JPanel crearPantallaChat() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

       
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        header.setPreferredSize(new Dimension(0, 60));

        labelChatActivo = new JLabel(" Usuario");
        labelChatActivo.setFont(new Font("SansSerif", Font.BOLD, 16));
        labelChatActivo.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        header.add(labelChatActivo, BorderLayout.WEST);

        
        panelMensajes = new JPanel();
        panelMensajes.setLayout(new BoxLayout(panelMensajes, BoxLayout.Y_AXIS));
        panelMensajes.setBackground(Color.WHITE);

        JScrollPane scrollMensajes = new JScrollPane(panelMensajes);
        scrollMensajes.setBorder(null);

  
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
        btnSticker.addActionListener(e -> seleccionarSticker());

        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnEnviar.setForeground(new Color(0, 149, 246));
        btnEnviar.setContentAreaFilled(false);
        btnEnviar.setBorderPainted(false);
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
    public void agregarFilaContacto(String username, String ultimoMensaje, String tiempo) {
        JPanel fila = new JPanel(new BorderLayout(12, 0));
        fila.setBackground(Color.WHITE);
        fila.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        fila.setMaximumSize(new Dimension(330, 65));
        fila.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        
        try {
            AvatarCircular avatar = AvatarCircular.crear(username, 44);
            fila.add(avatar, BorderLayout.WEST);
        } catch (ArchivoCorruptoException e) {
            JLabel fallback = new JLabel("●");
            fila.add(fallback, BorderLayout.WEST);
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
        textos.add(Box.createVerticalStrut(3));
        textos.add(lblSub);

        fila.add(textos, BorderLayout.CENTER);

      
        fila.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                contactoSeleccionado = username;
                labelChatActivo.setText(" " + username);
                cardLayoutDerecho.show(contenedorDerecho, "CHAT");
                // Cargar los mensajes guardados server
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                fila.setBackground(new Color(250, 250, 250));
                textos.setBackground(new Color(250, 250, 250));
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
            agregarBurbujaTexto(msg);

            campoTexto.setText("");
        }
    }
    private void seleccionarSticker() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona una imagen o Sticker");
        int res = fileChooser.showOpenDialog(this);

        if (res == JFileChooser.APPROVE_OPTION && !contactoSeleccionado.isEmpty()) {
            File archivo = fileChooser.getSelectedFile();
            String miUsuario = SesionActual.getInstancia().getUserActual().getUser();

           
            agregarBurbujaImagen(archivo.getAbsolutePath(), true);
        }
    }
    private void agregarBurbujaTexto(Mensaje msg) {
        String miUsuario = SesionActual.getInstancia().getUserActual().getUser();
        boolean esMio = msg.getAutor().equalsIgnoreCase(miUsuario);

        JPanel fila = new JPanel(new FlowLayout(esMio ? FlowLayout.RIGHT : FlowLayout.LEFT));
        fila.setBackground(Color.WHITE);

        JLabel burbuja = new JLabel("<html><p style='width: 180px;'>" + msg.getContenido() + "</p></html>");
        burbuja.setOpaque(true);
        burbuja.setFont(new Font("SansSerif", Font.PLAIN, 14));
        burbuja.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        if (esMio) {
            burbuja.setBackground(new Color(0, 149, 246));
            burbuja.setForeground(Color.WHITE);
        } else {
            burbuja.setBackground(new Color(239, 239, 239));
            burbuja.setForeground(Color.BLACK);
        }

        fila.add(burbuja);
        panelMensajes.add(fila);
        panelMensajes.revalidate();
        
      
        SwingUtilities.invokeLater(() -> panelMensajes.scrollRectToVisible(new Rectangle(0, panelMensajes.getHeight(), 1, 1)));
    }
    private void agregarBurbujaImagen(String rutaImagen, boolean esMio) {
        JPanel fila = new JPanel(new FlowLayout(esMio ? FlowLayout.RIGHT : FlowLayout.LEFT));
        fila.setBackground(Color.WHITE);

        ImageIcon iconOriginal = new ImageIcon(rutaImagen);
        Image imgEscalada = iconOriginal.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel lblSticker = new JLabel(new ImageIcon(imgEscalada));

        fila.add(lblSticker);
        panelMensajes.add(fila);
        panelMensajes.revalidate();

        SwingUtilities.invokeLater(() -> panelMensajes.scrollRectToVisible(new Rectangle(0, panelMensajes.getHeight(), 1, 1)));
    }

}
