/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta.PanelesInsta;

import ConfigInsta.Rutas;
import ConfigInsta.ServicioArchivoInsta;
import Excepciones.ArchivoCorruptoException;
import Insta.UsuarioInsta;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 *
 * @author vasqu
 */
public class AvatarCircular extends JComponent{
     private Image imagen;
    private int diametro;
    
    public AvatarCircular(Image imagen, int diametro) {
        this.imagen = imagen;
        this.diametro = diametro;
        setPreferredSize(new Dimension(diametro, diametro));
        setMaximumSize(new Dimension(diametro, diametro));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Ellipse2D circulo = new Ellipse2D.Float(0, 0, diametro, diametro);
        g2.setClip(circulo);
        g2.drawImage(imagen, 0, 0, diametro, diametro, null);

        g2.dispose();
    }
    public static AvatarCircular crear(String username, int diametro) throws ArchivoCorruptoException {
        UsuarioInsta usuario = ServicioArchivoInsta.buscarUsuario(username);

        Image imagen;
        if (usuario.getArchivoFoto() == null) {
            imagen = new ImageIcon(AvatarCircular.class.getResource("/Insta/Imagenes/UserIcon.png")).getImage();
        } else {
            imagen = new ImageIcon(Rutas.rutaFotoPerfil(usuario.getUser(), usuario.getArchivoFoto())).getImage();
        }

        return new AvatarCircular(imagen, diametro);
    }
}
