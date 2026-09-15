/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta.PanelesInsta;
import ConfigInsta.Rutas;
import Insta.Publicacion;
import Insta.NavegarInsta;
import Insta.Reaccion;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import ConfigInsta.ServicioArchivoInsta;
import Insta.UsuarioInsta;
import Excepciones.ArchivoCorruptoException;
import Insta.SesionActual;
import Servidor.ClienteInsta;
import Servidor.PeticionRed;
import Servidor.RespuestaRed;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
/**
 *
 * @author vasqu
 */
public class PanelPublicacion extends JPanel{
    private Publicacion publicacion;
    private NavegarInsta navegador;
    private static final String CLAVE_MENCION="mencionUsername";
    private static final int ANCHO_FIJO = 380;
  

    public PanelPublicacion(Publicacion p, NavegarInsta navegador) {
        this.publicacion=p;
        this.navegador=navegador;
        setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        setBackground(Color.WHITE); 
        try {
        add(construirFilaAutor());
        add(Box.createVerticalStrut(10));
        JLabel imagen = construirImagen();
        if (imagen != null) {
            add(imagen);
        }
        add(Box.createVerticalStrut(10));
         add(construirLikesComentarios());
         add(Box.createVerticalStrut(8));
         add(construirContenido());
         
        JLabel sticker = construirSticker();
        if (sticker != null) add(sticker);

        add(construirFecha());
    } catch (ArchivoCorruptoException | BadLocationException e) {
        add(new JLabel("No se pudo cargar esta publicación."));
        }
    }
    @Override
    public Dimension getPreferredSize() {
        Dimension original = super.getPreferredSize();
        return new Dimension(ANCHO_FIJO, original.height);
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
    
    private JPanel construirFilaAutor()throws ArchivoCorruptoException{
        JPanel fila= new JPanel();
        fila.setLayout(new BoxLayout(fila,BoxLayout.X_AXIS));
        fila.setBackground(Color.WHITE);
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        ServicioArchivoInsta servicio=new ServicioArchivoInsta();
        UsuarioInsta autor = servicio.buscarUsuario(publicacion.getAutor());
        
        Image imagenPerfil;
        
         if (autor.getArchivoFoto() == null) {
            imagenPerfil = new ImageIcon(getClass().getResource("/Insta/Imagenes/UserIcon.png")).getImage();
        } else {
            imagenPerfil =  new ImageIcon(Rutas.rutaFotoPerfil(autor.getUser(), autor.getArchivoFoto())).getImage();
        }

         AvatarCircular perfil=new AvatarCircular(imagenPerfil,40);
     
        JLabel nombreAutor = new JLabel(publicacion.getAutor());
        nombreAutor.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nombreAutor.setForeground(Color.BLACK);
        nombreAutor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                navegador.mostrarPerfil(publicacion.getAutor());
            }
        });

        fila.add(perfil);
        fila.add(Box.createHorizontalStrut(8));
        
        fila.add(nombreAutor);

        Dimension pref = fila.getPreferredSize();
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, pref.height));
        return fila; 
         
    }
    private JLabel construirImagen() {
    if (publicacion.getRutaImagen() == null) {
        return null;
    }
    JLabel lblImagen;
    String rutaCompleta = Paths.get( Rutas.rutaImagenes(publicacion.getAutor()),
        publicacion.getRutaImagen()
    ).toString();

    ImageIcon icono = new ImageIcon(rutaCompleta);
    Image escalada = icono.getImage().getScaledInstance(360, 360, Image.SCALE_SMOOTH);
    
    lblImagen=new JLabel(new ImageIcon(escalada));
    lblImagen.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

    return lblImagen;
}
    
    private JPanel construirLikesComentarios() {
    JPanel fila = new JPanel();
    
    fila.setLayout(new BoxLayout(fila, BoxLayout.X_AXIS));
    fila.setBackground(Color.WHITE);
    fila.setAlignmentX(Component.LEFT_ALIGNMENT);
    ImageIcon iconoLikeRelleno = new ImageIcon(new ImageIcon(getClass().getResource("/Insta/Imagenes/like_relleno.png")) .getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
    ImageIcon iconoLikeContorno = new ImageIcon(new ImageIcon(getClass().getResource("/Insta/Imagenes/like_contorno.png"))  .getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
    ImageIcon iconoComentario = new ImageIcon(new ImageIcon(getClass().getResource("/Insta/Imagenes/comentarios.png")).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));

    String usuarioActual = SesionActual.getInstancia().getUserActual().getUser();
    boolean[] yaDioLike = { publicacion.getReacciones().contiene(new Reaccion(usuarioActual, null)) };

    JButton botonLike = new JButton(yaDioLike[0] ? iconoLikeRelleno : iconoLikeContorno);
    JLabel contadorLikes = new JLabel(publicacion.getReacciones().length() + " likes");
    botonLike.setBorderPainted(false);
    botonLike.setContentAreaFilled(false);
    botonLike.setFocusPainted(false);
    botonLike.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
    botonLike.setMargin(new java.awt.Insets(0, 0, 0, 0));
    botonLike.setOpaque(false);
    botonLike.addActionListener(e -> {
       try {
            
            Reaccion reaccion = new Reaccion(usuarioActual, LocalDateTime.now());

         
            PeticionRed req = new PeticionRed("TOGGLE_LIKE", publicacion.getAutor(), publicacion, reaccion); // <-- Petición Servidor
            RespuestaRed res = ClienteInsta.getInstancia().enviarPeticion(req); // <-- Petición Servidor

           
            if (res != null && res.isExito()) {
                if (yaDioLike[0]) {
                    publicacion.getReacciones().eliminar(new Reaccion(usuarioActual, null));
                } else {
                    publicacion.agregarReaccion(reaccion);
                }
                yaDioLike[0] = !yaDioLike[0];
                botonLike.setIcon(yaDioLike[0] ? iconoLikeRelleno : iconoLikeContorno);
                contadorLikes.setText(publicacion.getReacciones().length() + " likes");
            } else {
                String error = (res != null) ? res.getMensajeError() : "Error de comunicación con el servidor.";
                JOptionPane.showMessageDialog(this, error, "Error de Red", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            System.err.println("Error al procesar el Like en red: " + ex.getMessage());
            //JOptionPane.showMessageDialog(this, "No se pudo conectar con el servidor para registrar el like.", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
    });

    JLabel iconoComentarios = new JLabel(iconoComentario);
    JLabel contadorComentarios = new JLabel(publicacion.getComentarios().length() + " comentarios");

    fila.add(botonLike);
    fila.add(contadorLikes);
    fila.add(Box.createHorizontalStrut(8));
    fila.add(iconoComentarios);
    fila.add(contadorComentarios);
    Dimension pref = fila.getPreferredSize();
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, pref.height));
    return fila;
}
    private JLabel construirFecha() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        JLabel etiqueta = new JLabel(publicacion.getFecha().format(formato));
        etiqueta.setForeground(Color.GRAY);
        etiqueta.setFont(etiqueta.getFont().deriveFont(Font.PLAIN, 12f));
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        return etiqueta;
}
    
    private JLabel construirSticker() {
    if (publicacion.getRutaSticker() == null) {
        return null;
    }
    String nombreArchivo = publicacion.getRutaSticker();
    File enPersonales = new File(Rutas.rutaStickersPersonales(publicacion.getAutor()), nombreArchivo);
    File enGlobales = new File(Rutas.RUTA_STICKERS_GLOBALES, nombreArchivo);

    String rutaFinal = enPersonales.exists() ? enPersonales.getPath() : enGlobales.getPath();

    ImageIcon icono = new ImageIcon(rutaFinal);
    Image escalada = icono.getImage().getScaledInstance(80, -1, Image.SCALE_SMOOTH);
    return new JLabel(new ImageIcon(escalada));
}
    
    private JTextPane construirContenido() throws BadLocationException{
        JTextPane panel= new JTextPane();
        panel.setEditable(false);
        panel.setOpaque(false);
        
        StyledDocument doc = panel.getStyledDocument();
        String contenido = publicacion.getContenido();

        SimpleAttributeSet normal = new SimpleAttributeSet();
        
        SimpleAttributeSet estiloAutor = new SimpleAttributeSet();
        StyleConstants.setBold(estiloAutor, true);

        doc.insertString(doc.getLength(), publicacion.getAutor() + " escribió: ", estiloAutor);
        SimpleAttributeSet estiloMencion = new SimpleAttributeSet();
        StyleConstants.setForeground(estiloMencion, Color.BLUE);
        StyleConstants.setBold(estiloMencion, true);

        SimpleAttributeSet estiloHashtag = new SimpleAttributeSet();
        StyleConstants.setForeground(estiloHashtag, new Color(0, 100, 180));

        Pattern patron = Pattern.compile("@\\w+|#\\w+");
        Matcher m = patron.matcher(contenido);
        
        int ultimaPosicion=0;
        while(m.find()){
            String textoAntes = contenido.substring(ultimaPosicion, m.start());
            doc.insertString(doc.getLength(), textoAntes, normal);
            
            String coincidencia=m.group();
            if(coincidencia.startsWith("@")){
                SimpleAttributeSet atributosMencion= new SimpleAttributeSet(estiloMencion);
                atributosMencion.addAttribute(CLAVE_MENCION, coincidencia.substring(1));
                doc.insertString(doc.getLength(), coincidencia, atributosMencion);
            } else {
                doc.insertString(doc.getLength(), coincidencia, estiloHashtag);
            }
            ultimaPosicion=m.end();
        }
        
         doc.insertString(doc.getLength(), contenido.substring(ultimaPosicion), normal);

        panel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int offset = panel.viewToModel2D(e.getPoint());
            AttributeSet atributos = doc.getCharacterElement(offset).getAttributes();
            String username = (String) atributos.getAttribute(CLAVE_MENCION);
            
            if (username != null) {
                navegador.mostrarPerfil(username);
            }
        }
    });
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        return panel;
        
        }
        
    
    
    }
    
    
