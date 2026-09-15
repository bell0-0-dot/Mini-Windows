
package Insta;

import ConfigInsta.Rutas;
import ConfigInsta.ServicioArchivoInsta;
import Excepciones.ArchivoCorruptoException;
import Excepciones.UsernameDuplicadoException;
import Insta.PanelesInsta.JPanelPrincipal;
import Insta.PanelesInsta.PanelApp;
import Insta.PanelesInsta.PanelLogin;
import base.ArchivoUtil;
import base.ListaEnlazada;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author vasqu
 */
public class LoginJFrame extends JFrame{
    static Image iconoVentana;
    public LoginJFrame() {
        this.setTitle("Instagram");
        try {
         iconoVentana = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Insta/Imagenes/LogoBarra.png"));
        this.setIconImage(iconoVentana);
    } catch (Exception e) {
        System.err.println("No se pudo cargar el icono de la ventana: " + e.getMessage());
}
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new JPanelPrincipal());
        this.setMinimumSize(new Dimension(800, 550)); 
        this.setPreferredSize(new Dimension(1000, 650));
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public static void main(String[] args) throws ArchivoCorruptoException, IOException, UsernameDuplicadoException {
        
       
        UsuarioInsta usuario = ServicioArchivoInsta.buscarUsuario("Daya_0_0");
        
        SesionActual.getInstancia().iniciarSesion(usuario);

        JFrame frame = new JFrame("Instagram");
        Image icono = new ImageIcon(LoginJFrame.class.getResource("/Insta/Imagenes/LogoBarra.png")).getImage();
        frame.setIconImage(icono);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new JPanelPrincipal());
        
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public static void crearImagenPrueba(String rutaCompleta, String textoEtiqueta) throws IOException {
    int ancho = 400;
    int alto = 400;

    BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = imagen.createGraphics();

    g2.setColor(new Color(100, 150, 200));
    g2.fillRect(0, 0, ancho, alto);

    g2.setColor(Color.WHITE);
    g2.setFont(new Font("Arial", Font.BOLD, 30));
    g2.drawString(textoEtiqueta, 40, alto / 2);

    g2.dispose();

    File archivoSalida = new File(rutaCompleta);
    archivoSalida.getParentFile().mkdirs();  // crea la carpeta "imagenes" si no existe
    ImageIO.write(imagen, "png", archivoSalida);
}
    private static void crearPublicacionDePrueba() {
    try {
        String autor = "Daya_0_0";
        String rutaArchivoInsta = Rutas.rutaInsta(autor);

        // 1. Cargar la lista existente del usuario
        ListaEnlazada<Publicacion> publicaciones = ArchivoUtil.leerLista(rutaArchivoInsta);

        // 2. Crear las listas enlazadas para menciones y hashtags
        ListaEnlazada<String> menciones = new ListaEnlazada<>();
        menciones.insertarFinal("Paola_1");

        ListaEnlazada<String> hashtags = new ListaEnlazada<>();
        hashtags.insertarFinal("feliz");

        // 3. Instanciar la publicación con tu constructor
        Publicacion nuevaPost = new Publicacion(
            autor,                                      // autor
            "hola @Paola_1 como estas #feliz",           // contenido
            LocalDateTime.now(),                        // fecha
            menciones,                                  // menciones
            hashtags,                                   // hashtags
            "miCasa.jpg",                                // rutaImagen
            null                                        // rutaSticker (o "" si no usas)
        );

     
        publicaciones.insertarFinal(nuevaPost);
        ArchivoUtil.guardarLista(rutaArchivoInsta, publicaciones);

        System.out.println("¡Publicación creada exitosamente desde el main!");

    } catch (Exception e) {
        System.err.println("Error al crear publicación: " + e.getMessage());
        e.printStackTrace();
    }
    
    }
}
