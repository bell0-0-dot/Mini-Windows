/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta.PanelesInsta;

import ConfigInsta.Rutas;
import ConfigInsta.UtilImagen;
import Excepciones.ArchivoCorruptoException;
import Insta.Comentario;
import Insta.NavegarInsta;
import Insta.Publicacion;
import Insta.Reaccion;
import Insta.SesionActual;
import Servidor.ClienteInsta;
import Servidor.PeticionRed;
import Servidor.RespuestaRed;
import base.ListaEnlazada;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author vasqu
 */
public class PanelDetallePublicacion extends JDialog{
    private Publicacion publicacion;
    private NavegarInsta navegador;
    private String usuarioActual;
    
    private JPanel contenedorComentarios;
    private JTextField txtNuevoComentario;
    private JLabel lblContadorLikes;
    private JLabel lblContadorComentarios;
    private JButton btnLike;
    private boolean[] yaDioLike;
    
    private ImageIcon iconoLikeRelleno;
    private ImageIcon iconoLikeContorno;
    private ImageIcon iconoComentario;

    public PanelDetallePublicacion(Frame parent, Publicacion publicacion, NavegarInsta navegador) {
        super(parent, true);
        this.publicacion = publicacion;
        this.navegador = navegador;
        this.usuarioActual = SesionActual.getInstancia().getUserActual().getUser();

        setUndecorated(true);
        setSize(850, 530);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(218, 218, 218), 1));

        JPanel contenedorCentral = new JPanel(new GridLayout(1, 2));
        contenedorCentral.setBackground(Color.WHITE);

        contenedorCentral.add(construirSeccionIzquierda());
        contenedorCentral.add(construirSeccionDerecha());

        add(contenedorCentral, BorderLayout.CENTER);
    }

    private JPanel construirSeccionIzquierda() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        JLabel lblFoto = new JLabel();
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);

        String rutaCompleta = Paths.get(Rutas.rutaImagenes(publicacion.getAutor()), publicacion.getRutaImagen()).toString();
        File archivo = new File(rutaCompleta);

        if (archivo.exists()) {
            ImageIcon icono = new ImageIcon(archivo.getAbsolutePath());
            Image escalada = UtilImagen.escalarAlta(icono.getImage(), 425, 530);
            lblFoto.setIcon(new ImageIcon(escalada));
        } else {
            lblFoto.setText("📷 Imagen no disponible");
            lblFoto.setForeground(Color.WHITE);
        }

        panel.add(lblFoto, BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirSeccionDerecha() {
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBackground(Color.WHITE);

       
        panelDerecho.add(construirHeaderAutor(), BorderLayout.NORTH);

        
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setBackground(Color.WHITE);

        try {
            panelCentro.add(construirFilaDescripcion());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        contenedorComentarios = new JPanel();
        contenedorComentarios.setLayout(new BoxLayout(contenedorComentarios, BoxLayout.Y_AXIS));
        contenedorComentarios.setBackground(Color.WHITE);

        cargarComentarios();

        JScrollPane scrollComentarios = new JScrollPane(contenedorComentarios);
        scrollComentarios.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(230, 230, 230)));
        scrollComentarios.getVerticalScrollBar().setUI(new BasicScrollBar());
        scrollComentarios.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        scrollComentarios.getVerticalScrollBar().setUnitIncrement(12);

        panelCentro.add(scrollComentarios);
        panelDerecho.add(panelCentro, BorderLayout.CENTER);

       
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));
        panelInferior.setBackground(Color.WHITE);

        panelInferior.add(construirBarraReacciones());
        panelInferior.add(construirCajaComentar());

        panelDerecho.add(panelInferior, BorderLayout.SOUTH);

        return panelDerecho;
    }

    private JPanel construirHeaderAutor() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JPanel infoAutor = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        infoAutor.setBackground(Color.WHITE);

        try {
            AvatarCircular avatar = AvatarCircular.crear(publicacion.getAutor(), 36);
            infoAutor.add(avatar);
        } catch (ArchivoCorruptoException e) { }

        JLabel lblAutor = new JLabel(publicacion.getAutor());
        lblAutor.setFont(lblAutor.getFont().deriveFont(Font.BOLD, 13f));
        lblAutor.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblAutor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                navegador.mostrarPerfil(publicacion.getAutor());
            }
        });

        infoAutor.add(lblAutor);

        JLabel btnCerrar = new JLabel("✕", SwingConstants.CENTER);
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });

        header.add(infoAutor, BorderLayout.WEST);
        header.add(btnCerrar, BorderLayout.EAST);
        return header;
    }

    private JPanel construirFilaDescripcion() throws BadLocationException {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setBackground(Color.WHITE);
        fila.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JTextPane txtContenido = new JTextPane();
        txtContenido.setEditable(false);
        txtContenido.setOpaque(false);

        StyledDocument doc = txtContenido.getStyledDocument();
        SimpleAttributeSet normal = new SimpleAttributeSet();
        SimpleAttributeSet estiloAutor = new SimpleAttributeSet();
        StyleConstants.setBold(estiloAutor, true);

        doc.insertString(doc.getLength(), publicacion.getAutor() + " ", estiloAutor);

        SimpleAttributeSet estiloMencion = new SimpleAttributeSet();
        StyleConstants.setForeground(estiloMencion, new Color(0, 55, 107));
        StyleConstants.setBold(estiloMencion, true);

        Pattern patron = Pattern.compile("@\\w+|#\\w+");
        Matcher m = patron.matcher(publicacion.getContenido());

        int ultimaPos = 0;
        while (m.find()) {
            doc.insertString(doc.getLength(), publicacion.getContenido().substring(ultimaPos, m.start()), normal);
            String coincidencia = m.group();
            if (coincidencia.startsWith("@")) {
                SimpleAttributeSet attrs = new SimpleAttributeSet(estiloMencion);
                attrs.addAttribute("user", coincidencia.substring(1));
                doc.insertString(doc.getLength(), coincidencia, attrs);
            } else {
                doc.insertString(doc.getLength(), coincidencia, estiloMencion);
            }
            ultimaPos = m.end();
        }
        doc.insertString(doc.getLength(), publicacion.getContenido().substring(ultimaPos), normal);

        txtContenido.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int offset = txtContenido.viewToModel2D(e.getPoint());
                AttributeSet attrs = doc.getCharacterElement(offset).getAttributes();
                String targetUser = (String) attrs.getAttribute("user");
                if (targetUser != null) {
                    dispose();
                    navegador.mostrarPerfil(targetUser);
                }
            }
        });

        fila.add(txtContenido, BorderLayout.CENTER);
        return fila;
    }

    private void cargarComentarios() {
        contenedorComentarios.removeAll();
        ListaEnlazada<Comentario> comentarios = publicacion.getComentarios();

        if (comentarios != null && !comentarios.estaVacia()) {
            for (int i = 0; i < comentarios.length(); i++) {
                Comentario c = comentarios.obtenerEn(i);
                contenedorComentarios.add(construirFilaComentario(c));
            }
        }

        
        contenedorComentarios.add(Box.createVerticalGlue());

        contenedorComentarios.revalidate();
        contenedorComentarios.repaint();
    }

    private JPanel construirFilaComentario(Comentario c) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        fila.setBackground(Color.WHITE);
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTexto = new JLabel("<html><b>" + c.getAutor() + "</b> " + c.getTexto() + "</html>");
        lblTexto.setFont(lblTexto.getFont().deriveFont(12f));

        fila.add(lblTexto);
        return fila;
    }

    private JPanel construirBarraReacciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panel.setBackground(Color.WHITE);

        iconoLikeRelleno = new ImageIcon(new ImageIcon(getClass().getResource("/Insta/Imagenes/like_relleno.png")).getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH));
        iconoLikeContorno = new ImageIcon(new ImageIcon(getClass().getResource("/Insta/Imagenes/like_contorno.png")).getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH));
        iconoComentario = new ImageIcon(new ImageIcon(getClass().getResource("/Insta/Imagenes/comentarios.png")).getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH));

        yaDioLike = new boolean[]{ publicacion.getReacciones().contiene(new Reaccion(usuarioActual, null)) };

        btnLike = new JButton(yaDioLike[0] ? iconoLikeRelleno : iconoLikeContorno);
        btnLike.setBorderPainted(false);
        btnLike.setContentAreaFilled(false);
        btnLike.setFocusPainted(false);
        btnLike.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblContadorLikes = new JLabel(publicacion.getReacciones().length() + " Me gusta");
        lblContadorLikes.setFont(lblContadorLikes.getFont().deriveFont(Font.BOLD, 12f));

        btnLike.addActionListener(e -> {
            try {
                Reaccion r = new Reaccion(usuarioActual, LocalDateTime.now());
                PeticionRed req = new PeticionRed("TOGGLE_LIKE", publicacion.getAutor(), publicacion, r);
                RespuestaRed res = ClienteInsta.getInstancia().enviarPeticion(req);

                if (res != null && res.isExito()) {
                    if (yaDioLike[0]) {
                        publicacion.getReacciones().eliminar(new Reaccion(usuarioActual, null));
                    } else {
                        publicacion.agregarReaccion(r);
                    }
                    yaDioLike[0] = !yaDioLike[0];
                    btnLike.setIcon(yaDioLike[0] ? iconoLikeRelleno : iconoLikeContorno);
                    lblContadorLikes.setText(publicacion.getReacciones().length() + " Me gusta");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JLabel lblIconoComentario = new JLabel(iconoComentario);
        lblContadorComentarios = new JLabel(publicacion.getComentarios().length() + " comentarios");
        lblContadorComentarios.setFont(lblContadorComentarios.getFont().deriveFont(Font.BOLD, 12f));

      
        panel.add(btnLike);
        panel.add(lblContadorLikes);
        panel.add(Box.createHorizontalStrut(12));
        panel.add(lblIconoComentario);
        panel.add(lblContadorComentarios);

        return panel;
    }

    private JPanel construirCajaComentar() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        txtNuevoComentario = new JTextField();
        txtNuevoComentario.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        txtNuevoComentario.setText("Agrega un comentario...");
        txtNuevoComentario.setForeground(Color.GRAY);

        txtNuevoComentario.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtNuevoComentario.getText().equals("Agrega un comentario...")) {
                    txtNuevoComentario.setText("");
                    txtNuevoComentario.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtNuevoComentario.getText().trim().isEmpty()) {
                    txtNuevoComentario.setText("Agrega un comentario...");
                    txtNuevoComentario.setForeground(Color.GRAY);
                }
            }
        });

        JButton btnPublicar = new JButton("Publicar");
        btnPublicar.setFont(btnPublicar.getFont().deriveFont(Font.BOLD, 12f));
        btnPublicar.setForeground(new Color(0, 149, 246));
        btnPublicar.setContentAreaFilled(false);
        btnPublicar.setBorderPainted(false);
        btnPublicar.setFocusPainted(false);
        btnPublicar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnPublicar.addActionListener(e -> enviarComentario());

        panel.add(txtNuevoComentario, BorderLayout.CENTER);
        panel.add(btnPublicar, BorderLayout.EAST);

        return panel;
    }

    private void enviarComentario() {
        String texto = txtNuevoComentario.getText().trim();
        if (texto.isEmpty() || texto.equals("Agrega un comentario...")) {
            return;
        }

        try {
            Comentario nuevoComentario = new Comentario(usuarioActual, texto, LocalDateTime.now());
            PeticionRed req = new PeticionRed("AGREGAR_COMENTARIO", publicacion.getAutor(), publicacion, nuevoComentario);
            RespuestaRed res = ClienteInsta.getInstancia().enviarPeticion(req);

            if (res != null && res.isExito()) {
               
                publicacion.getComentarios().insertarFinal(nuevoComentario);
                cargarComentarios();
                
                lblContadorComentarios.setText(publicacion.getComentarios().length() + " comentarios");
                
               
                txtNuevoComentario.setText("");
            } else {
                String err = (res != null) ? res.getMensajeError() : "Error en el servidor";
                //JOptionPane.showMessageDialog(this, err, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
