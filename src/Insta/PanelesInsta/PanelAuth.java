/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta.PanelesInsta;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author vasqu
 */
public abstract class PanelAuth extends JPanel{
    protected final Consumer<String> navegar;
    protected JLabel labelError;
    protected final Image Fondo;
    public PanelAuth(Consumer<String> navegar) {
        this.Fondo=new ImageIcon(getClass().getResource("/Insta/Imagenes/FondoLogin.jpg")).getImage();
        this.navegar = navegar;
        this.setOpaque(false);
        this.setLayout(new GridLayout(1, 2, 20, 0));
        JPanel panelIzquierdo = construirPanelIzquierdo();
        JPanel panelDerecho = construirPanelDerecho();
        
        this.add(panelIzquierdo);
        this.add(panelDerecho);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (Fondo != null) {
           
            g.drawImage(Fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
    private JPanel construirPanelIzquierdo() {
        JPanel panelIzquierdo = new JPanel(new GridBagLayout());
    panelIzquierdo.setOpaque(false);

    GridBagConstraints gbcIzq = new GridBagConstraints();
    gbcIzq.gridx = 0;
    gbcIzq.anchor = GridBagConstraints.NONE;

    try {
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Insta/Imagenes/InstaLogo.png"));
        Image logoImg = logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel labelLogo = new JLabel(new ImageIcon(logoImg));
        gbcIzq.gridy = 0;
        gbcIzq.anchor = GridBagConstraints.NORTH;
        gbcIzq.insets = new Insets(0, 10, 5, 10);
        gbcIzq.weighty = 0.0;
        panelIzquierdo.add(labelLogo, gbcIzq);
    } catch (Exception e) {
        System.err.println("No se pudo cargar el logo de Instagram");
    }
    gbcIzq.anchor = GridBagConstraints.CENTER;

    JLabel labelSlogan = new JLabel("<html><div style='text-align: center;'>Mira los momentos cotidianos de<br><b style="
            + "'color:#E1306C; font-size:16px;'>tus mejores amigos.</b></div></html>");
    labelSlogan.setFont(new Font("SansSerif", Font.PLAIN, 18));
    gbcIzq.gridy = 1;
    gbcIzq.insets = new Insets(5, 10, 20, 10);
    panelIzquierdo.add(labelSlogan, gbcIzq);

    try {
        ImageIcon promoIcon = new ImageIcon(getClass().getResource("/Insta/Imagenes/InstaPromo.png"));
        Image promoImg = promoIcon.getImage().getScaledInstance(350, 300, Image.SCALE_SMOOTH);
        JLabel labelImagenPromo = new JLabel(new ImageIcon(promoImg));
        gbcIzq.gridy = 2;
        gbcIzq.insets = new Insets(10, 10, 10, 10);
        panelIzquierdo.add(labelImagenPromo, gbcIzq);
    } catch (Exception e) {
        System.err.println("No se pudo cargar la imagen promocional");
    }

    return panelIzquierdo;
    }
    
    private JPanel crearTarjetaBase() {
       JPanel tarjeta = new JPanel(new GridBagLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(255, 255, 255, 170));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

            g2.setColor(new Color(255, 255, 255, 220));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);

            g2.dispose();
            super.paintComponent(g);
        }
    };
    tarjeta.setOpaque(false);
    tarjeta.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
    return tarjeta;
    }
    
    private JPanel construirPanelDerecho() {
        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setOpaque(false);
        JPanel tarjeta = crearTarjetaBase();
        construirContenidoTarjeta(tarjeta); 
        panelDerecho.add(tarjeta, new GridBagConstraints());
        return panelDerecho;
    }
    
    protected abstract void construirContenidoTarjeta(JPanel tarjeta);
    protected abstract void limpiarCampos();
}
    
    

